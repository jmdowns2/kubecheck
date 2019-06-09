package com.kubecheck.server.checks;

public interface ICheck {
    public String getName();
    public CheckResult execute(String resourceName, String namespace);
}
