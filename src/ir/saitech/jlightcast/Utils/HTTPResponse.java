package ir.saitech.jlightcast.Utils;

import com.sun.jndi.ldap.ext.StartTlsResponseImpl;
import ir.saitech.jlightcast.Classes.Station;

/**
 * Created by blk-arch on 10/12/16.
 */
public class HTTPResponse {
    private static final String HEAD_OK = "HTTP/1.1 200 OK\r\n";
    private static final String HEAD_FAIL = "HTTP/1.1 404 Not Found\r\n";
    private static final String NOCACHE = "Cache-Control: no-cache, no-store\r\n" +
                                          "Pragma: no-cache\r\n";
    private static final String SERVER = "Server: JLightCast (beta)\r\n";
    private static final String CONNECTION_CLOSE = "Connection: Close\r\n";
    private static final String CONNECTION_ALIVE = "Connection: keep-alive\r\n";
    private static final String CONTENT_LENGTH = "Content-Length: -1\r\n";
    private static final String CONTENT_TYPE = "Content-Type: audio/mpeg\r\n";
    private static final String NAME = "icy-name: JLightCast\r\n";
    private static final String END = "\r\n";


    public static String getNewOk(){
        StringBuilder sb = new StringBuilder();
        sb.append(HEAD_OK)
                .append(SERVER)
                .append(CONNECTION_ALIVE)
                .append(CONTENT_TYPE)
                .append(CONTENT_LENGTH)
                .append(NOCACHE)
                .append(END);
        return sb.toString();
    }
    public static String getNewFail(){
        StringBuilder sb = new StringBuilder();
        sb.append(HEAD_FAIL)
                .append(SERVER)
                .append(END);
        return sb.toString();
    }
}
