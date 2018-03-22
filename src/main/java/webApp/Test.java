package webApp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="FrancoisTest")

public class Test extends HttpServlet {
    private static final long serialVersionUID = 1L;
       

    public Test() {
        super();
        // TODO Auto-generated constructor stub
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// TODO Auto-generated constructor stub
    }


 
    	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		String choix = request.getParameter("library1");
    		response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<HTML>\n<BODY>\n" +
					"<H1>Yo</H1>\n" +
					"<p>Le choix est "+choix+".</p>" +                
					"</BODY></HTML>");
    		// ajouter l'import d'un fichier 
    		
    		

    	/*	}else {
    			response.setContentType("text/html");
    			PrintWriter out = response.getWriter();
    			out.println("<HTML>\n<BODY>\n" +
    					"<H1>L'import a échoué</H1>\n" +
    					"<p>L'extension du fichier"+end+" n'est pas autorisé.</p>" +                
    					"</BODY></HTML>");
    		}*/

    }

}