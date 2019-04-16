package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.*;

import java.io.IOException;

public class FileHandler extends RequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                String urlPath = exchange.getRequestURI().toString();
                if (urlPath == null || urlPath.equals("/")) {
                    urlPath = "/index.html";
                }
                String currentWorkingDir = System.getProperty("user.dir");
                currentWorkingDir += "/web" + urlPath;
                Path myPath = FileSystems.getDefault().getPath(currentWorkingDir);
                //Headers headers = exchange.getResponseHeaders();
               // headers.set("Content-Type", "text/html");
                String filePathstr;
                File test = new File(myPath.toString());


                if (test.exists()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(test.toPath(), respBody);
                    respBody.close();
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    filePathstr = "web/HTML/404.html";
                    File test1 = new File(filePathstr);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(test1.toPath(), respBody);
                    respBody.close();
                }

            } else {
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
