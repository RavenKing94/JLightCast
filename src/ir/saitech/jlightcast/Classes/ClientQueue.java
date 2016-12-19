package ir.saitech.jlightcast.Classes;

import sun.awt.Mutex;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by blk-arch on 12/14/16.
 *
 */
public class ClientQueue {
    private static Queue<ClientSocket> queue = new ArrayBlockingQueue<>(20);
    private static Lock lock = new ReentrantLock(true);

    public static void add(ClientSocket cls){
        lock.lock();
        queue.add(cls);
        lock.unlock();
    }

    public static ClientSocket get(){
        lock.lock();
        ClientSocket c;
        c = queue.poll();
        lock.unlock();
        return c;
    }

    public static ClientSocket[] getAll(){
        lock.lock();
        ClientSocket[] cl;
        Object[] ol;
        ol = queue.toArray();
        cl = new ClientSocket[ol.length];
        for (int i=0;i<ol.length;i++){
            cl[i] = (ClientSocket)ol[i];
        }
        lock.unlock();
        return cl;
    }
}
