package io.rastered.app.service;

import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;

public class SocketClient 
{
    private Socket cSck;
    private DataOutputStream dataOut;
    
    public SocketClient( String ip, int port ) throws Exception
    {
        cSck = new Socket( ip, port );
        this.dataOut = new DataOutputStream(
            new BufferedOutputStream(cSck.getOutputStream())
        );
    }
    
    public void sendStreamKey( int streamKey ) throws Exception
    {
        dataOut.writeInt(streamKey);
        dataOut.flush();
    }
    
    public void sendTextureResolution(
        int width, int height ) throws Exception
    {
        dataOut.writeInt(width);
        dataOut.writeInt(height);
    }
    
    public void sendFrame( byte [] frame ) throws Exception
    {
        dataOut.write(frame);
        dataOut.flush();
    }
    
    public void stopConnection() throws Exception
    {
        cSck.close();
    }
}
