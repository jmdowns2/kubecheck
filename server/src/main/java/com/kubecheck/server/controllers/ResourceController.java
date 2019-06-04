package com.kubecheck.server.controllers;

import com.kubecheck.server.models.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @GetMapping("/")
    public Resource index() {
        Resource r = new Resource();
        r.test = "ASDFSD";
        return r;
    }
}
