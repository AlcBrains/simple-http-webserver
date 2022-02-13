package com.christopher.javaserver.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BaseURLHandler extends AbstractHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HashMap<String, Object> params = parseRequestQuery(exchange);
        String path = (String) params.get("path");
        if (!path.equals("/")) {
            writeResponseBody(exchange, new ArrayList<>(), 404);
        } else {
            writeResponseBody(exchange, new ArrayList<>(), 200);
        }
    }

}
