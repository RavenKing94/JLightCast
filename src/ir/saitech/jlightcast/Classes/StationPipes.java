package ir.saitech.jlightcast.Classes;

import ir.saitech.jlightcast.Utils.Out;

import java.io.PipedReader;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by blk-arch on 12/10/16.
 *
 */
public class StationPipes {
    private static PipeInfo pipeInfo = new PipeInfo(0, Station.StreamBitrate.Q128);

    private static ConcurrentHashMap<PipeInfo,PipedReader> stp = new ConcurrentHashMap<>(500);

    public static synchronized void add(PipeInfo pi, PipedReader pr){
        stp.put(pi,pr);
    }

    public static synchronized void remove(PipeInfo pi){
        stp.remove(pi);
    }

    public static PipedReader get(PipeInfo pi){
        if (!stp.containsKey(pi)) Out.println("key does not exist !");
        return stp.get(pi);
    }

    public static synchronized PipedReader get(int sId, Station.StreamBitrate sbr){
        pipeInfo.setStationId(sId);
        pipeInfo.setBitrate(sbr);
        return stp.get(pipeInfo);
    }

    public static synchronized int count(){
        return stp.size();
    }

    public static synchronized PipeInfo[] getKeys() {
        PipeInfo[] pi;
        Object[] obj;
        obj = stp.keySet().toArray();
        pi = new PipeInfo[obj.length];
        for (int i = 0; i < obj.length; i++) {
            pi[i] =(PipeInfo)obj[i];
        }
        return pi;
    }

    public static synchronized PipedReader[] getValues() {
        PipedReader[] pr;
        Object[] obj;
        obj = stp.values().toArray();
        pr = new PipedReader[obj.length];
        for (int i = 0; i < obj.length; i++) {
            pr[i] =(PipedReader) obj[i];
        }
        return pr;
    }
}
