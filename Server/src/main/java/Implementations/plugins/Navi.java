package Implementations.plugins;

import Interfaces.Plugin;
import Implementations.RequestImpl;
import Implementations.ResponseImpl;
import Interfaces.Request;
import Interfaces.Response;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;

public class Navi implements Plugin {

    boolean xmlParsed = false;
    private SortedSet<String> set;
    private Map<String, SortedSet<String>> navi_data = null;

    private static String getFile() throws FileNotFoundException {
        String filename = "austria-latest.osm";
        //String inputFile = System.getProperty("user.home") + "/Documents/Studium/FH/Informatik/Semester3/SWE1/Server/files/" + filename;
        String inputFile = "/home/alex/Documents/Studium/FH/Informatik/Semester3/SWE1/Server/files/osm/austria-latest.osm";
        System.out.println("Valid input file");
        return inputFile;

    }

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
        System.out.println("Kann ich das?");
        return (req.getUrl().getPath().startsWith("/navi")) ? 1.0f : 0.0f;
    };

    /**
     * Called by the server when the plugin should handle the request.
     *
     * @param req
     * @return A new response object.
     */
    @Override
    public Response handle(Request req) throws IOException, ParserConfigurationException, SAXException {

        Response response = new ResponseImpl();
        System.out.println(req.getUrl().getRawUrl());

        if (req.getUrl().getRawUrl().equals("/navi")) {
            response.setContent("<html>"
                    + "<head>"
                    + "</head>"
                    + "<body>"
                    + "<form <action='navi' method='get'>"
                    + "Streetname: <input type='text' name='street'>"
                    + "<input type='submit' value='Submit'>"
                    + "</form>"
                    + "</body>"
                    + "</html>");
        }

        if (req.getUrl().getRawUrl().equals("/navi/parseNew")) {
            System.out.println("PARSENEW");
            xmlParsed = false;

            response.setContent("<html>"
                    + "<head>"
                    + "</head>"
                    + "<body>"
                    + "File neu <a href='/navi?street=/'>parsen</a>"
                    + "</body>"
                    + "</html>");
        }


        if (req.getUrl().getRawUrl().startsWith("/navi?")) {

            if (!xmlParsed) {
                System.out.println("PARSING");

                navi_data = xmlRead();
//                xml_got_parsed = true;
                System.out.println("FINISHED PARSING");
                //xml_got_parsed gets TRUE
            }

            if (xmlParsed) {
                System.out.println("SEARCHFOR");
                StringBuilder cities = new StringBuilder();
                if (req.getUrl().getRawUrl().startsWith("/navi?street=")) {
                    String street = req.getUrl().getParameter().get("street");
                    if (!street.equals("")) {
                        street = street.replaceAll("\\+", " ")
                                .toLowerCase()
                                .replaceAll("%df", "ß")
                                .replaceAll("%e4", "ä")
                                .replaceAll("%f6", "ö")
                                .replaceAll("%fc", "ü");


                        if (navi_data.containsKey(street)) {
                            set = navi_data.get(street);
                            for (String s : set) {
                                cities.append(
                                        s.replaceAll("ß", "&szlig")
                                                .replaceAll("ä", "&auml")
                                                .replaceAll("ö", "&ouml")
                                                .replaceAll("ü", "&uuml")
                                                .replaceAll("Ä", "&Auml")
                                                .replaceAll("Ö", "&Ouml")
                                                .replaceAll("Ü", "&Uuml"));
                                cities.append("<br>");
                            }
                        } else {
                            if (req.getUrl().getRawUrl().startsWith("/navi?street=/")) {
                                cities = new StringBuilder("Please enter a city.");
                            } else {
                                cities = new StringBuilder("Street does not exist.");
                            }
                        }
                    }
                }

                response.setContent("<html>"
                        + "<head>"
                        + "</head>"
                        + "<body>"
                        + "<form <action='navi' method='get'>"
                        + "Streetname: <input type='text' name='street'>"
                        + "<input type='submit' value='Submit'>"
                        + "</form>"
                        + "<br><pre>"
                        + cities
                        + "</pre>"
                        + "</body>"
                        + "</html>");
            }

        }
        response.setStatusCode(200);
        response.setContentType("text/html; charset=utf8");
        response.addHeader("Accept-Language", "en-US");
        return response;
    }

    /**
     * Class to load the map for the navi.
     *
     * @return Returns a map with the parsed data from the XML-file
     * @throws ParserConfigurationException
     * @throws IOException
     */
    private Map<String, SortedSet<String>> xmlRead() throws ParserConfigurationException, IOException {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser parser = parserFactory.newSAXParser();
            FileReader fileRead = new FileReader(getFile());
            InputSource input = new InputSource(fileRead);
            OSMHandler handler = new OSMHandler();
            parser.parse(input, handler);
            xmlParsed = true;
            return handler.returnData();
        } catch (SAXException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}



