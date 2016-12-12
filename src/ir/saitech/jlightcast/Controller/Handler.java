package ir.saitech.jlightcast.Controller;


import ir.saitech.jlightcast.Caster.JLCSocketAccepterEx;
import ir.saitech.jlightcast.Caster.JLCStreamerEx;
import ir.saitech.jlightcast.Classes.ClientSocket;
import ir.saitech.jlightcast.Classes.Station;
import ir.saitech.jlightcast.Classes.StationList;
import ir.saitech.jlightcast.Utils.Out;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by blk-arch on 12/9/16.
 *
 */
public class Handler implements JLCSocketAccepterEx.SocketAcceptListener {

    ExecutorService tpe = Executors.newFixedThreadPool(4);
    JLCSocketAccepterEx accepter;
    JLCStreamerEx streamer;
    int port;

    public Handler(int port){
        this.port = port;
        init();
        init_test();
    }

    public void init(){
        Out.ilog("Handler","init() started");
        accepter = new JLCSocketAccepterEx(port);
        accepter.setServerSocketListener(this);
        tpe.execute(accepter);
        Out.ilog("Handler","accepter executed");

        init_test(); // FIXME: 12/10/16
    }


    public void init_test() {
        Station.StreamBitrate[] sbr = {Station.StreamBitrate.Q48,
                Station.StreamBitrate.Q96};
        addStation("The Trip", Station.StreamType.WEB, sbr, "http://ice1.somafm.com/thetrip-128-mp3");
    }

    /**
     * Add new Station
     */
    public void addStation(String name, Station.StreamType st, Station.StreamBitrate[] sb, String path) {
        try {
            synchronized (StationList.class) {
                StationList.add(new Station(name, st, sb, path));
            }
        } catch (Exception e) {
            Out.elog("Handler-addStation",e.getMessage());
        }
    }

    // FIXME: 12/12/16 Okkkaayy Biiiitch, I'm on it -_-
    @Override
    public void onAccept(ClientSocket clientSocket) {
        // getting client's Bitrate
        Station.StreamBitrate sb;
        sb = clientSocket.getStreamBitrate();

    }
}
