package ir.saitech.jlightcast.Classes;

import java.io.PipedReader;
import java.util.HashMap;

/**
 * Created by blk-arch on 12/10/16.
 *
 */
public class StationPipes {
    private static HashMap<PipeInfo,PipedReader> stp = new HashMap<>();

    public static synchronized void add(PipeInfo pi, PipedReader pr){
        stp.put(pi,pr);
    }

    public static synchronized void remove(PipeInfo pi){
        stp.remove(pi);
    }

    public static synchronized PipedReader get(PipeInfo pi){
        return stp.get(pi);
    }

    public static synchronized int count(){
        return stp.size();
    }

    public static synchronized PipeInfo[] getKeys() {
        return (PipeInfo[]) stp.keySet().toArray();
    }

    public static synchronized PipeInfo[] getValues() {
        return (PipeInfo[]) stp.keySet().toArray();
    }
}
