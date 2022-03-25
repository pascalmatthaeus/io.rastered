package io.rastered.app.core;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Texture implements Cloneable
{
    private byte [] pixels;
    private byte [] pixelsOrig;
    private byte [] sampleFrom;
    private int width;
    private int height;
    private PixelFormat pxFmt;
    
    public enum PixelFormat { RGB24(), BGR24() };
    
    public Texture( int width, int height )
    {
        this( width,height,PixelFormat.RGB24 );
    }
    
    public Texture( int width, int height, PixelFormat pxFmt)
    {
        this.width = width; this.height = height;
        this.pixels = new byte [width*height*3];
        this.pixelsOrig = pixels.clone();
        this.sampleFrom = pixelsOrig;
        this.pxFmt = pxFmt;
    }
    
    public Texture( BufferedImage bi )
    {
        this(bi,PixelFormat.RGB24);
    }
    
    public Texture( BufferedImage bi, PixelFormat pxFmt )
    {
        if (bi.getType() != BufferedImage.TYPE_3BYTE_BGR)
            throw new IllegalArgumentException(
                "The image given must be encoded in the format B8G8R8!"
            );
        else
        {
            this.width = bi.getWidth(); 
            this.height = bi.getHeight();
            this.pxFmt = pxFmt;
            this.pixels = (
                (DataBufferByte)
                bi.getRaster()
                .getDataBuffer())
                .getData();
            
            if (pxFmt == PixelFormat.RGB24)
            {
                pixels = swapRedAndBlue(pixels);
            }
            this.pixelsOrig = pixels.clone();
            this.sampleFrom = pixelsOrig;
        }
    }
    
    public byte [] getData() { return pixels; }
    
    public byte [] getOriginalData() { return pixelsOrig; }
    
    public int getWidth() { return width; }
    
    public int getHeight() { return height; }
    
    // these two could be more beautiful
    public void sampleFrom( byte [] pixels )
    {
        sampleFrom = pixels;
    }
    public byte [] getSampleFrom()
    {
        return sampleFrom;
    }
    
    public void reset() { pixels = pixelsOrig.clone(); }
    
    public PixelFormat getPixelFormat() { return pxFmt; }
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Texture clone = (Texture)super.clone();
        clone.pixels = clone.pixels.clone();
        return clone;
    }
    
    public <I,O> O filter( FilterFunction<I,O> f, I... args )
    {
        return f.apply(this, args);
    }
    
    public Texture filter( String filterName, Float... args )
    {
        return Presets.valueOf(filterName).apply(this, args);
    }
    
    public <I,O> O filterNtimes( final int n, FilterFunction<I,O> f, I... args )
    {
        if ( n<1 )
            throw new IllegalArgumentException("Argument is not allowed to be"
                + " negative or zero."
            );
        else {
            O result = null;
            for (int i=0; i<n; i++) result = f.apply(this, args);
            return result;
        }
    }
    
    public static float normalize( float input )
    {
        return Math.min( 1.0f, Math.max(0.0f,input) );
    }
    
    // A standard bitmap maps color channels in the order BGR. This method
    // is for achieving RGB order.
    public static byte [] swapRedAndBlue( byte [] pixels )
    {
        for (int i=0; i<pixels.length; i+=3)
        {
            byte tmp = pixels[i];
            pixels[i] = pixels[i+2];
            pixels[i+2] = tmp;
        }
        return pixels;
    }
}
