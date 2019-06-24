package com.kubecheck.server.checks;

import com.kubecheck.server.services.ResourceService;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServicePorts implements ICheck {

    ResourceService s;

    public ServicePorts(ResourceService s) { this.s = s; }

    public String getName() { return "Service Ports"; }
    public List<String> getOptions() { return null; }

    public CheckResult execute(String resourceName, String namespace, Map<String,String> requestParams)
    {
        V1Service service;
        try {
            service = s.getService(resourceName, namespace);
        } catch (ApiException e) {
            e.printStackTrace();
            return CheckResult.unavailable();
        }

        CheckResult r = new CheckResult();
        r.available = true;
        r.result = "Exposed ports = " +
                service.getSpec().getPorts().stream()
                        .map((p) -> { return p.getPort().toString(); }).collect(Collectors.joining(", "));
        return r;
    }

}
