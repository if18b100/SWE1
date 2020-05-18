package Implementations;

import Interfaces.Url;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;



public class UrlImpl implements Url {

    private String url;

    /** Constructor
     * @param url URL as String
     */
    public UrlImpl (String url){
        this.url = url;
    }

    /** Returns the whole URL
     * @return Returns the raw url.
     */
    @Override
    public String getRawUrl(){
        return this.url;
    };

    /**
     * @return Returns the path of the url, without parameter.
     */
    @Override
    public String getPath(){
        String result;
        String splitProtocol;

        if(this.url.contains("//")){
            splitProtocol = this.url.split("//")[1];
        } else {
            splitProtocol = this.url;
        }

        if(!splitProtocol.contains("/")){
            return "";
        } else if (splitProtocol.indexOf("/") == splitProtocol.length() - 1){
            return  "";
        }

        String splitPath = splitProtocol.substring(splitProtocol.indexOf('/'), splitProtocol.length());

        if(splitPath.indexOf('?') >= 0){
            result =  splitPath.substring(0, splitPath.indexOf('?'));
        } else if (splitPath.indexOf('#') >= 0){
            result =  splitPath.substring(0, splitPath.indexOf('#'));
        } else {
            result = splitPath;
        }
        return result;
    };

    /**
     * @return Returns a dictionary with the parameter of the url. Never returns
     *         null.
     */
    @Override
    public Map<String, String> getParameter(){
        Map<String, String> myMap = new HashMap<String, String>();

        if(!this.url.contains("?")){
            return myMap;
        } else {
            String part = this.url.substring(this.url.indexOf("?")+1);
            String params;
            if(this.url.contains("#")){
                params = part.substring(0,part.indexOf("#"));
            } else {
                params = part;
            }
            String[] paramArray;
            if(params.contains("&")){
                paramArray = params.split("&");
            } else {
                paramArray = new String[]{params};
            }
            for(String temp: paramArray){
                String[] fragment = temp.split("=");
                myMap.put(fragment[0], fragment[1]);
            }
        }
        return myMap;
    };


    /**
     * @return Returns the number of parameter of the url. Returns 0 if there are no parameter.
     */
    @Override
    public int getParameterCount(){
        return this.getParameter().size();
    };

    /**
     * @return Returns the segments of the url path. A segment is divided by '/'
     *         chars. Never returns null.
     */
    @Override
    public String[] getSegments(){
        String path = this.getPath();
        String[] segments = new String[]{};

        if(path.equals("")){
            return segments;
        } else {
            return path.substring(1).split("/");
        }
    };

    /**
     * @return Returns the filename (with extension) of the url path. If the url
     *         contains no filename, a empty string is returned. Never returns
     *         null. A filename is present in the url, if the last segment
     *         contains a name with at least one dot.
     */
    @Override
    public String getFileName(){
        int length;
        if(this.getSegments().length == 0){
            return "";
        } else {
            length = this.getSegments().length-1;
        }

        String fileName = this.getSegments()[length];
        if(fileName.contains(".")){
            return fileName;
        } else {
            return "";
        }
    };

    /**
     * @return Returns the extension of the url filename, including the leading
     *         dot. If the url contains no filename, a empty string is returned.
     *         Never returns null.
     */
    @Override
    public String getExtension(){
        String fileName = this.getFileName();
        if(fileName.equals("")){
            return "";
        } else {
            return fileName.substring(fileName.indexOf("."));
        }
    };

    /**
     * @return Returns the url fragment. A fragment is the part after a '#' char
     *         at the end of the url. If the url contains no fragment, a empty
     *         string is returned. Never returns null.
     */
    @Override
    public String getFragment(){
        String url = this.getRawUrl();
        if(url.contains("#")){
            return url.substring(url.indexOf("#") + 1);
        } else {
            return "";
        }
    };

}
