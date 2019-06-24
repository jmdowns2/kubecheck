package com.kubecheck.server.checks;

import java.util.List;
import java.util.Map;

public interface ICheck {
    public String getName();
    public CheckResult execute(String resourceName, String namespace, Map<String,String> requestParams);

    public List<String> getOptions();

}
