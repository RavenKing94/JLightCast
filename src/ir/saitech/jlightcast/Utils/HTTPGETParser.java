package ir.saitech.jlightcast.Utils;

/*
 * Created by Kasra_Sh on 10/12/16.
 * A simple independent HTTP-GET parser for extracting paths .
 * Mostly needed when writing servers from scratch .
 * example-header : GET /user/settings HTTP/1.1
 *                  ...
 *                  ...
 * output : a List<String> containing {"user", "settings"}
 */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses HTTP-GET headers and extracts requested path .
 */
public class HTTPGETParser {

    private String rawHeader;
    private final Pattern p;

    private String PATTERN = "(GET)|((?<=\\/)[\\w\\d_]+)|((?<=\\?)[\\w\\d_]*[\\=][\\w\\d]*)" +
            "|(HTTP\\/1\\.1[\\n\\r])|(HTTP\\/1\\.0[\\n\\r])";
    public HTTPGETParser(){
        rawHeader = "GET / HTTP/1.1"; //default header if none given
        p = Pattern.compile(PATTERN);
    }

    public HTTPGETParser(String head){
        rawHeader = head;
        p = Pattern.compile(PATTERN);
    }

    public HTTPGETParser(byte[] head){
        rawHeader = new String(head);
        p = Pattern.compile(PATTERN);
    }

    public void setRawHeader(String head){
        rawHeader = head;
    }

    public void setRawHeader(byte[] head){
        rawHeader = new String(head);
    }

    public List<String> parseGET(String head){
        setRawHeader(head);
        return parseGET();
    }

    public List<String> parseGET()
    {
        //System.out.println(" - - - - - - - - ");
        boolean state=false;
        List<String> sl = new ArrayList<String>();
        Matcher m =p.matcher(rawHeader);
        while (m.find()) {
            if (m.group().contains("HTTP/1.")){
                break;
            }
            if (state) {
                sl.add(m.group());
                continue;
            }
            if (m.group().equals("GET")){
                state = true;
            }
        }
        return sl;
    }
}
