package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.register_and_login.RegisterRequest;
import services.register_and_login.RegisterResponse;
import services.register_and_login.RegisterService;

import java.io.*;
import java.net.HttpURLConnection;

public class RegisterHandler extends RequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                Headers headers = exchange.getResponseHeaders();
                headers.set("Content-Type", "text/html");
                RegisterRequest registerRequest = createRegisterRequest(exchange);

                RegisterService registerService = new RegisterService();
                RegisterResponse registerResponse = registerService.RegisterUser(registerRequest);
                if (registerResponse.getMessage() != null) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                String respData = ObjectEncode.Encode(registerResponse);
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

    private RegisterRequest createRegisterRequest(HttpExchange httpExchange) throws IOException {
        InputStream reqBody = httpExchange.getRequestBody();
        String request = readString(reqBody);
        Gson gson = new Gson();
        RegisterRequest registerRequest = gson.fromJson(request, RegisterRequest.class);
        return registerRequest;
    }

}
