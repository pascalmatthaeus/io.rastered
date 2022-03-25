package io.rastered.video;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.util.concurrent.ExecutorService;

public class VideoFrameTask implements Runnable
{
    protected Socket cSocketIn;
    protected ExecutorService workerPool;
    
    VideoFrameTask(Socket cSocketIn, ExecutorService workerPool)
    {
        this.cSocketIn = cSocketIn;
        this.workerPool = workerPool;
    }
    
    @Override
    public void run()
    {   
        System.out.println(
            "VideoFrameTask "+Thread.currentThread().getName()+" started."
        );
        Thread.currentThread().setPriority(8);
        
        try ( var socketDIS = new DataInputStream(
            new BufferedInputStream(cSocketIn.getInputStream())))
        {
            int streamKey = socketDIS.readInt();
            int frameWidth = socketDIS.readInt();
            int frameHeight = socketDIS.readInt();
            
            final var vidStream = new VideoStream
            (
                EncoderConfigurationFactory.build
                (
                    streamKey, frameWidth, frameHeight, 
                    EncoderConfigurationFactory.Quality.HIGH
                )
            );
            workerPool.execute(vidStream);
            
            synchronized( vidStream )
            {
                vidStream.wait();
            }
            
            byte [] framebuffer = new byte[frameWidth * frameHeight * 3];
            
            while (true) 
            {
                try {
                    socketDIS.readFully(framebuffer);
                    for (int i=0;i<10;i++) vidStream.out.write(framebuffer);
                    vidStream.out.flush();
                    
                    int frameWidthNew = socketDIS.readInt();
                    int frameHeightNew = socketDIS.readInt();
                    
                    if (frameWidthNew != frameWidth 
                        || frameHeightNew != frameHeight)
                    {
                        frameWidth = frameWidthNew;
                        frameHeight = frameHeightNew;
                        vidStream.setResolution(frameWidth, frameHeight);
                        framebuffer = new byte[frameWidth * frameHeight * 3];
                        synchronized( vidStream )
                        {
                            vidStream.wait();
                        }
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
