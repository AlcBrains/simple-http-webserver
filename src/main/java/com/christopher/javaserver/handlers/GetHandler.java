package com.christopher.javaserver.handlers;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class GetHandler extends BaseHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Process get requests
        HashMap<String,String> parameters = parseQuery(getRequestQuery(exchange));

        // send response
        writeResponseBody(exchange, parameters);
    }

    @Override
    protected HashMap<String, String> parseQuery(String query) {
        HashMap<String, String> parameters = new HashMap<>();

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

            parameters.merge(key, value, (oldVal, newVal) -> oldVal + "&" + newVal);
        }
        return parameters;
    }
}
