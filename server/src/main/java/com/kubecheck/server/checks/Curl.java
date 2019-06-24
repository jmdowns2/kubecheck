package com.kubecheck.server.checks;

import com.kubecheck.server.services.ResourceService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Curl implements ICheck {

    ResourceService s;

    public Curl(ResourceService s) { this.s = s; }

    public String getName() { return "Curl"; }

    public List<String> getOptions() {

        List<String> ret = new ArrayList<>();
        ret.add("Url");
        return ret;

    }

    public CheckResult execute(String pod, String namespace, Map<String,String> requestParams)  {

        CheckResult result = new CheckResult();

        String url = requestParams.get("Url");


        String[] cmd = new String[] { "sh", "-c", "curl "+url };
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

}
