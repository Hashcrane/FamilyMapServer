package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.load.LoadRequest;
import services.load.LoadResponse;
import services.load.LoadService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LoadHandler extends RequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                Headers headers = exchange.getResponseHeaders();
                headers.set("Content-Type", "text/html");
                LoadRequest loadRequest = createLoadRequest(exchange);
                LoadService loadService = new LoadService();
                LoadResponse loadResponse = loadService.Load(loadRequest);
                if (loadResponse.getMessage() == null) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                }
                String respData = ObjectEncode.Encode(loadResponse);
                OutputStream respBody = exchange.getResponseBody();
                writeString(respData, respBody);
                respBody.close();


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

    private LoadRequest createLoadRequest(HttpExchange httpExchange) throws IOException {
        InputStream reqBody = httpExchange.getRequestBody();
        String request = readString(reqBody);
        Gson gson = new Gson();
        LoadRequest loadRequest = gson.fromJson(request, LoadRequest.class);
        return loadRequest;
    }
}
