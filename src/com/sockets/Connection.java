package com.sockets;


import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Connection implements Runnable {

    private final Socket socket;
    private final String RESOURCE = "resources";

    public Connection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        boolean connected = true;

        while(connected){
            try {
                Request request = Request.readRequest(socket.getInputStream());

                if (request.isKeepAlive()) {
                    socket.setKeepAlive(true);
                    socket.setSoTimeout(request.getTimeRating());
                } else {
                    socket.setSoTimeout(300);
                }

                var index = request.getPath().equals("/");
                var isHtml = request.getPath().matches(".*.html$");

                if (!index && !isHtml) {
                    request.setPath(RESOURCE + "/404.html");
                } else {
                    if (index) request.setPath(RESOURCE + "/index.html");
                    else request.setPath(RESOURCE + request.getPath());
                }


                File file = new File(request.getPath());
                Response response;

                if (file.exists()) {
                    response = new Response(request.getProtocol(), 200, "OK");
                } else {
                    response = new Response(request.getProtocol(), 404, "Not Found");
                    file = new File("resources/404.html");
                }

                response.setContent(Files.readAllBytes(file.toPath()));

                response.setHeader("Location", "https://localhost:8000/");
                response.setHeader("Date", response.getServerTime());
                response.setHeader("Server", "MeuServidor/1.0");
                response.setHeader("Content-Type", "text/html");
                response.setHeader("Content-Length", response.getResponseLength());

                // Envia a resposta
                response.setOutput(socket.getOutputStream());
                response.send();

            } catch (IOException ex) {
                //quando o tempo limite terminar encerra a thread
                if (ex instanceof SocketTimeoutException) {
                    try {
                        connected = false;
                        socket.close();
                    } catch (IOException ex1) {
                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
        }
    }
}

