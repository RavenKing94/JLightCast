package ir.saitech.jlightcast.Utils;

/**
 * Created by blk-arch on 9/15/16.
 * Http Response Header
 */
public class DefaultHTTPHeader {

    private final static String dhead_bfile = "HTTP/1.1 200 OK\n" +
            "Pragma: public\r\n" +
            "Expires: 0\r\n" +
            "Content-Type: application/force-download\r\n" +
            "Content-Disposition: attachment; filename=\"";
    private final static String dhead_afile = "\";\r\n" +
            "Content-Transfer-Encoding: binary\r\n" +
            "Content-Length: ";

    private final static String shead = "HTTP/1.1 200 OK\r\n" +
            "Pragma: public\r\n" +
            "Expires: 0\r\n" +
            "Content-Type: audio/mpeg\r\n" +
            "Content-Length: -1\r\n\r\n";

    public static String downloadHeader(String filename,String length){
        StringBuilder sb = new StringBuilder();
        sb.append(dhead_bfile).append(filename).append(dhead_afile).append(length).append("\r\n\r\n");
        return sb.toString();
    }
    public static String streamHeader(){
        return shead;
    }
}
