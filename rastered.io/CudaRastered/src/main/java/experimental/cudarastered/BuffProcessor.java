package experimental.cudarastered;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class BuffProcessor 
{
    private BufferedImage im,filtered;
    
    public BuffProcessor(File f)
    {
        try{ 
            this.im=ImageIO.read(f);
            this.filtered=ImageIO.read(f);
        } catch(Exception e){};
    }
    
    public BufferedImage getImage()
    {
        return im;
    }
    
    public BufferedImage getFiltered()
    {
        return filtered;
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
                filtered.setRGB(i,j,ir);
            }
        }
    }
    
    public void filterExp(int v)
    {
        for (int i=0;i<filtered.getWidth();i++)
        {
            for (int j=0;j<filtered.getHeight();j++)
            {
                int rgb=filtered.getRGB(i, j);
                float r = ((rgb>>16) & 0xff)/256.0f;
                r*=((float)v/100.0f);
                r = Math.min(Math.max(0f, r),1f);
                r*=256.0f;
                int ir = (int)r;
                ir = (255<<24)|(ir<<16)|(ir<<8)|ir;
                filtered.setRGB(i,j,ir);
            }
        }
    }
}
