package ir.saitech.jlightcast.Classes;

import java.util.concurrent.SynchronousQueue;

/**
 * Created by blk-arch on 12/14/16.
 *
 */
public class ClientQueue {
    private SynchronousQueue<ClientSocket> clq = new SynchronousQueue<>(false);

    private void add(ClientSocket cls){
        
    }
}
