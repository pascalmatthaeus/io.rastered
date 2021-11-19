package experimental.cudarastered;



import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

public class Image 
{
    public BufferedImage stream,streamOrig;
    public float[] rgb;
    public String path;
    public int width, height;
    
    public void load(String path)
    {
         try 
        { 
            stream = ImageIO.read(new File(path));
            streamOrig = stream;
            this.path = path;
            width = stream.getWidth();
            height = stream.getHeight();
            getRGB();
        } catch(IOException e){}
    }
    
    public void getRGB() 
    {
        rgb = new float[width*height];
        int c=0;
        for (int j=0;j<height;j++)
        {
            for (int i=0;i<width;i++)
            {
                int tmp = stream.getRGB(i,j);
                rgb[c]=(float)((tmp>>16)&0xff)/256.f;
                c++;
            }
        }
    }
    
    public void getStream(float[] rgb)
    {
        int[][] testArr = new int[2][8];
        testArr[0][1]=1;
        testArr[1][3]=3;
        int tmp=0;
        for (int i=0;i<rgb.length;i++)
        {
            tmp = (int)(rgb[i]*255.0f);
            tmp = (255<<24)|(tmp<<16)|(tmp<<8)|tmp;
            stream.setRGB(i%width, i/width, tmp);
        }
    }
    
    
}
