package io.rastered.video;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class EncoderConfiguration 
{
    public static final String [] DEFAULT = 
            new EncoderConfiguration().getCommand();
    
    public String [] templateCommand = new String []
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
            "placeholder_frameblending",
            "-c:v",
            "placeholder_encodername",
            "-bf",
            "0",
            "-an",
            "-preset",
            "placeholder_encoderqualitypreset",
            "-tune",
            "zerolatency",
            "-b:v",
            "placeholder_bitrate",
            "-r",
            "20",
            "-f",
            "flv",
            "placeholder_streamkey"
        };
    
    private Bitrate bitrate;
    private Encoder encoder;
    private FrameBlending frameblending;
    
    public EncoderConfiguration()
    {
        this.bitrate = Bitrate.MEDIUM;
        this.encoder = Encoder.X264;
        this.frameblending = FrameBlending.MEDIUM;
    }
    
    public EncoderConfiguration(Bitrate bitrate, 
            Encoder encoder, FrameBlending frameblending)
    {
        this.bitrate = bitrate;
        this.encoder = encoder;
        this.frameblending = frameblending;
    }
    
    public enum Bitrate 
    {
        VERYLOW("20k"),LOW("50k"),MEDIUM("100k"),HIGH("200k"),VERYHIGH("800k");
        
        private String level;
        Bitrate(String level)
        {
            this.level = level;
        }
        public String get()
        {
            return level;
        }
    }
    
    public enum Encoder
    {
        X264("libx264","ultrafast"),NVENC("h264_nvenc","ll");
        
        private String libName;
        private String qualityPreset;
        Encoder(String libName, String qualityPreset)
        {
            this.libName = libName;
            this.qualityPreset = qualityPreset;
        }
        public String getLibName()
        {
            return libName;
        }
        public String getQualityPreset()
        {
            return qualityPreset;
        }
    }
    
    public enum FrameBlending 
    {
        VERYLOW("2"),LOW("5"),MEDIUM("15"),HIGH("20"),VERYHIGH("25");
        
        private String amountFrames;
        FrameBlending(String amountFrames)
        {
            this.amountFrames = amountFrames;
        }
        public String get()
        {
            return amountFrames;
        }
    }
    
    public String [] getCommand()
    {
        List<String> parameterList = 
                new ArrayList<String>(Arrays.asList(templateCommand));
        
        parameterList.set (
                parameterList.indexOf("placeholder_frameblending"), 
                "tmix=frames="+this.frameblending.get()
        );
        
        parameterList.set (
                parameterList.indexOf("placeholder_encodername"), 
                this.encoder.getLibName()
        );
        
        parameterList.set (
                parameterList.indexOf("placeholder_encoderqualitypreset"), 
                this.encoder.getQualityPreset()
        );
        
        parameterList.set (
                parameterList.indexOf("placeholder_bitrate"), 
                this.bitrate.get()
        );
        
        if (this.encoder == Encoder.NVENC) {
            parameterList.remove("-tune");
            parameterList.remove("zerolatency");
        }
        
        return parameterList.toArray(new String[parameterList.size()]);
    }
}
