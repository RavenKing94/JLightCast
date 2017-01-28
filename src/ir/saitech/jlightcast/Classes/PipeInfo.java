package ir.saitech.jlightcast.Classes;

/**
 * Created by blk-arch on 12/11/16.
 */
public class PipeInfo {
    private Integer stationId;
    private Station.StreamBitrate bitrate;
    private String value;


    private void setValue(){
        value = String.valueOf(bitrate)+String.valueOf(stationId);
    }

    public PipeInfo(int stId, Station.StreamBitrate sb){
        stationId = stId;
        bitrate = sb;
        setValue();
    }

    public Station.StreamBitrate getBitrate() {
        return bitrate;
    }

    public void setBitrate(Station.StreamBitrate sb){
        bitrate = sb;
        setValue();
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int sId) {
        stationId = sId;
        setValue();
    }

    @Override
    public int hashCode() {
        int hs1 = this.stationId*13;
        int hs2 = String.valueOf(bitrate).hashCode()*17;
        return hs1*hs2;
    }

    @Override
    public boolean equals(Object obj) {
        PipeInfo other = (PipeInfo) obj;
        if (value == null | other.value == null) {
            return false;
        } else if (value.equals(other.value))
            return true;
        else
            return false;
    }

}
