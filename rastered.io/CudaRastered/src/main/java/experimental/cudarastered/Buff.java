package experimental.cudarastered;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;

public class Buff extends HttpServlet {

	private static final long serialVersionUID = 1L;
        
        static SocketClient client;
        CudaCtl cctl;
        
        @Override
        public void init()
        {
            this.getServletContext().setAttribute("cctl", new CudaCtl(Filters.boxBlur));
            System.out.println("Hallo.");
            String pathToWeb = getServletContext().getRealPath(File.separator);
            client = new SocketClient();
        }
        
        @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
                int valueGam = 100;
                int valueExp = 100;
                int valueRange = 100;
                try{
                    valueGam = Integer.parseInt(request.getParameter("valGam").toString());
                    valueExp = Integer.parseInt(request.getParameter("valExp").toString());
                    valueRange = Integer.parseInt(request.getParameter("valRange").toString());
                }catch(Exception e){
                valueGam = 100;
                valueGam = 100;
                valueRange = 100;
                }
                
		response.setContentType("image/jpeg");
                
		OutputStream out = response.getOutputStream();
                
                ((CudaCtl)getServletConfig().getServletContext().getAttribute("cctl")).filter(valueRange);
                
                try 
                {
                    client.startConnection("127.0.0.1", 6679);
                    client.sendMsg(((CudaCtl)this.getServletConfig().getServletContext().getAttribute("cctl")).api.image.stream);
                    client.stopConnection();
                } catch (Exception e) {System.out.println("Socket Error!!");}
                
		ImageIO.write(((CudaCtl)this.getServletConfig().getServletContext().getAttribute("cctl")).api.image.stream, "jpg", out);
		out.close();
	}

}