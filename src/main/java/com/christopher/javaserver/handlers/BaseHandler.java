package com.christopher.javaserver.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

abstract class BaseHandler implements HttpHandler {

    void printRequestInfo(HttpExchange exchange) {
        System.out.println("-- headers --");
        Headers requestHeaders = exchange.getRequestHeaders();
        requestHeaders.entrySet().forEach(System.out::println);

        System.out.println("-- principal --");
        HttpPrincipal principal = exchange.getPrincipal();
        System.out.println(principal);

        System.out.println("-- method --");
        String requestMethod = exchange.getRequestMethod();
        System.out.println(requestMethod);

        System.out.println("-- query --");
        URI requestURI = exchange.getRequestURI();
        String query = requestURI.getQuery();
        System.out.println(query);
    }

    /**
     * returns the queryParams from the requestURI
     *
     * @return String representation of request query
     */
    protected String getRequestQuery(HttpExchange exchange) {
        return exchange.getRequestURI().getQuery();
    }

    protected abstract HashMap<String, String> parseQuery(String query);

    protected void writeResponseBody(HttpExchange exchange, Map<String, String> responseData) throws IOException {
        StringBuilder response = new StringBuilder();

        //Todo: Build valid json
        for (String key : responseData.keySet())
            response.append(key).append(" = ").append(String.join(", ", responseData.get(key))).append("\n");
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}
