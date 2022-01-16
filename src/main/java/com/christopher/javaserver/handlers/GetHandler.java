package com.christopher.javaserver.handlers;

import com.christopher.javaserver.helpers.RequestHelpers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class GetHandler implements BaseHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Process get requests
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        Map<String, List<String>> parameters = RequestHelpers.parseQuery(query);



        // send response
        RequestHelpers.writeResponseBody(exchange, parameters);
    }
}
