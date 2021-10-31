package io.rastered.app.service;

import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
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
    
    public SocketClient(String ip, int port) throws Exception
    {
        //synchronized(this){
        try
        {
            cSck = new Socket(ip,port);
        } catch (Exception e) {e.printStackTrace();}//};
    }
    
    public void sendMsg(int streamKey) throws Exception
    {
        new DataOutputStream(cSck.getOutputStream()).writeInt(streamKey); // lacks buffer >> no flush needed
    }
    
    public void sendMsg(BufferedImage bi) throws Exception
    {
        OutputStream sOut = cSck.getOutputStream();
        DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(sOut));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi,"jpeg",baos);
        byte [] framebuffer = baos.toByteArray();
        dataOut.writeInt(framebuffer.length);
        dataOut.write(framebuffer);
        //dataOut.writeInt(-1);
        dataOut.flush();
    }
    
    public void stopConnection() throws Exception
    {
        in.close();
        out.close();
        cSck.close();
    }
}
