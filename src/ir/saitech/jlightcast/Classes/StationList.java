package ir.saitech.jlightcast.Classes;

import ir.saitech.jlightcast.Utils.Out;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

/**
 * Created by blk-arch on 12/9/16.
 * Global Static List of Running Stations
 */
public class StationList {
    private static ConcurrentHashMap<String,Station> list = new ConcurrentHashMap<>(100);

    public static void add(Station st) throws Exception {
        Out.println("List size is "+list.size());
        if (!list.containsKey(st.getName().toLowerCase())) {// FIXME: 12/13/16
            list.putIfAbsent(st.getName().toLowerCase(), st);
            Out.println("List size is now " + list.size());
        }
        else {
            //Out.println(list.get(String.valueOf(st.getId())).getName());
            throw new Exception("Station already exists !");
        }
        Out.println("Station added");
    }

    public static void remove(Station st) throws Exception {
        if (list.containsKey(st.getName().toLowerCase())){
            list.remove(st.getName().toLowerCase());
        } else throw new Exception("Station does not exist !");
    }

    public static void remove(String name) throws Exception {
        if (list.containsKey(name.toLowerCase())){
            list.remove(name.toLowerCase());
        } else throw new Exception("Station does not exist !");
    }

    public static Station findByName(String name){
        if (list.containsKey(name.toLowerCase())){
            return list.get(name.toLowerCase());
        } else return null;
    }

    public static Station findById(int id){
        for (Station s: list.values()) {
            if ( s.getId() == id ){
                return s;
            }
        }
        return null;
    }

    public static boolean exists(String name){
        return list.containsKey(name.toLowerCase());
    }
}
