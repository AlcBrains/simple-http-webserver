package com.christopher.javaserver.handlers.base;

import com.christopher.javaserver.handlers.AbstractHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.HashMap;

public class BaseURLHandler extends AbstractHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HashMap<String,Object> parameters = parseRequestQuery(exchange);
        writeResponseBody(exchange, parameters);
    }

}
