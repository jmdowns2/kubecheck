package com.kubecheck.server.controllers;

import com.kubecheck.server.checks.Netstat;
import com.kubecheck.server.models.Resource;
import com.kubecheck.server.services.ResourceService;
import io.kubernetes.client.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ResourceController {

    @Autowired
    ResourceService service;

    @GetMapping("/services")
    public List<Resource> getServices() throws ApiException {
        return service.getServices();
    }

    @GetMapping("/")
    public Resource index() throws IOException, ApiException, InterruptedException {
        List<Integer> ports = new Netstat(service).check("testing-5cdfd64d87-m2tvw", "default");
      //  String out = service.executePodCmd("testing-6df864bdff-hcm67", "default", new String[]{"sh", "-c", "netstat -l" });

        Resource r = new Resource();
        return r;
    }
}
