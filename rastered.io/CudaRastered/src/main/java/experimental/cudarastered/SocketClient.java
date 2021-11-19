package experimental.cudarastered;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class SocketClient 
{
    private Socket cSck;
    private PrintWriter out;
    private BufferedReader in;
    
    public void startConnection(String ip, int port) throws Exception
    {
        cSck = new Socket(ip,port);
    }
    
    public void sendMsg(BufferedImage bi) throws Exception
    {
        ImageIO.write(bi, "jpeg", cSck.getOutputStream());
        cSck.getOutputStream().flush();
    }
    
    public void stopConnection() throws Exception
    {
        in.close();
        out.close();
        cSck.close();
    }
}
