package com.christopher.javaserver.handlers.base;

import com.christopher.javaserver.handlers.AbstractHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.ArrayList;

public class BaseURLHandler extends AbstractHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        writeResponseBody(exchange, new ArrayList<>(), 200);
    }

}
