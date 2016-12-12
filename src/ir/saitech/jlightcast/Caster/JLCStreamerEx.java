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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * Streams files(pipes) to specified ports
 * Created by blk-arch on 9/13/16.
 */
// FIXME: 12/10/16 replacing file list with pipe list
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
    private boolean ready = false;

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

        _init();

        _init_pipes();

        ready = true;

        ByteBuffer bb;

        while (true){

            for (int f = 0; f< StationPipes.count(); f++) {
                try {
                    // read from all pipes
                    len[f]=pr[f].read(ch[f]);
                    charArrayToByteArray(ch[f],bt[f],len[f]);
                } catch (IOException e) {
                    Out.elog("Streamer",e.getMessage());
                }
            }
            try {
                ready = false;

                int cnt = sel.select(100);
                if (cnt==0) continue;

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
    
    public synchronized void addClient(ClientSocket clientSocket) {
        ready = false;
            try {
                clientSocket
                        .getSocketChannel()
                        .register(sel,SelectionKey.OP_WRITE)
                        .attach(clientSocket);
                connected++;
            } catch (IOException e) {
                Out.elog("Streamer-addClient", e.getMessage());
                connected--;
                ready = true;
                return;
            }
            try {
                Out.print("Socket Opened "
                        + clientSocket.getSocketChannel().getRemoteAddress()
                        + ":"+clientSocket.getStationId());
            } catch (IOException e) {
                Out.elog("Streamer-ch2bt",e.getMessage());
                connected--;
            }
            Out.println(" - "+connected);
        ready = true;
    }

    public synchronized void updateStations(){
        _init();
        _init_pipes();
    }
}
