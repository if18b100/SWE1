package Implementations;

import Interfaces.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


public class ResponseImpl implements Response {
    private byte[] content;
    private String header;
    private String contentType;
    private int statusCode;
    private String status;
    private int contentLength;
    private Map<String, String> headers = new HashMap<>();

    public ResponseImpl(){
        headers.put("Server", "IoT-Server");
    }

    /**
     * @return Returns a writable map of the response headers. Never returns
     *         null.
     */
    @Override
    public Map<String, String> getHeaders(){
        return headers;
    };

    /**
     * @return Returns the content length or 0 if no content is set yet.
     */
    @Override
    public int getContentLength(){
        return contentLength;
    };

    /**
     * @return Gets the content type of the response.
     */
    @Override
    public String getContentType(){
        return contentType;
    };

    /**
     * @param contentType
     *            Sets the content type of the response.
     * @throws IllegalStateException
     *             A specialized implementation may throw a
     *             InvalidOperationException when the content type is set by the
     *             implementation.
     */
    @Override
    public void setContentType(String contentType){
        headers.put("Content-Type", contentType);
        this.contentType = contentType;
    };

    /**
     * @return Gets the current status code. An Exceptions is thrown, if no status code was set.
     */
    @Override
    public int getStatusCode(){
        if(statusCode == 0){
            throw new IllegalStateException("null");
        } else {
            return statusCode;
        }
    };

    /**
     * @param status
     *            Sets the current status code.
     */
    @Override
    public void setStatusCode(int status){
        switch(status){
            case 100:
                this.status = "100 Continue";
                break;
            case 200:
                this.status = "200 OK";
                break;
            case 204:
                this.status = "204 No Content";
                break;
            case 404:
                this.status = "404 Not Found";
                break;
            case 418:
                this.status = "418 I'm a teapot";
                break;
            case 500:
                this.status = "500 Internal Server Error";
                break;
            default:
                this.status = "400 Bad Request";
                break;
        }
        this.statusCode = status;
    };

    /**
     * @return Returns the status code as string. (200 OK)
     */
    @Override
    public String getStatus(){
        if(statusCode == 0){
            throw new IllegalStateException("null");
        } else {
            return String.format("(%s)", this.status);
        }
    };

    /**
     * Adds or replaces a response header in the headers map
     *
     * @param header Identifier des Headers
     * @param value Value des Headers
     */
    @Override
    public void addHeader(String header, String value){
        this.headers.put(header, value);
    };

    /**
     * @return Returns the Server response header. Defaults to "BIF-SWE1-Server".
     */
    @Override
    public String getServerHeader(){
        return this.header;
    };

    /**
     * Sets the Server response header.
     * @param server Bezeichnung des Servers
     */
    @Override
    public void setServerHeader(String server){
        this.headers.put("Server", server);
    };

    /**
     * @param content
     *            Sets a string content. The content will be encoded in UTF-8.
     */
    @Override
    public void setContent(String content){
        this.content = content.getBytes();
        this.contentLength = this.content.length;
    };

    /**
     * @param content
     *            Sets a byte[] as content.
     */
    @Override
    public void setContent(byte[] content){
        this.content = content;
        this.contentLength = this.content.length;
    };

    /**
     * @param stream
     *            Sets the stream as content.
     */
    @Override
    public void setContent(InputStream stream){
        try {
            this.content = stream.readAllBytes();
            this.contentLength = this.content.length;
        } catch (IOException exception){
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    };

    /**
     * @param network
     *            Sends the response to the network stream.
     */
    @Override
    public void send(OutputStream network){
        try{
            if(statusCode == 0 || status == null){
                throw new IllegalStateException("Not a valid response");
            }

            //Construct response
            StringBuilder responseHeader = new StringBuilder();
            responseHeader.append(String.format("HTTP/1.1 %s\n", this.getStatus()));
            responseHeader.append(String.format("Content-Length: %d\n", this.getContentLength()));

            for(Map.Entry<String, String> buffer: headers.entrySet()){
                responseHeader.append(
                        String.format("%s: %s\n", buffer.getKey(), buffer.getValue())
                );
            }
            responseHeader.append("\n");

            //Send Response
            network.write(responseHeader.toString().getBytes());
            network.write(content);
            network.flush();

        } catch (IOException exception){
            exception.getMessage();
            exception.printStackTrace();
        }
    };
}
