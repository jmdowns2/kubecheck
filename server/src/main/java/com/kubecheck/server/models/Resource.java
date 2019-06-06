package com.kubecheck.server.models;

import java.util.List;

public class Resource {

    public String type;
    public String name;
    public List<Resource> children;
}
