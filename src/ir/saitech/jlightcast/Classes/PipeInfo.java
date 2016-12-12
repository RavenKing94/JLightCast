package ir.saitech.jlightcast.Classes;

/**
 * Created by blk-arch on 12/11/16.
 */
public class PipeInfo {
    private Integer stationId;
    private Station.StreamBitrate bitrate;

    public PipeInfo(Integer stId, Station.StreamBitrate sb){
        stationId = stId;
        bitrate = sb;
    }

    public Station.StreamBitrate getBitrate() {
        return bitrate;
    }

    public void setBitrate(Station.StreamBitrate sb) {
        bitrate = sb;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(int sId) {
        stationId = sId;
    }

}
