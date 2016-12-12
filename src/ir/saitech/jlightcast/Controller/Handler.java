package ir.saitech.jlightcast.Controller;

import ir.saitech.jlightcast.Caster.JLCSocketAccepterEx;
import ir.saitech.jlightcast.Caster.JLCStreamerEx;
import ir.saitech.jlightcast.Classes.*;
import ir.saitech.jlightcast.Utils.Out;

import java.io.PipedReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        Station station = new Station(name, st, sb, path);
        try {
            synchronized (StationList.class) {
                StationList.add(station);
            }
            addStationPipes(station);
        } catch (Exception e) {
            Out.elog("Handler-addStation",e.getMessage());
        }
    }

    private void addStationPipes(Station station){
        for (Station.StreamBitrate stb:
                station.getBitrates()) {
            StationPipes.add(new PipeInfo(station.getId(), stb),new PipedReader(102400));
        }
    }

    @Override
    public void onAccept(ClientSocket clientSocket) {
        while (!streamer.ready){
            try {
                TimeUnit.MICROSECONDS.sleep(100);
            } catch (InterruptedException e) {
                Out.elog("Handler-onAccept",e.getMessage());
            }
        }
    }
}
