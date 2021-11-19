package experimental.cudarastered;

import java.awt.Toolkit;

public class Api 
{
    public Cuda cuda;
    public Image image;
    public int screenX =
            (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public int screenY = 
            (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    
    public Api()
    {
        String imPath="/home/pascal/Pictures/lenna.jpg";
        image = new Image();
        image.load(imPath);
        cuda = new Cuda(this.image.width,this.image.height);
        info();
    }
    
    public void info()
    {
        System.out.println("Image loaded from "+image.path+".");
        System.out.println("Dimensions: "+image.width+"*"+image.height+".");
    }
    
    public void filter(CudaTechnique technique, int param)
    {
        cuda.launch(technique, param,image.rgb);
        image.getStream(cuda.hostOutput);
    }
}
