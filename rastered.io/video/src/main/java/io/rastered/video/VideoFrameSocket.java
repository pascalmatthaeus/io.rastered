package io.rastered.video;

import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VideoFrameSocket 
{
    private final ThreadPoolExecutor frameTaskExecutor = new ThreadPoolExecutor(
            2, 128, 10000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(4)
    );
    
    public VideoFrameSocket(int port) throws Exception
    {
        ServerSocket svrSck = new ServerSocket(6679);
        SocketAcceptor acceptorThread = new SocketAcceptor(svrSck,frameTaskExecutor);
        new Thread(acceptorThread).start();
    }
    
    public VideoFrameSocket(int port,VideoStream targetStream, int acceptorThreadCount) throws Exception
    {
        ServerSocket svrSck = new ServerSocket(6679);
        SocketAcceptor acceptorThreads [] = new SocketAcceptor[acceptorThreadCount];
        for (int i=0;i<acceptorThreads.length;i++)
        {
        
            acceptorThreads[i] = new SocketAcceptor(svrSck,frameTaskExecutor);
            new Thread(acceptorThreads[i]).start();
        }
    }
}
