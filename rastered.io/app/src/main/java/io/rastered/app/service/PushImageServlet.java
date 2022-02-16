package io.rastered.app.service;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class PushImageServlet extends HttpServlet 
{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        try 
        {
            BufferedImage image = ImageIO.read(new BufferedInputStream(request.getInputStream()));
            HttpSession session = request.getSession();
            session.setAttribute("image", image);
            session.setAttribute("imageprocessor", new ImageProcessor(image));
            System.out.println("Info: "+image.getColorModel().toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
