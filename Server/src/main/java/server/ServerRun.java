package server;

import Interfaces.Plugin;
import Interfaces.Response;
import Implementations.RequestImpl;
import Implementations.ResponseImpl;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class ServerRun implements Runnable{
    private Socket socket;
    private String file;

    public ServerRun(Socket socket, String file){
        this.file = file;
        this.socket = socket;
    }

    private Plugin chosePlugin(RequestImpl request, List<Plugin> plugins){
        float score = 0.0f;
        Plugin plugin = null;

        for(Plugin buffer: plugins){
            if(buffer.canHandle(request) > score){
                score = buffer.canHandle(request);
                plugin = buffer;
            }
        }
        return plugin;
    }

    @Override
    public void run() {
        try {
            RequestImpl request = new RequestImpl(socket.getInputStream());
            Response response = new ResponseImpl();
            List<Plugin> plugins = null;


            //No else, since invalid request cant be properly responded
            if (request.isValid()) {
                System.out.println("Start PluginManager");
                plugins = Server.pluginManager.getPlugins();
                Plugin plugin = chosePlugin(request, plugins);

                if (plugin == null) {
                    String url = request.getUrl().getPath();
                    System.out.printf("URL: %s", url);
                    System.out.println();
                    this.file += "/files/myfiles" + url;


                    try {
                        response.setStatusCode(200);
                        //response.setContentType("text/html; charset=utf8");
                        response.addHeader("Accept-Language", "en-US");
                        response.setContent(Files.readAllBytes(Paths.get(this.file)));
                    } catch (NoSuchFileException exception) {
                        System.out.printf("Error %s\n", exception.getMessage());
                        exception.printStackTrace();
                        response.setStatusCode(404);
                        response.setContent("404 - No such file or plugin");
                    }


                } else {
                    System.out.printf("Plugin %s handles request\n", plugin.getClass());
                    response = plugin.handle(request);
                }
            }
            response.send(socket.getOutputStream());
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
