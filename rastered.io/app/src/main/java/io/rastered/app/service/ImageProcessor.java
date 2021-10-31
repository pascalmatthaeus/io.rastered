package io.rastered.app.service;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;

public class ImageProcessor 
{
    private BufferedImage im,orig;
    
    public ImageProcessor(BufferedImage im)
    {
        this.im=im;
        this.orig=deepCopy(this.im);
    }
    
    public BufferedImage getImage()
    {
        return im;
    }
    
    public void reset()
    {
        this.im=deepCopy(this.orig);
    }
    
    private BufferedImage deepCopy(BufferedImage bi) 
    {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    
    public void filterExp(int v)
    {
        for (int i=0;i<im.getWidth();i++)
        {
            for (int j=0;j<im.getHeight();j++)
            {
                int rgb=im.getRGB(i, j);
                float r = ((rgb>>16) & 0xff)/256.0f;
                r*=((float)v/100.0f);
                r = Math.min(Math.max(0f, r),1f);
                r*=256.0f;
                int ir = (int)r;
                ir = (255<<24)|(ir<<16)|(ir<<8)|ir;
                im.setRGB(i,j,ir);
            }
        }
    }
    
    public void filterGam(int v)
    {
        for (int i=0;i<im.getWidth();i++)
        {
            for (int j=0;j<im.getHeight();j++)
            {
                int rgb=im.getRGB(i, j);
                float r = ((rgb>>16) & 0xff)/256.0f;
                r = (float)Math.pow(r, 1.0f/((float)v/100f));
                r = Math.min(Math.max(0f, r),1f);
                r*=256.0f;
                int ir = (int)r;
                ir = (255<<24)|(ir<<16)|(ir<<8)|ir;
                im.setRGB(i,j,ir);
            }
        }
    }
}
