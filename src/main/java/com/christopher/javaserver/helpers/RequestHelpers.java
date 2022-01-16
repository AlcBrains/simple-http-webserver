package com.christopher.javaserver.helpers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHelpers {

    public static void writeResponseBody(HttpExchange exchange, Map<String, List<String>> parameters) throws IOException {
        StringBuilder response = new StringBuilder();
        for (String key : parameters.keySet())
            response.append(key).append(" = ").append(String.join(", ", parameters.get(key))).append("\n");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    public static Map<String, List<String>> parseQuery(String query){
        Map<String, List<String>> parameters = new HashMap<>();

        if (query == null) {
            return parameters;
        }


        String[] pairs = query.split("[&]");

        // TODO : NULL HANDLING ?
        for (String pair : pairs) {
            String[] param = pair.split("[=]");
            if (param.length == 0) {
                continue;
            }

            String key = URLDecoder.decode(param[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(param[1], StandardCharsets.UTF_8);

            if (parameters.get(key) != null) {
                parameters.get(key).add(value);
            } else {
                parameters.put(key, new ArrayList<>() {{
                    add(value);
                }});
            }
        }
        return parameters;
    }
}
