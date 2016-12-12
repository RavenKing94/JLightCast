package ir.saitech.jlightcast.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blk-arch on 9/10/16.
 */
public class FFMPEGRunner implements Runnable {

    private String input = "";

    private String output[];

    private String bitrate[];

    private boolean isMusicFile=false;

    public FFMPEGRunner(String infile,String outfile,String btr,boolean musicFile){
        input = infile;
        output = new String[]{outfile};
        bitrate = new String[]{btr};
        isMusicFile = musicFile;
    }

    public FFMPEGRunner(String infile,String[] outfile,String[] btr,boolean musicFile){
        input = infile;
        output = outfile;
        bitrate = btr;
        isMusicFile = musicFile;
    }

    @Override
    public void run() {
        try {
            Process pcb;
            String cmd = "ffmpeg";
            List<String> args= new ArrayList<>();
            args.add(cmd);
            if (isMusicFile) args.add("-re");
            args.add("-y");
            args.add("-i"); args.add(input);
            for (int i=0;i<output.length;i++){
                args.add("-f"); args.add("mp3");
                args.add("-ab"); args.add(bitrate[i]);
                args.add("-ar"); args.add("22050");
                if (output.length>1)
                    args.add(output[i]);
                else args.add("pipe:");
            }
            for (String item:args){
                System.out.print(item+" ");
            }
            System.out.println();
            pcb = new ProcessBuilder(args).start();
            while (pcb.isAlive()){
                Thread.sleep(500);
            }
            System.out.println("Process Fucked !");
            Thread.sleep(200);
            byte[] err = new byte[5000];
            int len=0;

            len = pcb.getErrorStream().read(err);
            System.out.println(new String(err));
            pcb.getOutputStream().write("y\n".getBytes());
            len = pcb.getErrorStream().read(err);
            System.out.println(new String(err));
            pcb.getOutputStream().write("y\n".getBytes());
            len = pcb.getErrorStream().read(err);
            System.out.println(new String(err));
            /*if (output.length==1) {
                byte[] outp = new byte[10240];
                //int len = 0;
                FileOutputStream outfile = new FileOutputStream(output[0]);
                while (true) {
                    len = pcb.getInputStream().read(outp);
                    if (len > 0)
                        outfile.write(outp, 0, len);
                }
            }
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    interface onClose{
        void onFFMPEGClosed();
    }
}
