package io.rastered.video;

public class EncoderConfigurationFactory 
{
    public enum Quality { LOW,MEDIUM,HIGH,VERYHIGH; }
    
    public static EncoderConfiguration build( 
        int streamKey, int width, int height, Quality quality )
    {
        var bitrate = EncoderConfiguration.Bitrate.MEDIUM;
        
        switch (quality)
        {
            case LOW:
                bitrate = EncoderConfiguration.Bitrate.LOW;
                break;
            case MEDIUM:
                bitrate = EncoderConfiguration.Bitrate.MEDIUM;
                break;
            case HIGH:
                bitrate = EncoderConfiguration.Bitrate.HIGH;
                break;
            case VERYHIGH:
                bitrate = EncoderConfiguration.Bitrate.VERYHIGH;
                break;
        }
        
        return new EncoderConfiguration(
            streamKey, width, height,
            bitrate,
            EncoderConfiguration.Encoder.X264,
            EncoderConfiguration.FrameBlending.LOW
        );
    }
}
