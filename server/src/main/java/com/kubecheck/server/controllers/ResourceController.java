package com.kubecheck.server.controllers;

import com.kubecheck.server.checks.CheckResult;
import com.kubecheck.server.checks.ICheck;
import com.kubecheck.server.checks.Netstat;
import com.kubecheck.server.models.Resource;
import com.kubecheck.server.services.ResourceService;
import io.kubernetes.client.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ResourceController {

    @Autowired
    ResourceService service;

    @GetMapping("/services")
    public List<Resource> getServices() throws ApiException {
        return service.getServices();
    }

    @GetMapping("/services/checks")
    public List<ICheck> getServiceChecks() {
        return service.getServiceChecks();
    }

    @GetMapping("/services/{namespace}/{name}/check/{checkName}")
    public CheckResult serviceCheck(@PathVariable String namespace, @PathVariable String name, @PathVariable String checkName, Map<String,String> requestParams) throws Exception {
        ICheck check =  service.getServiceChecks().stream()
                .filter((c) -> c.getName().compareToIgnoreCase(checkName) == 0)
                .findFirst().orElseThrow(() -> new Exception(checkName+" not found"));

        return check.execute(name, namespace, requestParams);
    }


    @GetMapping("/pods/checks")
    public List<ICheck> getPodsChecks() {
        return service.getPodChecks();
    }

    @GetMapping("/pods/{namespace}/{name}/check/{checkName}")
    public CheckResult podCheck(@PathVariable String namespace, @PathVariable String name, @PathVariable String checkName, @RequestParam Map<String,String> requestParams) throws Exception {
        ICheck check =  service.getPodChecks().stream()
                .filter((c) -> c.getName().compareToIgnoreCase(checkName) == 0)
                .findFirst().orElseThrow(() -> new Exception(checkName+" not found"));

        return check.execute(name, namespace, requestParams);
    }

    @GetMapping("/")
    public String index() throws IOException, ApiException, InterruptedException {
        return "OK";
    }
}
