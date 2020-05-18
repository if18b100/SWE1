package Implementations;
import Interfaces.Plugin;
import Interfaces.Request;
import Interfaces.Response;

public class PluginImpl implements Plugin {

    /**
     * Returns a score between 0 and 1 to indicate that the plugin is willing to
     * handle the request. The plugin with the highest score will execute the
     * request.
     *
     * @param req
     * @return A score between 0 and 1
     */
    public float canHandle(Request req){
        float score;
        if(req.isValid()){
            score = 0.9f;
        } else {
            score = 0.0f;
        }
        return score;
    };

    /**
     * Called by the server when the plugin should handle the request.
     *
     * @param req
     * @return A new response object.
     */
    public Response handle(Request req){
        ResponseImpl response = new ResponseImpl();

        if (canHandle(req) > 0.0f){
            response.setStatusCode(200);
        } else {
            response.setStatusCode(500);
        }
        response.setContentType("text/html; charset=utf8");
        response.addHeader("Accept-Language", "en-US");
        response.setContent(String.format("Servertest: %s", req.getUrl().getRawUrl()));

        return response;
    };
}
