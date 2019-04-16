package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.clear.ClearResponse;
import services.clear.ClearService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ClearHandler extends RequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
            //Headers reqHeaders = exchange.getRequestHeaders();

            ClearService clearService = new ClearService();

            ClearResponse clearResponse = clearService.clear();
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "text/html");
            //Will always have a response body
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            String respData = ObjectEncode.Encode(clearResponse);
            OutputStream respBody = exchange.getResponseBody();
            writeString(respData, respBody);
            respBody.close();
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        exchange.getResponseBody().close();
    }

}
