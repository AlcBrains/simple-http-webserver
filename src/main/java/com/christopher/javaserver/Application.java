package com.christopher.javaserver;

import com.christopher.javaserver.authenticator.SampleAuthenticator;
import com.christopher.javaserver.handlers.SalaryHandler;
import com.christopher.javaserver.handlers.TitleHandler;
import com.christopher.javaserver.handlers.base.BaseURLHandler;
import com.christopher.javaserver.handlers.DepartmentHandler;
import com.christopher.javaserver.handlers.EmployeeHandler;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Application {
    private static final int THREAD_COUNT = 2;
    private static final int PORT_NO = 9000;


    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT_NO), 0);

        List<HttpContext> contextArrayList = new ArrayList<>();

        contextArrayList.add(server.createContext("/", new BaseURLHandler()));
        contextArrayList.add(server.createContext("/employees", new EmployeeHandler()));
        contextArrayList.add(server.createContext("/departments", new DepartmentHandler()));
        contextArrayList.add(server.createContext("/salaries", new SalaryHandler()));
        contextArrayList.add(server.createContext("/titles", new TitleHandler()));

        for (HttpContext context: contextArrayList) {
            context.setAuthenticator(new SampleAuthenticator("test"));
        }

        Executor executor = Executors.newFixedThreadPool(THREAD_COUNT);

        server.setExecutor(executor);
        server.start();

        System.out.println("server started at " + PORT_NO);

    }
}
