package ir.saitech.jlightcast.Caster;

import ir.saitech.jlightcast.Classes.Station;
import ir.saitech.jlightcast.Utils.JLCSyncedList;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by blk-arch on 9/11/16.
 */
public class SyncSocket implements Runnable {

    private JLCSyncedList sockList;
    private int count;
    private int socketBasePort;
    private Station.StreamBitrate[] bitrates;
    private String req_pre = "GET /";
    private String req_midle;
    private String req_post = " HTTP/1.1\r\n";
    private String st;

    public SyncSocket(int portBase, Station.StreamBitrate[] bt, int cnt, String station)
    {
        count = cnt;
        sockList = new JLCSyncedList(cnt);
        socketBasePort=portBase;
        bitrates = bt;
        st = station;
    }

    @Override
    public void run() {
        byte[] byt = new byte[1024];
        boolean head=false;
        int c=0,l=0;
        try {
            for (int i=0;i<count;i++) {
                sockList.add(new Socket("localhost", socketBasePort));
                ((Socket)sockList.get(i)).setKeepAlive(true);
            }
            while (true){
                Thread.sleep(10);
                for (int i=0;i<count;i++) {
                    req_midle = st + "/" + Station.sbToString(bitrates[i]);
                    String hd = req_pre + req_midle + req_post;
                    ((Socket) sockList.get(i)).getOutputStream().write(hd.getBytes());
                }
                for (int i=0;i<count;i++) {
                    l=((Socket)sockList.get(i)).getInputStream().read(byt);
                    c++;
                    if (c<10) {
                        System.out.println("Read : "+l);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SizeLimitExceededException sle) {
            sle.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
