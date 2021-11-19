package io.rastered.app.service;

import java.io.OutputStream;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import jakarta.json.Json;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException 
        {
            // CORS fix - QnD do this using a filter.
            response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            //response.addHeader("Access-Control-Allow-Origin", "http://192.168.188.38:3000");
            response.addHeader("Access-Control-Allow-Origin", "https://rastered.io");
            response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, remember-me");
            response.addHeader("Access-Control-Max-Age", "1728000");
            
            
            // 07.06.2021: 
            // Implemented multiple image states (ImageProcessor objects) over sessions.
            // Each Session now has its own ImageProcessor stored as a session attribute.
            // This is inefficient for two reasons:
            // * redundant data that will be reusable for multiple client sessions
            // => rather assign an individual float buffer (2D array) to each client session.
            // => then transform ImageProcessor into a stateless API (which doesn't own the float buffer)
            // => store the origImage as a separate session attribute. Overwrite if client clicks "apply"
            // => ImageProcessor object just takes the float[][] origImage as an input, processes
            //    and pushes it into the Servlet response's output stream.
            // => If client clicks "apply", run doGet() routine again (refactor this) and overwrite 
            //    the Session attribute with the newly filtered image.
            
            // depends only on import HttpSession objects, works out of the box.
            HttpSession session = request.getSession();
            
            // DEBUGTRACE: List all attributes for current session. WARNING SPAM!
            /*Enumeration<String> attributes = request.getSession().getAttributeNames();
            while (attributes.hasMoreElements()) {
                String attribute = (String) attributes.nextElement();
                System.out.println(attribute);
            }*/
            
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
            // now this is thread safe
            int valueGam = 100, valueExp = 100, valueSli = 100;
            try
            {
                valueGam = Integer.parseInt(request.getParameter("valGam").toString());
                valueExp = Integer.parseInt(request.getParameter("valExp").toString());
                valueSli = Integer.parseInt(request.getParameter("valSli").toString());

            } catch (Exception e)
            { 
                valueGam = 100;
                valueExp = 100;
                valueSli = 100;
            }
            
            Integer sk = (Integer)session.getAttribute("streamkey");
            if (sk != null) 
            {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().print((Json.createObjectBuilder().add(
                        "streamKey", sk.toString())//(String)session.getAttribute("streamkey"))
                        .build()
                        .toString()));
                //response.setContentType("text/html");
            }
                    
            synchronized(this) 
            {
                sessionImPro.reset();
                sessionImPro.filterGam(valueSli);
                sessionImPro.filterExp(valueExp);

                //OutputStream out = response.getOutputStream();

                try
                {
                    sessionSocket.sendMsg(sessionImPro.getImage());
                } catch (Exception e){ System.out.println("Socket Error!!");e.printStackTrace();}

                //ImageIO.write(sessionImPro.getImage(), "jpeg", out);
                
                //out.flush();
                //out.close();
            }
	}
}