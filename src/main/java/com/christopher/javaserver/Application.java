package com.christopher.javaserver;

import com.christopher.javaserver.handlers.GetHandler;
import com.christopher.javaserver.handlers.PostHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Application {

    public static void main(String[] args) throws IOException {
        int port = 9000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        String[] urls = new String[]{"users", "secondarg", "thirdarg"};

        System.out.println("server started at " + port);
        server.createContext("/", new GetHandler());
        server.createContext("/", new PostHandler());
        server.setExecutor(null);
        server.start();
    }
}
