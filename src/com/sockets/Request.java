package com.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Request {

    private String protocol;
    private String path;
    private String method;
    private boolean keepAlive;
    private int timeRating;
    private final Map<String, List> headers = new TreeMap<>();

    public static Request readRequest(InputStream input) throws IOException {
        Request req = new Request();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
        System.out.println("[Request Info]");

        String line = buffer.readLine();
        String[] dataReq = line.split(" ");
        req.setMethod(dataReq[0]);
        req.setPath(dataReq[1]);
        req.setProtocol(dataReq[2]);

        System.out.println("Path: " + req.getPath());
        System.out.println("Method: " + req.getMethod());
        System.out.println("Protocol: " + req.getProtocol() + "\n");

        String dataHeader = buffer.readLine();


        while (dataHeader != null && !dataHeader.isEmpty()) {
            System.out.println(dataHeader);
            String[] lineHeader = dataHeader.split(":");
            req.setHeader(lineHeader[0], lineHeader[1].trim().split(","));
            dataHeader = buffer.readLine();
        }

        if (req.getHeaders().containsKey("Connection")) {
            req.setKeepAlive(req.getHeaders().get("Connection").get(0).equals("keep-alive"));
        }

        return req;
    }

    public void setHeader(String key, String... values) {
        headers.put(key, Arrays.asList(values));
    }


    public Map<String, List> getHeaders() {
        return headers;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public int getTimeRating() {
        return timeRating;
    }

    public void setTimeRating(int timeRating) {
        this.timeRating = timeRating;
    }
}
