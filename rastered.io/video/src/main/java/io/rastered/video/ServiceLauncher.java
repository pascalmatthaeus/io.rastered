package io.rastered.video;

public class ServiceLauncher 
{    
    public static void main(String[] args) throws Exception
    {        
        VideoFrameSocket socket = new VideoFrameSocket(6679); // port, VideoStream (single-threaded)
        //VideoFrameSocket socket = new VideoFrameSocket(6679,stream,8); // port, VideoStream, acceptorThreadCount*/
    }
}