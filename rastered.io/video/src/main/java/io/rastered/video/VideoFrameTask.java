package io.rastered.video;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.BufferedInputStream;

public class VideoFrameTask implements Runnable
{
    Socket cSocketIn;
    DataInputStream socketDIS;
    BufferedInputStream socketBIS;
    
    VideoFrameTask(Socket cSocketIn)
    {
        this.cSocketIn = cSocketIn;
    }
    
    @Override
    public void run()
    {
        System.out.println("VideoFrameTask "+Thread.currentThread().getName()+" started.");
        Thread.currentThread().setPriority(8);
        int imgBytes;
        try
        {
            socketBIS = new BufferedInputStream(cSocketIn.getInputStream());
            socketDIS = new DataInputStream(socketBIS);
        } catch(Exception e){}
        
        VideoStream vidStream = null;
        try
        {
            vidStream = new VideoStream(socketDIS.readInt());
            new Thread(vidStream).start();
        } catch (Exception e) {}
        
        
        while (true) 
        {
            try 
            {
                imgBytes = socketDIS.readInt();
                // vidStream.out.write(socketDIS.readNBytes(imgBytes));
                byte [] temp = socketDIS.readNBytes(imgBytes);
                for (int i=0;i<20;i++) vidStream.out.write(temp);
                vidStream.out.flush();
            } catch (Exception e) {}
        }
    }
}
