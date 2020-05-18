//Original

package Implementations.plugins;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class OSMHandler extends DefaultHandler {
    private Map<String, SortedSet<String>> data = new HashMap<String, SortedSet<String>>();
    private Map<String, String> map = new HashMap<String, String>();
    private SortedSet<String> sortedStreetSet = null;
    private boolean node = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes){
        if("node".equals(qName)) node = true;
        else if(("tag".equals(qName)) && (node)){
            map.put(attributes.getValue("k"), attributes.getValue("v"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName){
        if("node".equals(qName)){
            if(map.containsKey("addr:city") && map.containsKey("addr:street")){
                if(data.containsKey(map.get("addr:street").toLowerCase())){
                    if(!data.get(map.get("addr:street").toLowerCase()).contains(map.get("addr:city"))){
                        data.get(map.get("addr:street").toLowerCase()).add(map.get("addr:city"));
//                        System.out.println(map.get("addr:city"));
                    }
                }
                else{
                    sortedStreetSet = new TreeSet<String>();
                    sortedStreetSet.add(map.get("addr:city"));
                    data.put(map.get("addr:street").toLowerCase(), sortedStreetSet);
                }
            }
            node = false;
            map.clear();
        }
    }

    Map<String, SortedSet<String>> returnData(){
        return data;
    }
}