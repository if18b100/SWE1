//finished - refactor?

package Implementations.plugins;

import Interfaces.Plugin;
import Implementations.RequestImpl;
import Implementations.ResponseImpl;
import Interfaces.Request;
import Interfaces.Response;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

public class ToLower implements Plugin {

    /**
     * Returns a score between 0 and 1 to indicate that the plugin is willing to
     * handle the request. The plugin with the highest score will execute the
     * request.
     *
     * @param req
     * @return A score between 0 and 1
     */
    @Override
    public float canHandle(Request req){
        return (req.getUrl().getPath().startsWith("/tolower")) ? 1.0f : 0.0f;
    };

    /**
     * Called by the server when the plugin should handle the request.
     *
     * @param req
     * @return A new response object.
     */
    @Override
    public Response handle(Request req) throws IOException {
        ResponseImpl response = new ResponseImpl();



        if(req.getContentLength() > 1){
            String[] message = req.getContentString().split("=");
            response.setContent(createHTML(message[1]));
        } else {
            response.setContent(createHTML(null));
            //response.setContent(createHTML(null));
        }
        response.setStatusCode(200);
        response.setContentType("text/html; charset=utf8");
        response.addHeader("Accept-Language", "en-US");

        return response;
    };

    private String createHTML(String text) {
        return "<html>"
                + "<head>"
                + "</head>"
                + "<body>"
                + "Bitte geben Sie einen Text ein, der für Sie konvertiert werden soll:"
                + "<br>"
                + "<form <action='tolower' method='post'>"
                + "<textarea rows='4' cols='50' name='text'> </textarea><br>"
                + "<input type='submit' value='Submit'>"
                + "</form>"
                + "<pre>"
                + (text != null ? text.toLowerCase() : "")
                + "</pre>"
                + "</body>"
                + "</html>";
    }

}
