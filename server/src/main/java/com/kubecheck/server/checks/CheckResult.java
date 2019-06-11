package com.kubecheck.server.checks;

import com.sun.tools.javac.comp.Check;

public class CheckResult {
    public Boolean available;
    public String result;


    public static CheckResult unavailable() {
        CheckResult r = new CheckResult();
        r.available = false;
        return r;
    }
}
