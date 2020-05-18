package server;

import Implementations.PluginManagerImpl;
import Interfaces.PluginManager;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    static PluginManagerImpl pluginManager = new PluginManagerImpl();

    public void start(int port, String file) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        System.out.println("Socket offen");

        while(!socket.isClosed()){
            ServerRun serverRun = new ServerRun(socket.accept(), file);
            Thread thread = new Thread(serverRun);
            thread.start();
        }
    }
}
