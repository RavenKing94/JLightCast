package ir.saitech.jlightcast.Utils;

import org.apache.commons.lang3.time.DateUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SyncFailedException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by blk-arch on 12/2/16.
 * Unified output methods
 */
public class Out {
    private static FileOutputStream fos;
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static LocalDateTime now;
    /**
     * verb == 0 : Silent with no output
     * verb == 1 : Only Errors
     * verb == 2 : Errors and Warnings
     * verb == 3 : All Errors , Warnings , Classes
     * verb == 4 : Everything even logs
     */
    private static int verb=4;

    public static void initFileLog(){
        try {
            fos = new FileOutputStream("log.txt",true);
            fos.write("Started @ ".getBytes());
            fos.write(dtf.format(LocalDateTime.now()).getBytes());
            fos.write("\n".getBytes());
        } catch (FileNotFoundException fnfe) {
            Out.elog("Out",fnfe.getStackTrace().toString());
        } catch (IOException ioe) {
            Out.elog("Out",ioe.getStackTrace().toString());
        }
    }
    /**
     * Set verbosity level (0..4)
     * @param v verbosity
     */
    public static void setVerbosity(int v){
        if (1<=v && v<=3) {
            verb = v;
        }
    }
    public static void print(String msg){
        System.out.print(msg);
    }
    public static void println(String msg){
        System.out.println(msg);
    }
    public static void printWarning(String msg){
        if (verb>1)
            System.out.println("(!) "+msg+" .");
    }
    public static void printError(String msg){
        if (verb>0)
            System.out.println("(E) "+msg+" .");
    }
    public static void printInfo(String msg){
        if (verb>2)
            System.out.println("(I) "+msg+" .");
    }
    public static void ilog(String tag,String msg){
        String log = "**[i] " + tag + " : " + msg;
        _log(log);
    }
    public static void elog(String tag,String msg){
        String log = "**[e] " + tag + " : " + msg;
        _log(log);

    }

    private static void _log(String log){
        if (verb == 4) {
            System.out.println(log);
            // real Logger methods should be used here
            try {
                fos.write(dtf.format(LocalDateTime.now()).getBytes());
                fos.write(log.getBytes());
                fos.write("\n".getBytes());
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }
}
