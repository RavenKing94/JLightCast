package ir.saitech.jlightcast.Utils;

import javax.naming.SizeLimitExceededException;

public class JLCSyncedList {

    private Object[] list;
    private Object[] olist;
    private boolean locked = false;
    private int len = 0;
    private int maxSize = 0;

    public JLCSyncedList(int size)
    {
        maxSize = size;
        list = new Object[size];
        olist = new Object[size];
    }

    private void orderlist(){
        locked = true;
        int cnt = 0;
        for (int i = 0;i<len;i++){
            if (list[i]!=null){
                list[cnt] = list[i];
                cnt++;
            }
        }
        len = cnt;
        locked = false;
    }

    public void add(Object object) throws SizeLimitExceededException{
        while (locked){
            try {
                Thread.sleep(0,10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        locked = true;
        if (len>=maxSize) {
            throw new SizeLimitExceededException();
        }
        list[len] = object;
        len++;
        locked = false;
    }

    public Object get(int index){
        while (locked){
            try {
                Thread.sleep(0,100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (index>=0 && index<len) {
            return list[index];
        }else throw new IndexOutOfBoundsException();
    }

    public void remove(int index){
        while (locked){
            try {
                Thread.sleep(0,10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        locked = true;
        if (index>=0 && index<len) {
            list[index]=null;
            orderlist();
        }else throw new IndexOutOfBoundsException();
        locked = false;
    }

    public int size(){
        while (locked){
            try {
                Thread.sleep(0,10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return len;
    }
}
