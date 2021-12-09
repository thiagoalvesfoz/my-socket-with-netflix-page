package com.sockets;


import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class Response {

    private String protocol;
    private int status;
    private String message;
    private byte[] content;
    private Map<String, List> headers = new TreeMap<>();
    private OutputStream output;

    public Response(String protocol, int status, String message) {
        this.protocol = protocol;
        this.status = status;
        this.message = message;
    }

    public void send() throws IOException {
        output.write(buildHeader());
        output.write(content);
        output.flush();
    }

    public void setHeader(String key, String... values) {
        headers.put(key, Arrays.asList(values));
    }

    public String getResponseLength() {
        return this.getContent().length+"";
    }

    public String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    private byte[] buildHeader() {
        return this.toString().getBytes();
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(protocol).append(" ").append(status).append(" ").append(message).append("\r\n");
        for (Map.Entry<String, List> entry : headers.entrySet()) {
            str.append(entry.getKey());
            String item = Arrays.toString(entry.getValue().toArray()).replace("[", "").replace("]", "");
            str.append(": ").append(item).append("\r\n");
        }
        str.append("\r\n");
        return str.toString();
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Map<String, List> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List> headers) {
        this.headers = headers;
    }

    public OutputStream getOutput() {
        return output;
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }
}
