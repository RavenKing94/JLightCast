package ir.saitech.jlightcast.Classes;

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
        lock.tryLock();
        queue.add(cls);
        lock.unlock();
    }

    public static ClientSocket get(){
        lock.tryLock();
        ClientSocket c;
        c = queue.poll();
        lock.unlock();
        return c;
    }

    public static ClientSocket[] getAll(){
        lock.tryLock();
        ClientSocket[] cl;
        Object[] ol;
        ol = queue.toArray();
        queue.clear();
        cl = new ClientSocket[ol.length];
        for (int i=0;i<ol.length;i++){
            cl[i] = (ClientSocket)ol[i];
        }
        lock.unlock();
        return cl;
    }

    public static boolean isEmpty(){
        lock.tryLock();
        boolean empty= true;
        empty = queue.isEmpty();
        lock.unlock();
        return empty;
    }
}
