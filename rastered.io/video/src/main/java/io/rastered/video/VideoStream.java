package io.rastered.video;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;
import javax.imageio.ImageIO;

public class VideoStream implements Runnable
{
    public volatile BufferedImage biOut;
    public BufferedOutputStream out;
    String [] ffmpegCommand;
    
    public VideoStream(int streamKey)
    {
        ffmpegCommand = EncoderConfiguration.DEFAULT;
        ffmpegCommand[ffmpegCommand.length-1] = 
                "rtmp://127.0.0.1:1935/app/stream" + streamKey;
    }
    
    public VideoStream(int streamKey, EncoderConfiguration config)
    {
        ffmpegCommand = config.getCommand();
        ffmpegCommand[ffmpegCommand.length-1] = 
                "rtmp://127.0.0.1:1935/app/stream" + streamKey;
    }
    
    @Override
    public void run()
    {
        Thread.currentThread().setPriority(8);
        try{biOut = ImageIO.read(new File("/home/sk/Pictures/lenna.jpg"));}catch(Exception e){}
        
        ProcessBuilder pBuilderFfmpeg = new ProcessBuilder(ffmpegCommand);
        
        Process encoderProcess;
        try
        {
            encoderProcess = pBuilderFfmpeg.start();
            System.out.println("Encoder launched, client provider target is: "+ffmpegCommand[ffmpegCommand.length-1]);
        } catch(Exception e)
        {
            System.out.println("Error running video encoder as sub-process.");
            e.printStackTrace();
            encoderProcess=null;
        }
        
        out = new BufferedOutputStream( encoderProcess.getOutputStream() );
        
        // Logging & Error Stream Redirection
        BufferedReader br = new BufferedReader(new InputStreamReader(encoderProcess.getErrorStream()));
        String ffmpegOutSample = null;
        //StringBuilder sb = new StringBuilder(); // store the output
        
        while (true)
        {
            try 
            {
                if (br.ready())
                {
                    ffmpegOutSample = br.readLine();
                    System.out.println(ffmpegOutSample);
                    //sb.append(line); // store the output
                }
            } catch(Exception e)
            { 
                System.out.println("Error shipping frame to video encoder.");
                e.printStackTrace();
            }
        }
    }
}
