package com.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8000);
        ExecutorService pool = Executors.newFixedThreadPool(20);
        Logger.getLogger(Main.class.getName()).log(Level.INFO, "Servidor iniciado na porta 8000");
        while (true) pool.execute(new Connection(server.accept()));
    }
}
