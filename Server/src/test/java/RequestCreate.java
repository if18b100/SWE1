import java.io.*;
import java.nio.charset.StandardCharsets;

public class RequestCreate {

    public static InputStream getInvalidRequest(String url, String http) throws IOException{
        return getRequest(url, http, "/", null, null);
    }

    public static InputStream getValidRequest(String url) throws IOException {
        return getRequest(url, "POST", "/", null, null);
    }

    public static InputStream getValidRequest(String url, String http, String body) throws IOException {
        return getRequest(url, http, "/", null, body);
    }

    public static InputStream getRequest(String url, String http, String host, String[][] header, String body) throws IOException {
        byte[] bytes = null;

        //Message
        String[] partOne = {
                http + " " + url + " HTTP/1.1\n",
                "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)",
                "Host: " + host + "\n",
                "Accept-Language: en-us",
                "Accept-Encoding: gzip, deflate",
                "Connection: keep-alive\n"
        };

        if(body != null){
            bytes = body.getBytes(StandardCharsets.UTF_8);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        //print partOne
        for(String each: partOne){
            writer.print(each);
        }

        if(bytes != null){
            writer.printf("Content-Length: %d\n", bytes.length);
        }

        //Print Header
        if(header != null){
            for (String[] each : header){
                writer.printf("%s: %s\n", each[0], each[1]);
            }
        }

        //Print Body
        writer.println();
        if(bytes != null){
            writer.flush();
            outputStream.write(bytes);
        }
        writer.flush();
        return new ByteArrayInputStream(outputStream.toByteArray());

    }
}
