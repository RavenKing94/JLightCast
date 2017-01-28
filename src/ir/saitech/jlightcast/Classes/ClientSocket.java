package ir.saitech.jlightcast.Classes;

import java.nio.channels.SocketChannel;
import ir.saitech.jlightcast.Classes.Station.*;

/**
 * Created by blk-arch on 12/2/16.
 *
 */

// FIXME: 12/11/16 Add PipeInfo
public class ClientSocket {
    private SocketChannel socketChannel;
    private PipeInfo pipeInfo;
    private int id;

    public ClientSocket(SocketChannel sc, PipeInfo pi){
        socketChannel = sc;
        pipeInfo = pi;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public int getStationId() {
        return pipeInfo.getStationId();
    }

    public void setStationId(int stationId) {
        this.pipeInfo.setStationId(stationId);
    }

    public StreamBitrate getStreamBitrate() {
        return pipeInfo.getBitrate();
    }

    public void setStreamBitrate(StreamBitrate streamBitrate) {
        this.pipeInfo.setBitrate(streamBitrate);
    }

    public PipeInfo getPipeInfo(){
        return this.pipeInfo;
    }
}
