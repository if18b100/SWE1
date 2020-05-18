package Interfaces;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

public interface Plugin {
	/**
	 * Returns a score between 0 and 1 to indicate that the plugin is willing to
	 * handle the request. The plugin with the highest score will execute the
	 * request.
	 * 
	 * @param req
	 * @return A score between 0 and 1
	 */
	float canHandle(Request req);

	/**
	 * Called by the server when the plugin should handle the request.
	 * 
	 * @param req
	 * @return A new response object.
	 */
	Response handle(Request req) throws IOException, SQLException, ClassNotFoundException, ParserConfigurationException, SAXException;
}
