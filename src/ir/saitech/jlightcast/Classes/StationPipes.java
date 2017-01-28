package ir.saitech.jlightcast.Classes;

import ir.saitech.jlightcast.Utils.Out;

import java.io.PipedReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by blk-arch on 12/10/16.
 *
 */
public class StationPipes {
    private static PipeInfo pipeInfo = new PipeInfo(0, Station.StreamBitrate.Q128);

    private static ConcurrentHashMap<PipeInfo,PipedReader> stp = new ConcurrentHashMap<>(500);

    private static Lock lock = new ReentrantLock(true);

    public static void add(PipeInfo pi, PipedReader pr) {
        lock.tryLock();
        stp.put(pi,pr);
        lock.unlock();
    }

    public static void remove(PipeInfo pi) {
        lock.tryLock();
        stp.remove(pi);
        lock.unlock();
    }

    public static PipedReader get(PipeInfo pi) {
        lock.tryLock();
        if (!stp.containsKey(pi)) Out.println("key does not exist !");
        PipedReader pr = stp.get(pi);
        lock.unlock();
        return pr;
    }

    public static PipedReader get(int sId, Station.StreamBitrate sbr) {
        lock.tryLock();
        pipeInfo.setStationId(sId);
        pipeInfo.setBitrate(sbr);
        PipedReader pr = stp.get(pipeInfo);
        lock.unlock();
        return pr;
    }

    public static int count(){
        return stp.size();
    }

    public static PipeInfo[] getKeys() {
        PipeInfo[] pi;
        Object[] obj;
        obj = stp.keySet().toArray();
        pi = new PipeInfo[obj.length];
        for (int i = 0; i < obj.length; i++) {
            pi[i] =(PipeInfo)obj[i];
        }
        return pi;
    }

    public static PipedReader[] getValues() {

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
