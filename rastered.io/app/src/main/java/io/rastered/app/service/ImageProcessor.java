package io.rastered.app.service;

import io.rastered.app.core.Texture;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageProcessor 
{
    protected Texture target;
    protected byte [] rgb;
    protected byte [] rgbOrig;
    
    public ImageProcessor(Texture target)
    {
        this.target = target;
        rgb = target.getData();
        
        rgbOrig = rgb.clone();
    }
    
    public byte [] getRGBBuffer()
    {
        return rgb;
    }
    
    protected static byte [] getRGBBuffer( BufferedImage bi )
    {
        return ((DataBufferByte) bi.getRaster()
            .getDataBuffer())
            .getData();
    }
    
    public Texture getTarget()
    {
        return target;
    }
    
    public void reset()
    {
        this.rgb = rgbOrig.clone();
    }
    
    protected static float normalize(float fRGB)
    {
        return Math.min(1.0f, Math.max(0.0f,fRGB));
    }
    
    public void filterExp(int v)
    {
        for (int i=0;i<rgb.length;i++)
        {
            float fRGB = (rgb[i]&0xFF)/255.0f;
            fRGB *= (v/100.0f);
            rgb[i] = (byte)(fRGB*255.0f);
        }
    }
    
    public void filterGam(int v)
    {
        for (int i=0;i<rgb.length;i++)
        {
            float fRGB = (rgb[i]&0xFF)/255.0f;
            fRGB = (float)Math.pow(fRGB, 1.0f / (v/100.0f) );
            rgb[i] = (byte)(fRGB*255.0f);
        }
    }
    
    public void filterSharpness(int v)
    {
        int width = target.getWidth();
        
        for (int i=0;i<rgb.length;i++)
        {
            float fRGB = (rgb[i]&0xFF)/255.0f;
            int fetchIdxU = Math.min( width-1, (i%width)+3);
            int fetchIdxV = i/width;
            
            float fetch = 
                (rgb[fetchIdxV*width+fetchIdxU]&0xFF)/255.0f;
            float mask = fRGB - (fRGB + fetch)*0.5f;
            mask *= 10.0f;
            fRGB += (mask * (v/100.0f));
            fRGB = normalize(fRGB);
            rgb[i] = (byte)(fRGB*255.0f);
        }
    }
}
