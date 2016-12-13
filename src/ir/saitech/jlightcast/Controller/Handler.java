package ir.saitech.jlightcast.Controller;

import ir.saitech.jlightcast.Caster.JLCSocketAccepterEx;
import ir.saitech.jlightcast.Caster.JLCStreamerEx;
import ir.saitech.jlightcast.Caster.JLCWebRadioGrabber;
import ir.saitech.jlightcast.Caster.SyncSocket;
import ir.saitech.jlightcast.Classes.*;
import ir.saitech.jlightcast.Utils.Out;

import java.io.PipedReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by blk-arch on 12/9/16.
 *
 */
public class Handler implements Runnable,JLCSocketAccepterEx.SocketAcceptListener {

    ExecutorService tpe = Executors.newFixedThreadPool(4);
    JLCSocketAccepterEx accepter;
    JLCStreamerEx streamer;
    int port;

    public Handler(int port){
        this.port = port;
    }

    public void init(){
        Out.ilog("Handler","init() started");
        accepter = new JLCSocketAccepterEx(port);
        streamer = new JLCStreamerEx(2048);
        accepter.setServerSocketListener(this);
        tpe.execute(accepter);
        Out.ilog("Handler-init","accepter executed");
        //init_test();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tpe.execute(streamer);
        Out.ilog("Handler-init","streamer executed");
    }


    public void init_test() {
        Station.StreamBitrate[] sbr = new Station.StreamBitrate[1];
        sbr[0] = Station.StreamBitrate.Q128;

        PipedReader[] pr;
        Station st = addStation(
                "TheTrip",
                Station.StreamType.WEB,
                sbr,
                "http://ice1.somafm.com/thetrip-128-mp3"
        );
        PipedReader ppr = StationPipes.get(st.getId(), sbr[0]);
        //Out.println(ppr.toString());
        if (ppr == null) Out.println("ppr is NULL :|");
        tpe.execute(new JLCWebRadioGrabber("http://ice1.somafm.com/thetrip-128-mp3",
                ppr,
                4096));
    }

    /**
     * Add new Station
     */
    public Station addStation(String name, Station.StreamType st, Station.StreamBitrate[] sb, String path) {
        Station station = new Station(name, st, sb, path);
        try {
            Out.println("StationList.add started");
            StationList.add(station);
            Out.println("StationList.add finished");
            addStationPipes(station);
            new Thread(new SyncSocket(9000,sb,1,name.toLowerCase())).start();
            return station;
        } catch (Exception e) {
            e.printStackTrace();
            Out.elog("Handler-addStation",e.getMessage());
        }
        return null;
    }

    private PipedReader[] addStationPipes(Station station){
        Station.StreamBitrate[] sbr = station.getBitrates();
        if (sbr==null) Out.println("sbr is NULL !!!!!");
        PipedReader[] pr = new PipedReader[sbr.length];
        int i=0;
        for (Station.StreamBitrate stb:sbr) {
            pr[i] = new PipedReader(102400);
            StationPipes.add(new PipeInfo(station.getId(), stb),pr[i]);
            Out.println("addStationPipes - "+station.getId()+" "+stb.toString()+" added");
            i++;
        }
        Out.println("addStationPipes finished");
        Out.println("StationPipes size is "+StationPipes.count());
        return pr;
    }

    @Override
    public void onAccept(ClientSocket clientSocket) {
        /*while (!streamer.ready){
            try {
                TimeUnit.MICROSECONDS.sleep(100);
            } catch (InterruptedException e) {
                Out.elog("Handler-onAccept",e.getMessage());
            }
        }*/
        Out.println("before addClient");
        while (!streamer.clientQueue.add(clientSocket))
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                Out.elog("onAccept","Shit happened !");
            }
        Out.ilog("onAccept","Client Added");
    }

    @Override
    public void run() {
        Out.ilog("Hanlder","run !!!");
        init();
        init_test();
        while (true){
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                Out.elog("Handler-run",e.getMessage());
            }
        }
    }
}
