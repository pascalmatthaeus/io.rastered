package io.rastered.app.service;

import io.rastered.app.core.Presets;
import io.rastered.app.core.Texture;
import io.rastered.app.core.FilterParameters;
import jakarta.json.*;
import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.io.File;
import java.util.Arrays;
//import java.util.Enumeration;

// improve how session parameters are handled, consider filter descriptions
// (e.g. sub-package with abstract class ImageFilterFunction( FilterBehaviour f, 
// String description ) or local path on server or other source 
// (e.g. another server). Putting dead code inside a separate file is
// one step towards making source more readable.

public class FilterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
        
        // Request parameters removed here for thread safety.
        // Fields declared here are not thread safe, single shared instance.
        protected String pathToWeb;
        protected volatile Texture testBitmap;
        
        @Override
        public void init() throws ServletException
        {
            ImageIO.setUseCache(false);
            pathToWeb = getServletContext().getRealPath(File.separator);
            try 
            {
                testBitmap = new Texture( ImageIO.read(
                    new File("/home/sk/Pictures/lenna.bmp")
                ));
            } catch (Exception e) { e.printStackTrace(); }
        }
        
        @Override
        protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException 
        {
            // keep shareable instances inside ctx scope
            
            HttpSession session = request.getSession();
            
            // Debug: List all attributes for current session.
            /*Enumeration<String> attributes = request.getSession()
                .getAttributeNames();
            while (attributes.hasMoreElements()) {
                System.out.println((String) attributes.nextElement());
            }*/
            
            var sessionTexture = 
                (Texture)session.getAttribute("texture");
            var sessionSocket = 
                (SocketClient)session.getAttribute("videosocket");
            
            if ( sessionSocket == null )
            {
                try
                {
                    sessionSocket = new SocketClient("127.0.0.1",6679);
                    session.setAttribute("videosocket", sessionSocket);
                    int streamKey = (int) System.currentTimeMillis();
                    session.setAttribute("streamkey", streamKey);
                    sessionSocket.sendStreamKey(streamKey);
                } catch (Exception e) { sessionSocket=null; }
            }
            
            if ( sessionTexture == null )
            {
                System.out.println("New Session created.");
                try
                {
                    sessionTexture = (Texture)testBitmap.clone();
                } catch (Exception e) { e.printStackTrace(); }
                session.setAttribute("texture", sessionTexture);
            }
            
            // Parse request parameters:
            /*valueGam = Integer.parseInt(
                request.getParameter("valGam").toString()
            );*/
            
            int [] paramList;
            int [] paramFetch = new FilterParameters(request).get();
            if (paramFetch == null)
                paramList = new int[] {100,100,100};
            else
                paramList = paramFetch;
            
            var sk = (Integer)session.getAttribute("streamkey");
            /*if (sk != null) 
            {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().print((Json.createObjectBuilder().add(
                        "streamKey", sk.toString())
                        .build()
                        .toString()));
            }*/
            
            // testing here...
            var jsonResponseBuilder = Json.createObjectBuilder();
            if (sk != null) {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                jsonResponseBuilder.add("streamKey", sk.toString());
            }
            Arrays.stream(Presets.values())
                .forEach( (filterEnum) -> { 
                        jsonResponseBuilder.add(
                            filterEnum.name(),filterEnum.getUserFacingName()
                        );
                    }
                );
            
            response.getWriter().print(
                    jsonResponseBuilder.build().toString()
                );
            synchronized(sessionTexture) { synchronized(sessionSocket) {
                sessionTexture.sampleFrom(sessionTexture.getOriginalData());
                boolean allArgsGiven = paramList.length == 3;
                float paramGamma = allArgsGiven ? paramList[0] : 100.0f;
                float paramExposure = allArgsGiven ? paramList[1] : 100.0f;
                float paramSharpness = allArgsGiven ? paramList[2] : 100.0f;

                sessionTexture
                    .filter(Presets.GAMMA, paramGamma)
                    .filter(Presets.EXPOSURE,paramExposure)
                    .filter(Presets.SHARPNESS,paramSharpness);

                try
                {
                    sessionSocket.sendTextureResolution( 
                        sessionTexture.getWidth(), sessionTexture.getHeight()
                    );
                    sessionSocket.sendFrame(sessionTexture.getData());
                } catch (Exception e)
                { 
                    System.out.println(
                        "Could not send frame to io.rastered.video!"
                    );
                    e.printStackTrace();
                }
            } }
	}
}