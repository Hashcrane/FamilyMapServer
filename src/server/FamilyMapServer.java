package server;

import com.sun.net.httpserver.HttpServer;
import dataAccess.DataAccessException;
import dataAccess.Database;
import handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Main server class
 */
public class FamilyMapServer {

    private HttpServer server;

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Please specify a port number");
            System.exit(0);
        }
        int port = Integer.parseInt(args[0]);
        if (port > 0 && port < 65536) {
            FamilyMapServer familyMapServer = new FamilyMapServer();

            try {
                familyMapServer.startServer(port);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("FamilyMapServer failed to start");
                System.exit(0);
            }
        } else {
            System.out.println("Please specify a port number between 0 and 65536");
        }
    }


    private void startServer(int port) throws IOException {
        Database database = new Database();
        try {
            database.createTables();
        } catch (DataAccessException e) {
            e.printStackTrace();
            System.out.println("Error Creating tables: Server exiting");
            System.exit(0);
        }
        System.out.println("Initializing HTTP Server");
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        server = HttpServer.create(serverAddress, 10);
        server.setExecutor(null);
        System.out.println("Creating contexts");
        registerHandlers(server);
        System.out.println("Starting server");
        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }

    private void registerHandlers(HttpServer server) {
        server.createContext("/", new FileHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event", new EventHandler());
    }

}
