package com.christopher.javaserver;

import com.christopher.javaserver.handlers.SalaryHandler;
import com.christopher.javaserver.handlers.TitleHandler;
import com.christopher.javaserver.handlers.base.BaseURLHandler;
import com.christopher.javaserver.handlers.DepartmentHandler;
import com.christopher.javaserver.handlers.EmployeeHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Application {
    private static final int THREAD_COUNT = 2;
    private static final int PORT_NO = 9000;


    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT_NO), 0);


        server.createContext("/", new BaseURLHandler());
        server.createContext("/employees", new EmployeeHandler());
        server.createContext("/departments", new DepartmentHandler());
        server.createContext("/salaries", new SalaryHandler());
        server.createContext("/titles", new TitleHandler());

        Executor executor = Executors.newFixedThreadPool(THREAD_COUNT);

        server.setExecutor(executor);
        server.start();

        System.out.println("server started at " + PORT_NO);

    }
}
