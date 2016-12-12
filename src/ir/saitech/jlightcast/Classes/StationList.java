package ir.saitech.jlightcast.Classes;

import java.util.HashMap;

/**
 * Created by blk-arch on 12/9/16.
 * Global Static List of Running Stations
 */
public class StationList {
    private static HashMap<String,Station> list = new HashMap<>(100);

    public static void add(Station st) throws Exception {
        if (!list.containsKey(st.getName()))
            list.put(st.getName(),st);
        else throw new Exception("Station already exists !");
    }

    public static void remove(Station st) throws Exception {
        if (list.containsKey(st.getName())){
            list.remove(st.getName());
        } else throw new Exception("Station does not exist !");
    }

    public static void remove(String name) throws Exception {
        if (list.containsKey(name)){
            list.remove(name);
        } else throw new Exception("Station does not exist !");
    }

    public static Station findByName(String name){
        if (list.containsKey(name)){
            return list.get(name);
        } else return null;
    }

    public static Station findById(int id){

        for (Station s: list.values()) {
            if (s.getId() == id){
                return s;
            }
        }
        return null;
    }

    public static boolean exists(String name){
        return list.containsKey(name);
    }
}
