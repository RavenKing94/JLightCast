package ir.saitech.jlightcast.Caster;

import ir.saitech.jlightcast.Classes.ClientSocket;
import ir.saitech.jlightcast.Classes.PipeInfo;
import ir.saitech.jlightcast.Classes.Station;
import ir.saitech.jlightcast.Classes.StationList;
import ir.saitech.jlightcast.Utils.HTTPResponse;
import ir.saitech.jlightcast.Utils.HTTPGETParser;
import ir.saitech.jlightcast.Utils.Out;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * Created by blk-arch on 9/10/16.
 * Receives Client Connections
 */
public class JLCSocketAccepterEx implements Runnable {

    private ServerSocketChannel svSock;
    private SocketChannel socket;
    private int serverPort;
    private SocketAcceptListener sal;
    private boolean head;
    private Selector ssel;
    private ClientSocket cls;
    private HTTPGETParser parser = new HTTPGETParser();
    private Station station;
    private Station.StreamBitrate bitrate;

    public JLCSocketAccepterEx(int port){
        serverPort = port;
    }

    public void setServerSocketListener(SocketAcceptListener listener){
        sal = listener;
    }

    @Override
    public void run() {
        while (true) {
            try {
                svSock = ServerSocketChannel.open();
                svSock.socket().bind(new InetSocketAddress(serverPort));
                svSock.configureBlocking(true);
                System.out.println("Port Running : " + serverPort);
            } catch (IOException e) {
                Out.elog("SocketAccepter",e.getMessage());
                return;
            }

            while (true) {
                try {
                    socket = svSock.accept();
                    // cls = makeClientSocket(socket);
                    socket.configureBlocking(true);
                    socket.socket().setKeepAlive(true);
                    cls = makeClientSocket(socket);
                    Out.println("Accepted : " + socket.getRemoteAddress() + " on " + cls.getStationId());
                    socket.configureBlocking(false);
                    sal.onAccept(cls);
                }catch (Exception e) {
                    Out.elog("SocketAccepter", e.getMessage());
                }
            }
        }
    }

    private ClientSocket makeClientSocket(SocketChannel sc){
        try {
            List<String> sl = parser.parseGET(readClientHeader(sc));
            Thread.sleep(5);
            // Check Station Name
            if (StationList.exists(sl.get(0))){
                // Read Bitrate
                bitrate = Station.sbFromString(sl.get(1));
                // Check if bitrate was given in GET request
                if (bitrate != null){
                    // Make ClientSocket object using Station Id & bitrate
                    station = StationList.findByName(sl.get(0));
                    if (station == null){
                        Out.elog("Accepter-makeClient", "StationList.findByName returned null !");
                        sc.write(ByteBuffer.wrap(HTTPResponse.getNewFail().getBytes()));
                    }
                    // Make ClientSocket object using Station Id & bitrate if given bitrate and StationId are correct
                    if (pipeExists(station.getId(), bitrate))
                        return new ClientSocket(sc, new PipeInfo(station.getId(),bitrate));
                    else {
                        // No bitrate (404)
                        sc.write(ByteBuffer.wrap(HTTPResponse.getNewFail().getBytes()));
                    }
                } else {
                    // No bitrate (404)
                    sc.write(ByteBuffer.wrap(HTTPResponse.getNewFail().getBytes()));
                }
            } else {
                // No Station (404)
                sc.write(ByteBuffer.wrap(HTTPResponse.getNewFail().getBytes()));
            }
        } catch (IOException e) {
            Out.elog("makeClientSocket", e.getMessage());
        } catch (InterruptedException ie) {
            Out.elog("makeClientSocket", ie.getMessage());
        }
        return null;
    }

    /**
     * Reads GET request header from client
     */
    private String readClientHeader(SocketChannel sc){
        byte[] bt = new byte[100];
        int len = 0;
        try {
            len = sc.socket().getInputStream().read(bt);
        } catch (IOException e) {
            Out.elog("readClientHeader",e.getMessage());
        }
        return new String(bt,0,len);
    }

    public interface SocketAcceptListener{
        void onAccept(ClientSocket clientSocket);
    }

    private boolean pipeExists(int sId, Station.StreamBitrate sb){
        // get Station from StationId
        Station st;
        st = StationList.findById(sId);

        // get Bitrates from Station
        Station.StreamBitrate[] ssb;
        ssb = st.getBitrates();
        // Although i know it is not null, i still check in case sh*t happens!(Multithreaded nightmare!)
        if (ssb == null) {
            Out.elog("Accepter-pipeExists","getBitrates returned null !");
            return false;
        }

        // Check to see if the requested bitrate exists in requested station
        for (int i = 0; i < ssb.length; i++) {
            if (ssb[i] == sb) return true;
        }

        return false;
    }
}
