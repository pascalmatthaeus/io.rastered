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
    String[] ffmpegCommand = 
    {
        "ffmpeg",
        "-y",
        "-f",
        "image2pipe",
        "-use_wallclock_as_timestamps",
        "1",
        "-r",
        "10",
        "-vcodec",
        "mjpeg",
        "-i",
        "-",
        "-vf",
        "tmix=frames=15",
        "-c:v",
        "libx264",
        //"h264_nvenc",
        "-bf",
        "0",
        "-an",
        /*"-acodec",
        "aac",
        "-pes_payload_size",
        "0",*/
        "-preset",
        "ultrafast",
        //"ll",
        "-tune",
        "zerolatency",
        "-b:v",
        "35k",
        "-r",
        "20",
        "-f",
        "flv",
        "placeholder"
    };
    
    public VideoStream(int streamKey)
    {
        ffmpegCommand[ffmpegCommand.length-1] = 
                "rtmp://127.0.0.1:1935/app/stream"
                + streamKey
                ;//+ "?pkt_size=1316";
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
