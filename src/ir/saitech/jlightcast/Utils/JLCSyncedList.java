package ir.saitech.jlightcast.Utils;

import javax.naming.SizeLimitExceededException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JLCSyncedList {

    private Object[] list;
    private Object[] olist;
    private Lock lock = new ReentrantLock(true);
    private int len = 0;
    private int maxSize = 0;

    public JLCSyncedList(int size)
    {
        maxSize = size;
        list = new Object[size];
        olist = new Object[size];
    }

    private void orderlist(){
        lock.lock();
        int cnt = 0;
        for (int i = 0;i<len;i++){
            if (list[i]!=null){
                list[cnt] = list[i];
                cnt++;
            }
        }
        len = cnt;
        lock.unlock();
    }

    public void add(Object object) throws SizeLimitExceededException{
        lock.lock();
        if (len>=maxSize) {
            throw new SizeLimitExceededException();
        }
        list[len] = object;
        len++;
        lock.unlock();
    }

    public Object get(int index){
        lock.lock();
        if (index>=0 && index<len) {
            lock.unlock();
            return list[index];
        }else {
            lock.unlock();
            throw new IndexOutOfBoundsException();
        }

    }

    public void remove(int index){
        lock.lock();
        if (index>=0 && index<len) {
            list[index]=null;
            orderlist();
        }else throw new IndexOutOfBoundsException();
        lock.unlock();
    }

    public int size(){
        return len;
    }
}
