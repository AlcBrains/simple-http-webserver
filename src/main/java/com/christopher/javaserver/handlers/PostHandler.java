package com.christopher.javaserver.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class PostHandler extends BaseHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // parse request
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        Map<String, String> parameters = parseQuery(query);
        // send response
        writeResponseBody(exchange, parameters);
    }


    @Override
    protected HashMap<String, String> parseQuery(String query) {
        //parse query as json
        return null;
    }
}
