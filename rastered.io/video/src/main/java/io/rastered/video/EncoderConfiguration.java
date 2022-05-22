package io.rastered.video;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class EncoderConfiguration 
{
    // DEFAULT removed => create EncoderConfigurationFactory
    // task: throw IllegalArgumentException for inapplicable input
    
    public static final String [] templateCommand = new String []
        {
            "ffmpeg",
            "-y",
            "-f",
            "rawvideo",
            "-pixel_format",
            "rgb24",
            "-video_size",
            "placeholder_dimensions",
            "-r",
            "6",
            "-i",
            "-",
            "-vf",
            "placeholder_frameblending",
            "-c:v",
            "placeholder_encodername",
            "-pix_fmt",
            "yuv420p",
            "-bf",
            "0",
            "-an",
            "-preset",
            "placeholder_encoderqualitypreset",
            "-tune",
            "zerolatency",
            "-crf",
            "20",
            "-b:v",
            "placeholder_bitrate",
            "-r",
            "12",
            "-f",
            "flv",
            "placeholder_streamkey"
        };
    
    private int streamKey;
    private int width;
    private int height;
    private Bitrate bitrate;
    private Encoder encoder;
    private FrameBlending frameblending;
    
    public EncoderConfiguration( int streamKey, int width, int height )
    {
        this.streamKey = streamKey;
        this.width = width;
        this.height = height;
        this.bitrate = Bitrate.MEDIUM;
        this.encoder = Encoder.X264;
        this.frameblending = FrameBlending.MEDIUM;
    }
    
    public EncoderConfiguration( int streamKey, int width, int height, 
        Bitrate bitrate, Encoder encoder, FrameBlending frameblending )
    {
        this.streamKey = streamKey;
        this.width = width;
        this.height = height;
        this.bitrate = bitrate;
        this.encoder = encoder;
        this.frameblending = frameblending;
    }
    
    public void setVideoDimensions( int width, int height )
    {
        this.width = width;
        this.height = height;
    }
    
    public enum Bitrate 
    {
        VERYLOW("20k"),
        LOW("50k"),
        MEDIUM("100k"),
        HIGH("200k"),
        VERYHIGH("800k");
        
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
        X264("libx264","ultrafast"),
        NVENC("h264_nvenc","ll");
        
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
        DISABLED("0"),
        VERYLOW("2"),
        LOW("5"),
        MEDIUM("15"),
        HIGH("20"),
        VERYHIGH("25");
        
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
    
    public String [] buildCommand()
    {
        List<String> parameterList = 
            new ArrayList<>(Arrays.asList(templateCommand)
        );
        
        if (this.frameblending == FrameBlending.DISABLED)
        {
            parameterList.remove(
                parameterList.indexOf("-vf")
            );
            parameterList.remove(
                parameterList.indexOf("placeholder_frameblending")
            );
        }
        else
        {
            parameterList.set (
                parameterList.indexOf("placeholder_frameblending"), 
                "tmix=frames="+this.frameblending.get()
            );
        }
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
        
        parameterList.set (
                parameterList.indexOf("placeholder_streamkey"),
                "rtmp://ome:1935/app/stream" + this.streamKey
        );
        
        parameterList.set (
                parameterList.indexOf("placeholder_dimensions"),
                this.width + "x" + this.height
        );
        
        if (this.encoder == Encoder.NVENC) {
            parameterList.remove("-tune");
            parameterList.remove("zerolatency");
        }
        
        return parameterList.toArray(new String[parameterList.size()]);
    }
}
