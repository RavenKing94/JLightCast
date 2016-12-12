package ir.saitech.jlightcast.Caster;

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
    private int bitrates[];

    public SyncSocket(int portBase, int bt[], int cnt)
    {
        count = cnt;
        sockList = new JLCSyncedList(cnt);
        socketBasePort=portBase;
        bitrates = bt;
    }

    @Override
    public void run() {
        byte[] byt = new byte[1024];
        boolean head=false;
        int c=0,l=0;
        try {
            for (int i=0;i<count;i++) {
                sockList.add(new Socket("localhost", socketBasePort+bitrates[i]));
                ((Socket)sockList.get(i)).setKeepAlive(true);
            }
            while (true){
                Thread.sleep(10);
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
