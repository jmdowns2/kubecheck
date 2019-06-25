package com.kubecheck.server.checks;

import com.kubecheck.server.services.ResourceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Shell implements ICheck {


    ResourceService s;

    public Shell(ResourceService s) { this.s = s; }

    @Override
    public String getName() {
        return "Shell";
    }

    @Override
    public CheckResult execute(String pod, String namespace, Map<String, String> requestParams) {

        CheckResult result = new CheckResult();

        String cmdValue = requestParams.get("cmd");


        String[] cmd = new String[] { "sh", "-c", cmdValue };
        String res = null;
        try {
            res = s.executePodCmd(pod, namespace, cmd);
        } catch (Exception e) {
            e.printStackTrace();
            result.available = false;
            return result;
        }
        if(res == null) {
            result.available = false;
            return result;
        }


        result.available = true;
        result.result = res;
        return result;

    }

    @Override
    public List<String> getOptions() {
        List<String> ret = new ArrayList<>();
        ret.add("cmd");
        return ret;
    }
}
