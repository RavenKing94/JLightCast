package ir.saitech.jlightcast.Classes;

/**
 * Created by blk-arch on 12/2/16.
 * Station information for the controller
 */
public class Station {
    private static int count=0;
    private int id=0;
    private String name;
    public enum StreamType {FILE, PLAYLIST, WEB}
    public enum StreamBitrate{Q48, Q96, Q128, Q256, Q320}
    private StreamType sType;
    private StreamBitrate[] sBtr;
    private String inputAddr;

    public Station(String name, StreamType stype, StreamBitrate[] bitrates, String input){
        this.name = name;
        this.sType = stype;
        this.inputAddr = input;
        count++;
        id = count;
    }

    public String getName() {
        return name;
    }

    public StreamType getsType() {
        return sType;
    }

    public String getInputAddr() {
        return inputAddr;
    }

    public int getId() {
        return id;
    }

    public StreamBitrate[] getBitrates() {
        return sBtr;
    }

    public static StreamBitrate sbFromString(String sb){
        if (sb.contains("48k")){
            return StreamBitrate.Q48;
        }
        else if (sb.contains("96k")){
            return StreamBitrate.Q96;
        }
        else if (sb.contains("128k")){
            return StreamBitrate.Q128;
        }
        else if (sb.contains("256k")){
            return StreamBitrate.Q256;
        }
        else if (sb.contains("320k")){
            return StreamBitrate.Q320;
        } else return null;
    }
}
