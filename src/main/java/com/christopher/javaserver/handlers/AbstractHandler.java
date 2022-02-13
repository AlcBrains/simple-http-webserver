package com.christopher.javaserver.handlers;

import com.christopher.javaserver.db.Connector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.List;
import java.util.Map;

public abstract class AbstractHandler implements HttpHandler {

    protected Connector connector;
    ObjectMapper objectMapper;

    public AbstractHandler() {

        connector = Connector.getInstance("jdbc:mysql://localhost:3306/employees", "root", "root");
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Writes the provided response to an outputStream and sends it to the client
     *
     * @param exchange     the HttpExchange object to write data to
     * @param responseData the actual data
     * @throws IOException if the responseData is malformed
     */

    protected void writeResponseBody(HttpExchange exchange, List<Object> responseData, int rCode) throws IOException {

        String response = objectMapper.writeValueAsString(responseData);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(rCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    /**
     * Checks for request method and parses the request parameters (or the request body) and returns parsed request
     * data
     *
     * @param exchange the HttpExchange object handling the request
     * @return the parsed request data in hashmap form
     * @throws IOException if there is malformed JSON data in POST type requests
     */
    protected HashMap<String, Object> parseRequestQuery(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        return method.equals("GET")
                ? parseGetRequestParam(exchange.getRequestURI().getPath(), exchange.getRequestURI().getQuery())
                : parsePostRequestBody(exchange.getRequestURI().getPath(), exchange.getRequestBody());
    }

    /**
     * Parses GET request parameters
     *
     * @param query the String representation of the request parameters
     * @return the parsed request data in hashmap form
     */
    private HashMap<String, Object> parseGetRequestParam(String path, String query) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("path", path);
        parameters.put("method", "GET");
        if (query == null) {
            return parameters;
        }
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
     *
     * @param requestBody the InputStream representation of the request body
     * @return the parsed request data in hashmap form
     * @throws IOException if there is malformed JSON data in the request body
     */
    private HashMap<String, Object> parsePostRequestBody(String path, InputStream requestBody) throws IOException {
        String query = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        HashMap<String, Object> parameters = (HashMap<String, Object>) objectMapper.readValue(query, Map.class);
        parameters.put("path", path);
        parameters.put("method", "POST");
        return parameters;
    }
}
