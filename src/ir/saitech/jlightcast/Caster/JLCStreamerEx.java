package ir.saitech.jlightcast.Caster;

import ir.saitech.jlightcast.Classes.ClientSocket;
import ir.saitech.jlightcast.Classes.PipeInfo;
import ir.saitech.jlightcast.Classes.StationPipes;
import ir.saitech.jlightcast.Utils.CPUUtils;
import ir.saitech.jlightcast.Utils.Out;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * Streams files(pipes) to specified ports
 * Created by blk-arch on 9/13/16.
 */
public class JLCStreamerEx implements Runnable {

    private SocketChannel selSocket;
    private Selector sel;
    private final int BUFFERSIZE;
    private int connected=0;
    private ClientSocket cls;
    private int[] len;
    private byte[][] bt;
    private char[][] ch;
    private PipeInfo[] pi;
    private PipedReader[] pr;
    public boolean ready = false;
    public ArrayBlockingQueue<ClientSocket> clientQueue = new ArrayBlockingQueue<ClientSocket>(10,true);

    public JLCStreamerEx(int bufferSize){
        BUFFERSIZE = bufferSize;
    }


    @Override
    public void run() {
        try {
            sel = SelectorProvider.provider().openSelector();
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateStations();
        ready = true;

        ByteBuffer bb;

        while (true){
            if (clientQueue.peek()!=null) {
                syncClients();
            }
            Out.println(" - - - 1");
            for (int f = 0; f< StationPipes.count(); f++) {
                try {
                    // read from all pipes
                    if (!pr[f].ready()) continue;
                    len[f]=pr[f].read(ch[f]);
                    charArrayToByteArray(ch[f],bt[f],len[f]);
                    //Out.ilog("Streamer1","read");
                } catch (Exception e) {
                    //Out.elog("Streamer1",e.getMessage());
                }
            }
            Out.println(" - - - 2");
            try {
                ready = false;

                int cnt = sel.select(100);
                if (cnt==0) continue;
                Out.println(" - - - 3");

                Set keys = sel.selectedKeys();
                Iterator it = keys.iterator();
                while (it.hasNext()) {
                        try {
                            SelectionKey key = (SelectionKey) it.next();
                            if (key.isWritable()) {
                                cls = (ClientSocket) key.attachment();

                                // Client PipeReader Id
                                int clpId;
                                clpId = getPipeIndex(cls.getPipeInfo());

                                // Check if pipe still exists
                                if (clpId == -1) throw new Exception("Pipe not found !");

                                // Check if there is a problem with pipe
                                if (len[clpId] <= 0) continue;

                                // Write buffer to client
                                bb = ByteBuffer.wrap(bt[clpId], 0, len[clpId]);
                                selSocket = ((SocketChannel) key.channel());
                                selSocket.write(bb);
                            }
                        }catch (Exception ee) {
                            try {
                                connected--;
                                Out.println(ee + "\nSocket Close : " + selSocket.getRemoteAddress() + " - " + connected);
                                selSocket.close();
                            } catch (IOException e) {
                                Out.elog("Streamer", "Error When Closing Socket !!!");
                            }
                        }
                }
                keys.clear();
                ready = true;
                Out.println(" - - - target reached !");
                Out.println(" - - - Queue size in target line "+clientQueue.size());
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                } catch (InterruptedException e) {
                    Out.elog("Streamer",e.getMessage());
                }
                //System.out.println("Served");
            } catch (IOException e) {
                Out.elog("Streamer",e.getMessage());
            }
        }
    }

    private void _init(){
        len = new int[StationPipes.count()];

        bt = new byte[StationPipes.count()][BUFFERSIZE];

        ch = new char[StationPipes.count()][BUFFERSIZE];
    }

    private void _init_pipes(){
        pi = StationPipes.getKeys();
        pr = StationPipes.getValues();
    }

    private int getPipeIndex(PipeInfo clientPipeInfo){
        for (int i = 0; i < pi.length; i++) {
            if (pi[i].equals(clientPipeInfo)) {
                return i;
            }
        }
        return -1;
    }

    private void charArrayToByteArray(char[] ch, byte[] bt, int len){
        for (int i = 0; i < len; i++) {
            bt[i] = (byte)ch[i];
        }
    }
    
    public void syncClients() {
        ready = false;
        Out.println(" - - - syncClient start");
        List<ClientSocket> clientSocket = new ArrayList<>();
        int n = clientQueue.drainTo(clientSocket);
        Out.println(" - - - Queue size "+n);
            try {
                for (int i = 0; i < n; i++) {
                    Out.println("syncClients before register");
                    clientSocket.get(i)
                            .getSocketChannel()
                            .register(sel,SelectionKey.OP_WRITE)
                            .attach(clientSocket);
                    Out.println("syncClients after register");
                    Out.ilog("Streamer-addClient","Client added!");
                    connected++;
                    try {
                        Out.print("Socket Opened "
                                + clientSocket.get(i).getSocketChannel().getRemoteAddress()
                                + ":"+clientSocket.get(i).getStationId());
                    } catch (IOException e) {
                        Out.elog("Streamer-ch2bt",e.getMessage());
                        connected--;
                    }
                    Out.println(" - - - "+connected);
                }
                clientQueue.clear();
            } catch (IOException e) {
                Out.elog("Streamer-addClient", e.getMessage());
                connected--;
                ready = true;
                return;
            }
        ready = true;
    }

    public synchronized void updateStations(){
        Out.ilog("Streamer","begin init");
        _init();
        Out.ilog("Streamer","end init");
        _init_pipes();
        Out.ilog("Streamer","end init_pipes");
    }
}
