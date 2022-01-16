package com.christopher.javaserver.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractHandler implements HttpHandler {

    ObjectMapper objectMapper;

    public AbstractHandler() {
        objectMapper = new ObjectMapper();
    }

    /**
     * TODO : remove when debugging is finished
     * Prints basic request info. Used for debugging purposes
     * @param exchange the HttpExchange object under inspection
     */
    private void printRequestInfo(HttpExchange exchange) {
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
     * Writes the provided response to an outputStream and sends it to the client
     * @param exchange the HttpExchange object to write data to
     * @param responseData the actual data
     * @throws IOException if the responseData is malformed
     */
    protected void writeResponseBody(HttpExchange exchange, Map<String, Object> responseData) throws IOException {
        String response = objectMapper.writeValueAsString(responseData);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * Checks for request method and parses the request parameters (or the request body) and returns parsed request
     * data
     * @param exchange the HttpExchange object handling the request
     * @return the parsed request data in hashmap form
     * @throws IOException if there is malformed JSON data in POST type requests
     */
    protected HashMap<String, Object> parseRequestQuery(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        return method.equals("GET")
                ? parseGetRequestParam(exchange.getRequestURI().getQuery())
                : parsePostRequestBody(exchange.getRequestBody());
    }

    /**
     * Parses GET request parameters
     * @param query the String representation of the request parameters
     * @return the parsed request data in hashmap form
     */
    private HashMap<String, Object> parseGetRequestParam(String query) {
        HashMap<String, Object> parameters = new HashMap<>();
        String[] pairs = query.split("[&]");
        for (String pair : pairs) {
            String[] param = pair.split("[=]");
            if (param.length == 0) {
                continue;
            }
            String key = URLDecoder.decode(param[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(param[1], StandardCharsets.UTF_8);
            parameters.merge(key, value, (oldVal, newVal) -> oldVal + "&" + newVal);
        }
        return parameters;
    }

    /**
     * Parses POST request body
     * @param requestBody the InputStream representation of the request body
     * @return the parsed request data in hashmap form
     * @throws IOException if there is malformed JSON data in the request body
     */
    private HashMap<String, Object> parsePostRequestBody(InputStream requestBody) throws IOException {
        String query = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        return (HashMap<String, Object>) objectMapper.readValue(query, Map.class);
    }
}
