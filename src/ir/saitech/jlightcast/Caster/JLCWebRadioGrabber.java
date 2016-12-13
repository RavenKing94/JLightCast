package ir.saitech.jlightcast.Caster;

import ir.saitech.jlightcast.Utils.Out;

import java.io.*;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by blk-arch on 9/11/16.
 *
 */

public class JLCWebRadioGrabber implements Runnable {

    private String url;
    //String pipe;
    private final int BUFFER_SIZE;
    private PipedWriter pw;
    private PipedReader pr;

    public JLCWebRadioGrabber(String mediaUrl, PipedReader pipedReader, int bufferSize){
        url = mediaUrl;
        pr = pipedReader;
        try {
            if (pipedReader == null) Out.println("pipedReader is NULL!!!");
            //pr.connect(pipedReader);
            pw = new PipedWriter();
        } catch (Exception e) {
            Out.elog("WebGrabber",e.getMessage());

            try {
                this.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        BUFFER_SIZE = bufferSize;
    }

    @Override
    public void run() {
        InputStream input;
        while (true) {
            try {
                input = new URL(url).openStream();
                pw.connect(pr);
                Out.println("Url Cpnnected : "+url);
                byte[] bt = new byte[BUFFER_SIZE];
                char[] ch = new char[BUFFER_SIZE];
                int len;
                while (true) {
                    len = input.read(bt);
                    for (int i = 0; i < len; i++) {
                        ch[i] = (char)bt[i];
                    }
                    if (len > 0) {
                        pw.write(ch,0,len);
                    }else throw new Exception("read len < 0");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Out.elog("WebGrabber",e.getMessage());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e1) {

                }
            }
        }
    }
}
