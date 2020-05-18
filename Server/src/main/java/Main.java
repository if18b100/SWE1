import server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        Server server;
        String input = System.getProperty("user.home") + "/Documents/Studium/FH/Informatik/Semester3/SWE1/Server";

        int port = 8080;

        try{
            System.out.printf("Starting Server - Port: %d\n", port);
            server = new Server();
            server.start(port, input);
            System.out.println("Server successfully started!");
        } catch(IOException exception){
            System.out.printf("Error: %s", exception.getMessage());
        }
    }
}
