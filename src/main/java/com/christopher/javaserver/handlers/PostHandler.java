package com.christopher.javaserver.handlers;

import com.christopher.javaserver.helpers.RequestHelpers;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


public class PostHandler implements BaseHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // parse request
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        Map<String, List<String>> parameters = RequestHelpers.parseQuery(query);

        // send response
        RequestHelpers.writeResponseBody(exchange,parameters);
    }


}
