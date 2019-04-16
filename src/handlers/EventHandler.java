package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.event.EventRequest;
import services.event.EventResponse;
import services.event.EventService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class EventHandler extends RequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                Headers headers = exchange.getResponseHeaders();
                headers.set("Content-Type", "text/html");
                String path = exchange.getRequestURI().toString();
                //"/event"
                String parameter;
                if (path.length() > 7) {
                    parameter = path.substring(7);
                } else {
                    parameter = "";
                }
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");

                    EventService eventService = new EventService();
                    EventRequest eventRequest = new EventRequest(authToken, parameter);
                    EventResponse eventResponse;
                    if (parameter.equals("")) {
                        eventResponse = eventService.GetAllEvents(eventRequest);
                    } else {
                        eventResponse = eventService.GetEvent(eventRequest);
                    }
                    if (eventResponse.getMessage() != null) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    }
                    String respData = ObjectEncode.Encode(eventResponse);
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(respData, respBody);
                    respBody.close();


                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
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
