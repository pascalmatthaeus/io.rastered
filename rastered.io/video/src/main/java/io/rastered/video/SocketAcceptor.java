package io.rastered.video;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class SocketAcceptor implements Runnable
{
    private ServerSocket svrSck;
    private Socket cSck;
    public static BufferedImage img;
    
    private final ThreadPoolExecutor workerPool;
    
    public SocketAcceptor(ServerSocket svrSck, ThreadPoolExecutor workerPool)
    {
        this.svrSck = svrSck;
        this.workerPool = workerPool;
    }
    
    @Override
    public void run()
    {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        while(true)
        {
            try
            {
                workerPool.execute(new VideoFrameTask(svrSck.accept()));
                System.out.println("Socket client connected.");
                //System.out.println("Tasks: "+workerPool.getQueue().size());
                //System.out.println("Pool size: "+workerPool.getPoolSize());
            } catch (Exception e){
                System.out.println("Error executing frame consumer thread.");
                img=null;
            }
        }
    }
    
    public void bye() throws IOException
    {
        cSck.close();
        svrSck.close();
    }
}
