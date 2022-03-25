package io.rastered.video;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class VideoStream implements Runnable
{
    private volatile boolean shutdownRequested;
    private volatile boolean restartRequested;
    private volatile EncoderConfiguration encoderConfig;
    private Process encoderProcess;
    public BufferedOutputStream out;
    
    public VideoStream( EncoderConfiguration encoderConfig )
    {
        this.encoderConfig = encoderConfig;
    }
    
    @Override
    public void run()
    {
        Thread.currentThread().setPriority(8);
        while (!shutdownRequested)
        {
            System.out.println("rerunning the loop");
            restartRequested = false;
            String [] ffmpegCommand = this.encoderConfig.buildCommand();
            var pBuilderFfmpeg = new ProcessBuilder(ffmpegCommand);
            try
            {
                encoderProcess = pBuilderFfmpeg.start();
                System.out.println(
                    "Encoder launched, client provider target is: "
                    + ffmpegCommand[ffmpegCommand.length-1]);
            }
            catch(Exception e)
            {
                System.out.println("Error running video encoder as sub-process.");
                e.printStackTrace();
            }

            out = new BufferedOutputStream( encoderProcess.getOutputStream() );

            // Logging & Error Stream Redirection
            var br = new BufferedReader(
                new InputStreamReader(encoderProcess.getErrorStream())
            );
            String ffmpegOutSample;
            //StringBuilder sb = new StringBuilder(); // store the output
            
            synchronized( this )
            {
                notify();
            }
            
            while (!restartRequested && !shutdownRequested)
            {
                try
                {
                    ffmpegOutSample = br.readLine();
                    System.out.println(ffmpegOutSample);
                    //sb.append(line); // store the output
                } catch( IOException e )
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void setResolution( int width, int height )
    {
        encoderConfig.setVideoDimensions(width, height);
        restartEncoder();
    }
    
    public void shutdown()
    {
        encoderProcess.destroyForcibly();
        shutdownRequested = true;
    }
    
    public void restartEncoder()
    {
        encoderProcess.destroyForcibly();
        restartRequested = true;
    }
}
