package io.rastered.app.service;

import io.rastered.app.core.Texture;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class PushImageServlet extends HttpServlet 
{
    @Override
    protected void doPost(HttpServletRequest request, 
        HttpServletResponse response) throws ServletException, IOException 
    {
        try 
        {
            BufferedImage image = ImageIO.read(
                new BufferedInputStream(request.getInputStream())
            );
            
            // Convert to bitmap if none was given
            if (image.getType() != BufferedImage.TYPE_3BYTE_BGR)
            {
                File f = new File(System.getProperty("user.dir"));
                ImageIO.write(image, "bmp", f);
                image = ImageIO.read(f);
            }
            HttpSession session = request.getSession();
            session.setAttribute("image", image);
            session.setAttribute("texture", new Texture(image));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
