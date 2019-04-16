package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.person.PersonRequest;
import services.person.PersonResponse;
import services.person.PersonService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class PersonHandler extends RequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                Headers headers = exchange.getResponseHeaders();
                headers.set("Content-Type", "text/html");
                String path = exchange.getRequestURI().toString();
                //"/person"
                String parameter;
                if (path.length() > 8) {
                    parameter = path.substring(8);
                } else {
                    parameter = "";
                }

                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");

                    PersonService personService = new PersonService();
                    PersonRequest personRequest = new PersonRequest(authToken, parameter);
                    PersonResponse personResponse;
                    if (parameter.equals("")) {
                        personResponse = personService.GetFamily(personRequest);
                    } else {
                        personResponse = personService.GetPerson(personRequest);
                    }
                    if (personResponse.getMessage() != null) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    } else {
                        //invalid auth code
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    }
                    String respData = ObjectEncode.Encode(personResponse);
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(respData, respBody);
                    respBody.close();


                } else {
                    //no Auth code
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
