package io.rastered.app.service;

import io.rastered.app.model.FilterParameters;
import jakarta.json.*;
import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import java.util.Enumeration;

public class FilterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
        
        // Request parameters removed here for thread safety.
        // Fields declared here are not thread safe, single shared instance.
        private String pathToWeb;
        private File f;
        private BufferedImage bi;
        
        @Override
        public void init() throws ServletException
        {
            pathToWeb = getServletContext().getRealPath(File.separator);
            try{f = new File("/home/pascal/Pictures/lenna.jpg");}catch(Exception e){}
            System.out.println(pathToWeb+"720.jpg");
            try{bi = ImageIO.read(f);}catch(Exception e){}
        }
        
        @Override
	protected void doPost( HttpServletRequest request, HttpServletResponse response) 
                throws IOException 
        {
            // Figure here which objects are reusable and therefore should be inside ctx scope.
            // => assign an individual float buffer (2D array) to each client session,
            // transform ImageProcessor into a stateless API (which doesn't own the float buffer)
            // => store the origImage as a separate session attribute. Overwrite if client clicks "apply"
            // use a global ImageProcessor with float[][] img as input.
            
            HttpSession session = request.getSession();
            
            // Debug: List all attributes for current session.
            Enumeration<String> attributes = request.getSession().getAttributeNames();
            while (attributes.hasMoreElements()) {
                System.out.println((String) attributes.nextElement());
            }
            
            ImageProcessor sessionImPro = (ImageProcessor)session.getAttribute("imageprocessor");
            SocketClient sessionSocket = (SocketClient)session.getAttribute("socket");
            
            if ( sessionImPro == null ) 
            {
                System.out.println("New Session created.");
                sessionImPro = new ImageProcessor(bi);
                session.setAttribute("imageprocessor", sessionImPro);
            }
            
            if ( sessionSocket == null )
            {
                try
                {
                    sessionSocket = new SocketClient("127.0.0.1",6679);
                    session.setAttribute("socket", sessionSocket);
                    int streamKey = (int) System.currentTimeMillis();
                    session.setAttribute("streamkey", streamKey);
                    sessionSocket.sendMsg(streamKey);
                } catch (Exception e) { sessionSocket=null; }
            }
            
            // Parse request parameters:
            // valueGam = Integer.parseInt(request.getParameter("valGam").toString());
            
            // now this is thread safe
            int [] paramList = {100,100,100};
            int [] paramFetch = new FilterParameters(request).get();
            if (paramFetch != null) paramList = paramFetch;
            
            Integer sk = (Integer)session.getAttribute("streamkey");
            if (sk != null) 
            {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().print((Json.createObjectBuilder().add(
                        "streamKey", sk.toString())
                        .build()
                        .toString()));
            }
            
            synchronized(this) 
            {
                sessionImPro.reset();
                sessionImPro.filterGam(paramList.length==2?paramList[0]:100);
                sessionImPro.filterExp(paramList.length==2?paramList[1]:100);

                //OutputStream out = response.getOutputStream();

                try
                {
                    sessionSocket.sendMsg(sessionImPro.getImage());
                } catch (Exception e)
                { 
                    System.out.println("Could not send frame to io.rastered.video!");
                    e.printStackTrace();
                }

                //ImageIO.write(sessionImPro.getImage(), "jpeg", out);
                
                //out.flush();
                //out.close();
            }
	}
}