package com.kubecheck.server.checks;

import com.kubecheck.server.services.ResourceService;
import io.kubernetes.client.ApiException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Netstat {

    ResourceService s;

    public Netstat(ResourceService s) { this.s = s; }

    public List<Integer> check(String pod, String namespace) throws InterruptedException, ApiException, IOException {

        String[] cmd = new String[] { "sh", "-c", "netstat -l" };
        String res = s.executePodCmd(pod, namespace, cmd);
        if(res == null)
            return null;

        String[] lines  = res.split("\n");
        return Arrays.stream(lines).map( line -> {
            return parseListenPort(line);
        }).filter((port) -> { return port > 0; }).collect(Collectors.toList());
    }

    private Integer parseListenPort(String line)
    {
        // Example response
        // tcp        0      0 0.0.0.0:http            0.0.0.0:*               LISTEN
        String[] items = line.split("\\s+");
        if(items.length <= 1) return -1;
        if(items[0].compareToIgnoreCase("tcp") != 0) return -1;
        if(items[5].compareToIgnoreCase("LISTEN") != 0) return -1;

        String localAddress = items[3];
        String[] addressPart = localAddress.split(":");
        if(addressPart.length < 2) return -1;

        if(addressPart[1].compareToIgnoreCase("http") == 0)
            return 80;
        if(addressPart[1].compareToIgnoreCase("https") == 0)
            return 443;
        return Integer.parseInt(addressPart[1]);
    }


}
