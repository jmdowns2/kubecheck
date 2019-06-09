package com.kubecheck.server.services;


import com.kubecheck.server.checks.ICheck;
import com.kubecheck.server.checks.Netstat;
import com.kubecheck.server.models.Pod;
import com.kubecheck.server.models.Resource;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.Exec;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.util.Config;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    CoreV1Api api;

    List<ICheck> serviceChecks = new ArrayList<ICheck>();
    List<ICheck> podChecks = new ArrayList<ICheck>();


    ResourceService() {
        init();

        registerPodCheck(new Netstat(this));
    }

    public List<ICheck> getServiceChecks() { return serviceChecks; }
    public List<ICheck> getPodChecks() { return podChecks; }

    protected void registerServiceCheck(ICheck check) {
        serviceChecks.add(check);
    }

    protected void registerPodCheck(ICheck check) {
        podChecks.add(check);
    }

    void init() {
        try {
            ApiClient client = Config.defaultClient();
            client.getHttpClient().setConnectTimeout(30, TimeUnit.SECONDS);
            client.getHttpClient().setReadTimeout(30, TimeUnit.SECONDS);
            client.getHttpClient().setWriteTimeout(30, TimeUnit.SECONDS);
            Configuration.setDefaultApiClient(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
        api = new CoreV1Api();
    }

    public List<Resource> getServices() throws ApiException {
        V1ServiceList services = api.listServiceForAllNamespaces(null, null, null, null, null, null, null, null, null);

        return services.getItems().stream().map((s) -> {

            com.kubecheck.server.models.Service service = new com.kubecheck.server.models.Service();
            service.name = s.getMetadata().getName();
            service.namespace = s.getMetadata().getNamespace();
            service.type = "Service";
            service.port = s.getSpec().getPorts().get(0).getPort();

            Map<String, String> selector =  s.getSpec().getSelector();

            try {
                if(selector != null) {

                    String labelSelector = selector.entrySet().stream().map((entry) -> {
                        return entry.getKey()+"="+entry.getValue();
                    }).findFirst().get();

                    V1PodList pods = api.listPodForAllNamespaces(null, null, null, labelSelector, null, null, null, null, null);

                    service.children = pods.getItems().stream().map((p) -> {
                        Pod pod = new Pod();

                        pod.name = p.getMetadata().getName();
                        pod.namespace = p.getMetadata().getNamespace();
                        pod.type = "Pod";
                        return pod;
                    }).collect(Collectors.toList());
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }


            return service;

        }).collect(Collectors.toList());
    }

    public String executePodCmd(String podName, String namespace, String[] cmd) throws ApiException, InterruptedException, IOException {

        Exec exec = new Exec();

        final Process proc =
                exec.exec(
                        namespace,
                        podName,
                        cmd,
                        false,
                        false);

        BufferedReader buffReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        proc.waitFor();

        String out = "";
        String line;

        while((line = buffReader.readLine()) != null) {
            out += line + "\n";
        }
        proc.destroy();

        int exitValue = proc.exitValue();

        return out;
    }


}
