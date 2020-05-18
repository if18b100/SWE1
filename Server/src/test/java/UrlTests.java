import Implementations.UrlImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.HashMap;

public class UrlTests {
    @DisplayName("Test Implementation Url")


    // Tests for getUrl

    @Test
    void getRawUrlEmpty(){
        UrlImpl url = new UrlImpl("");
        assertEquals("", url.getRawUrl());
    }

    @Test
    void getRawUrlNull(){
        UrlImpl url = new UrlImpl(null);
        assertNull(url.getRawUrl());
    }

    @Test
    void getRawUrl01(){
        UrlImpl url = new UrlImpl("http://www.testurl.at");
        assertEquals("http://www.testurl.at", url.getRawUrl());
    }

    @Test
    void getRawUrl02(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html?p1=A&p2=B#anchor");
        assertEquals("https://www.testurl.at:8080/index.html?p1=A&p2=B#anchor", url.getRawUrl());
    }

    @Test
    void getRawUrl03(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at/duales-studium-bachelor-studiengang-informatik/");
        assertEquals("https://www.technikum-wien.at/duales-studium-bachelor-studiengang-informatik/", url.getRawUrl());
    }

    //Tests for getPath

    @Test
    void getPathNull(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at");
        assertEquals("", url.getPath());
    }

    @Test
    void getPathEmpty(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at/");
        assertEquals("", url.getPath());
    }

    @Test
    void getPath01(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at/duales-studium-bachelor-studiengang-informatik/");
        assertEquals("/duales-studium-bachelor-studiengang-informatik/", url.getPath());
    }

    @Test
    void getPath02(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html?p1=A&p2=B#anchor");
        assertEquals("/index.html", url.getPath());
    }

    @Test
    void getPath03(){
        UrlImpl url = new UrlImpl("https://cis.technikum-wien.at/cis/index.php");
        assertEquals("/cis/index.php", url.getPath());
    }

    //tests for getParameter

    @Test
    void getParameterNull(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at");
        Map<String, String> output = url.getParameter();
        assertNotNull(output);
        assertEquals(0, output.size());
    }

    @Test
    void getParameterEmpty(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at/duales-studium-bachelor-studiengang-informatik/");
        Map<String, String> output = url.getParameter();
        assertNotNull(output);
        assertEquals(0, output.size());
    }

    @Test
    void getParameter01(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html?a=A");
        Map<String, String> output = url.getParameter();
        assertEquals(1, output.size());
        String myString = output.toString();
        assertEquals("A", output.get("a"));
    }

    @Test
    void getParameter02(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html?p1=A&p2=B");
        Map<String, String> output = url.getParameter();
        assertEquals(output.size(), 2);
        assertEquals("A", output.get("p1"));
        assertEquals("B", output.get("p2"));
    }

    @Test
    void getParameter03(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html?a=A&b=B#anchor");
        Map<String, String> output = url.getParameter();
        assertEquals(output.size(), 2);
        assertEquals("A", output.get("a"));
        assertEquals("B", output.get("b"));
    }

    //tests for getParameterCount

    @Test
    void getParameterCountNull(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at");
        assertEquals(0, url.getParameterCount());
    }

    @Test
    void getParameterCountEmpty(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html");
        assertEquals(0, url.getParameterCount());
    }

    @Test
    void getParameterCount01(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html?p1=A#anchor");
        assertEquals(1, url.getParameterCount());
    }

    @Test
    void getParameterCount02(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html?p1=A&p2=B#anchor");
        assertEquals(2, url.getParameterCount());
    }

    @Test
    void getParameterCount03(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html?p1=A&p2=B#anchor");
        assertEquals(2, url.getParameterCount());
    }

    @Test
    void getParameterCount04(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html#anchor");
        assertEquals(0, url.getParameterCount());
    }

    //tests for getSegment

    @Test
    void getSegmentEmpty01(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at");
        String[] output = url.getSegments();
        assertNotNull(output);
        String[] expected = {};
        assertArrayEquals(expected, output);
    }

    @Test
    void getSegmentEmpty02(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at/");
        String[] output = url.getSegments();
        assertNotNull(output);
        String[] expected = {};
        assertArrayEquals(expected, output);
    }

    @Test
    void getSegment01(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html");
        String[] output = url.getSegments();
        String[] expected = {"index.html"};
        assertArrayEquals(expected, output);
    }

    @Test
    void getSegment02(){
        UrlImpl url = new UrlImpl("https://www.testurl.at/de/shop/angebote");
        String[] output = url.getSegments();
        String[] expected = {"de", "shop", "angebote"};
        assertArrayEquals(expected, output);
    }

    @Test
    void getSegment03(){
        UrlImpl url = new UrlImpl("https://www.testurl.at/de/shop/angebote/index.html");
        String[] output = url.getSegments();
        String[] expected = {"de", "shop", "angebote", "index.html"};
        assertArrayEquals(expected, output);
    }

    @Test
    void getSegment04(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at/duales-studium-bachelor-studiengang-informatik/");
        String[] output = url.getSegments();
        String[] expected = {"duales-studium-bachelor-studiengang-informatik"};
        assertArrayEquals(expected, output);
    }
    //tests for getFilename

    @Test
    void getFileNameNone(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at");
        String output = url.getFileName();
        assertEquals("", output);
    }

    @Test
    void getFileNameEmpty01(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at/duales-studium-bachelor-studiengang-informatik/");
        String output = url.getFileName();
        assertEquals("", output);
    }

    @Test
    void getFileNameEmpty02(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at/duales-studium-bachelor-studiengang-informatik?p1=4");
        String output = url.getFileName();
        assertEquals("", output);
    }

    @Test
    void getFileName01(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html");
        String output = url.getFileName();
        assertEquals( "index.html", output);
    }

    @Test
    void getFileName02(){
        UrlImpl url = new UrlImpl("https://cis.technikum-wien.at/cis/index.php");
        String output = url.getFileName();
        assertEquals("index.php", output);
    }



    //tests for getExtension

    @Test
    void getExtensionNone(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at");
        String output = url.getExtension();
        assertEquals("", output);
    }

    @Test
    void getExtensionEmpty(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at/duales-studium-bachelor-studiengang-informatik/");
        String output = url.getExtension();
        assertEquals("", output);
    }

    @Test
    void getExtension01(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html");
        String output = url.getExtension();
        assertEquals(".html", output);
    }

    @Test
    void getExtension02(){
        UrlImpl url = new UrlImpl("https://cis.technikum-wien.at/cis/index.php");
        String output = url.getExtension();
        assertEquals(".php", output);
    }

    @Test
    void getExtension03(){
        UrlImpl url = new UrlImpl("https://cis.technikum-wien.at/cis/index.php?p1=A");
        String output = url.getExtension();
        assertEquals(".php", output);
    }

    //tests for getFragment

    @Test
    void getFragmentEmpty01(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at");
        String output = url.getFragment();
        assertEquals("", output);
    }

    @Test
    void getFragmentEmpty02(){
        UrlImpl url = new UrlImpl("https://cis.technikum-wien.at/cis/index.php?p1=A");
        String output = url.getFragment();
        assertEquals("", output);
    }

    @Test
    void getFragment01(){
        UrlImpl url = new UrlImpl("https://www.testurl.at:8080/index.html#test");
        String output = url.getFragment();
        assertEquals("test", output);
    }

    @Test
    void getFragment02(){
        UrlImpl url = new UrlImpl("https://www.technikum-wien.at#anchor");
        String output = url.getFragment();
        assertEquals("anchor", output);
    }

    @Test
    void getFragment03(){
        UrlImpl url = new UrlImpl("https://cis.technikum-wien.at/cis/index.php?p1=A#fragment");
        String output = url.getFragment();
        assertEquals("fragment", output);
    }
}