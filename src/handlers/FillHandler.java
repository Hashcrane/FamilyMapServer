package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.fill.FillResponse;
import services.fill.FillService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class FillHandler extends RequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                Headers headers = exchange.getResponseHeaders();
                headers.set("Content-Type", "text/html");
                String path = exchange.getRequestURI().toString();
                //"/person"
                boolean valid = false;
                String username = "";
                String generations = "";
                StringBuilder sb = new StringBuilder();
                if (path.length() > 5) {
                    valid = true;
                    char c;
                    int i;
                    int len = path.length();
                    for (i = 6; (i < len); ++i) {
                        c = path.charAt(i);
                        if (c == '/') break;
                        sb.append(c);
                    }
                    username = sb.toString();
                    if (len > i) {
                        generations = path.substring(i + 1);
                    }
                    else {
                        generations = "4";
                    }
                }
                FillService fillService = new FillService();

                if (valid) {
                    FillResponse fillResponse = fillService.Fill(username, Integer.parseInt(generations));

                    if (fillResponse.getMessage().equals("Invalid username or generations parameter") ||
                            fillResponse.getMessage().equals("Internal server error")) {
                        //Error
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    String respData = ObjectEncode.Encode(fillResponse);
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(respData, respBody);
                    respBody.close();
                } else {
                    FillResponse fillResponse = fillService.Fill("fail", -1);
                    String respData = ObjectEncode.Encode(fillResponse);
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(respData, respBody);
                    respBody.close();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_ACCEPTABLE, 0);
                }
            } else {
                //Expected GET but was not
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            exchange.getResponseBody().close();
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
