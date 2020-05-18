//next steps: DB and connection (mariaDB)

package Implementations.plugins;

import Interfaces.Plugin;
import Implementations.ResponseImpl;
import Interfaces.Request;
import Interfaces.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.*;
import java.math.BigDecimal;
import java.util.*;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public class Temperature implements Plugin {

    private ArrayList<Messwert> myData;
    
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement prep = null;
    private ResultSet result = null;

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
        return (req.getUrl().getPath().startsWith("/temperature")) ? 1.0f : 0.0f;
    };

    /**
     * Called by the server when the plugin should handle the request.
     *
     * @param req
     * @return A new response object.
     */
    @Override
    public Response handle(Request req) throws IOException, SQLException, ClassNotFoundException {
        ResponseImpl response = new ResponseImpl();

        generateData();


        if (req.getUrl().getRawUrl().matches("/temperature/\\d\\d\\d\\d/\\d\\d/\\d\\d")) {
            String time =
                    req.getUrl().getRawUrl().split("[/|?]")[2] + "-" +
                            req.getUrl().getRawUrl().split("[/|?]")[3] + "-" +
                            req.getUrl().getRawUrl().split("[/|?]")[4] + " 00:00:00";

            Timestamp ts = Timestamp.valueOf(time);
            response.setContent(getDatabasaDataXML(ts));
        } else if (req.getUrl().getRawUrl().startsWith("/temperature/search")) {
            String date = req.getUrl().getParameter().get("date");
            System.out.println("Suche nach: " + date);

            Timestamp ts = Timestamp.valueOf(date + " 00:00:00");

            response.setContent("<html><body>" +
                    "<h1>Temperatures:</h1>" +
                    "<form action=\"/temperature/search\">\n" +
                    "  <input type=\"date\" name=\"date\" value=" + date + ">\n" +
                    "  <input type=\"submit\" value=\"Search\">\n" +
                    "</form> " +
                    "<form action=\"/temperature\">\n" +
                    "  <input type=\"submit\" value=\"Overview\">\n" +
                    "</form> " +
                    "" +
                    "" +
                    "<br>" + getDatabaseDataByDate(ts) + "</body></html>");
        } else {
            int offset = 0;
            int limit = 10;

            String param = null;

            try {
                param = req.getUrl().getParameter().get("offset");
                if (param != null) {
                    offset = Integer.parseInt(req.getUrl().getParameter().get("offset"));
                    if (offset < 0) offset = 0;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                param = req.getUrl().getParameter().get("limit");
                if (param != null) {
                    limit = Integer.parseInt(req.getUrl().getParameter().get("limit"));
                    if (limit < 0) limit = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("RAWURL: " + req.getUrl().getRawUrl());

            response.setContent("<html><body>" +
                    "<h1>Temperatures:</h1>" +
                    "<form action=\"/temperature/search\">\n" +
                    "  Search date:<br>\n" +
                    "  <input type=\"date\" name=\"date\" value=\"2019-12-05\">\n" +
                    "  <input type=\"submit\" value=\"Submit\">\n" +
                    "</form> " +
                    "" +
                    "" +
                    "<br>" + getDatabaseData(limit, offset) + "</body></html>");
        }

        return response;
    };

    private Connection connectToDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/temperatures", "client", "2020DB4SWE1!");
    }

    private String getDatabaseDataByDate(Timestamp date) {
        StringBuilder res = new StringBuilder(
                "<table border='1'><tr><td><a>Date&nbsp;&nbsp;</a></td><td><a>Temperature&nbsp;&nbsp;</a></td></tr>");

        try {
            Connection conn = connectToDB();
            PreparedStatement query = conn.prepareStatement("SELECT * FROM entries WHERE date = ? ORDER BY date DESC;");
            query.setTimestamp(1, date);

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                res.append("<tr><td><a>");
                res.append(resultSet.getString("date"));
                res.append("</a></td><td><a>");
                res.append(resultSet.getString("temperature"));
                res.append("</a></td></tr>");
            }
            res.append("</table>");

            resultSet.close();
            query.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    private String getDatabaseData(int limit, int offset) {
        StringBuilder res = new StringBuilder(
                "<table border='1'><tr><td><a>Date&nbsp;&nbsp;</a></td><td><a>Temperature&nbsp;&nbsp;</a></td></tr>");

        try {
            Connection conn = connectToDB();
            PreparedStatement query = conn.prepareStatement("select * from entries order by date desc limit ? offset ?");
            query.setInt(1, limit);
            query.setInt(2, offset);
            ResultSet resultSet = query.executeQuery();

            int i = 0;
            while (resultSet.next()) {
                if (i >= limit) break;
                res.append("<tr><td><a>");
                res.append(resultSet.getString("date"));
                res.append("</a></td><td><a>");
                res.append(resultSet.getString("temperature"));
                res.append("</a></td></tr>");
                i++;
            }

            res.append("</table>");

            if (offset == 0) {
                res.append("<a href='?offset=");
                res.append(offset + limit);
                res.append("'>next</a>");
            } else if (offset > 0) {
                res.append("<a href='?offset=");
                res.append(offset - limit);
                res.append("'>prev</a>&nbsp;&nbsp;");
                res.append("<a href='?offset=");
                res.append(offset + limit);
                res.append("'>next</a>");
            }

            resultSet.close();
            query.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res.toString();
    }

    private String getDatabasaDataXML(Timestamp time) {
        StringBuilder res = new StringBuilder("<?xml version='1.0'?><temperatures>");

        try {
            Connection conn = connectToDB();

            PreparedStatement query = conn.prepareStatement("SELECT * FROM entries WHERE date = ? ORDER BY date DESC;");
            query.setTimestamp(1, time);

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                res.append("<entry><daytemperature>");
                res.append(resultSet.getString("temperature"));
                res.append("</daytemperature><date>");
                res.append(resultSet.getString("date"));
                res.append("</date></entry>");
            }
            res.append("</temperatures>");

            resultSet.close();
            query.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    private void fillDatabase() throws SQLException, ClassNotFoundException {
        Random r = new Random();

        double min = 0.00, max = 40.00, randDouble;
        int dayOfYear;
        BigDecimal bd;

        Connection connection = connectToDB();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM entries");
        resultSet.next();
        int rows = resultSet.getInt("count(*)");

        if (rows < 10000) {
            for (int i = 0; i <= 11000; i++) {

                GregorianCalendar gc = new GregorianCalendar();

                int year = ThreadLocalRandom.current().nextInt(1900, 2019 + 1);

                gc.set(Calendar.YEAR, year);

                dayOfYear = ThreadLocalRandom.current().nextInt(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR) + 1);
                bd = new BigDecimal(min + r.nextFloat() * (max - min)).setScale(2, RoundingMode.HALF_UP);
                randDouble = bd.doubleValue();

                gc.set(Calendar.DAY_OF_YEAR, dayOfYear);

                String randDate = gc.get(Calendar.YEAR) + "/" + (gc.get(Calendar.MONTH) + 1) + "/" + gc.get(Calendar.DAY_OF_MONTH);

                statement.executeUpdate("INSERT INTO entries (date, temperature) VALUES ('" + randDate + "'," + randDouble + ")");

            }
        }
        connection.close();
    }
    
    private void generateData(){
        myData = new ArrayList<>();
        int year;
        int month;
        int day;
        int[] monthLength = {31,28,31,30,31,30,31,31,30,31,30,31};
        
        for(year = 2010; year < 2020; year++){
            for(month = 1; month < 13; month++){
                for(day = 1; day <= monthLength[month-1]; day++){
                    myData.add(new Messwert(year, month, day, generateValue()));
                    myData.add(new Messwert(year, month, day, generateValue()));
                    myData.add(new Messwert(year, month, day, generateValue()));
                }
            }
        }
    }
    private Double generateValue(){
        long value = (long) (Math.random()*100);
        value = Math.round(value);
        return ((double) value)/10;
    }
    
}

class Messwert {
    int year, month, day;
    double value;
    public Messwert(int year, int month, int day, double value){
        this.year = year;
        this.month = month;
        this.day = day;
        this.value = value;
    }
    public int getYear(){
        return this.year;
    }
    public int getMonth(){
        return this.month;
    }
    public int getDay(){
        return this.day;
    }
    public String getDate(){
        return String.format("%d-%d-%d", this.year, this.month, this.day);
    }
    public double getValue(){
        return this.value;
    }
}