package io.rastered.video;

import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VideoFrameSocket 
{
    /* corePoolSize here acts as a limit of threads created before the TPE will
    start queueing, causing it to wait until amount of threads running shrinks
    to a value lower than corePoolSize. keepAliveTime in ms acts as an idle timeout 
    until TPE kills thread, though functionality is not tested in this project yet. 
    The Class CachedThreadPool & the method ThreadPoolExecutor.setCorePoolSize
    might be interesting for future development, allowing implementation of a
    dynamically sized pool. */
    private final ThreadPoolExecutor frameTaskExecutor = new ThreadPoolExecutor(
            32, 64, 100000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(64)
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
