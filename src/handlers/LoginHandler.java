package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.register_and_login.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LoginHandler extends RequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                LoginRequest loginRequest = createLoginRequest(exchange);
                LoginService loginService = new LoginService();
                Headers headers = exchange.getResponseHeaders();
                headers.set("Content-Type", "text/html");

                LoginResponse loginResponse = loginService.PublicLogin(loginRequest);
                if (loginResponse.getMessage() != null) {
                    if (loginResponse.getMessage().equals("Request property missing or has invalid value")) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                String respData = ObjectEncode.Encode(loginResponse);
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

    private LoginRequest createLoginRequest(HttpExchange httpExchange) throws IOException {
        InputStream reqBody = httpExchange.getRequestBody();
        String request = readString(reqBody);
        Gson gson = new Gson();
        LoginRequest loginRequest = gson.fromJson(request, LoginRequest.class);
        return loginRequest;
    }
}
