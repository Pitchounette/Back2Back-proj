package webApp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import libraries.Library;
import libraries.Methode;
import libraries.RenjinLib;
import libraries.SparkMLLib;
import libraries.WekaLib;
import tools.Comparateur;
import tools.SplitCSV;

import au.com.bytecode.opencsv.*;

@WebServlet("/Test")
public class Test extends HttpServlet {
	public static final String VUE = "/WEB-INF/ProjetGL.html";
	
	public String hasImport;
	public String fileName;
	
	public String library1;	
	public String methodeLibrary1=null;
	public String maxDepth1=null;
	public String numTrees1=null;
	
	public String library2= null;
	public String methodeLibrary2=null;
	public String maxDepth2=null;
	public String numTrees2= null;

	
	/* FILE */
	private boolean isMultipart;
	private String filePath= null;
	private int maxFileSize = 50 * 1024;
	private int maxMemSize = 4 * 1024;
	private File file ;
	public SplitCSV split;
	
	public Library lib1= null;	
	public Library lib2= null;	

	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		/* Affichage de la page d'inscription */
		this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
	}


	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		String hasImport = request.getParameter("import");
		System.out.println(hasImport);
		filePath=request.getServletContext().getRealPath("")+"/WEB-INF/iris.csv";
		split = new SplitCSV(filePath, "test");
		
		library1= request.getParameter("library1");
		methodeLibrary1=request.getParameter("methodeLibrary1");
		maxDepth1=request.getParameter("maxDepth1");
		numTrees1=request.getParameter("numTrees1");
		
		library2= request.getParameter("library2");
		methodeLibrary2=request.getParameter("methodeLibrary2");
		maxDepth2=request.getParameter("maxDepth2");
		numTrees2= request.getParameter("numTrees2");
		
		
		
		//HashMap arguments1 = new HashMap<String,String>();
		//arguments1.put("indY", "19");
		
		//HashMap arguments2 = new HashMap<String,String>();
		System.out.println("je suis allée jusque là");
		switch (this.library1) {
		case "sparkml": calculSparkML(lib1);
		case "renjin": calculRenjin(lib1);
		case "weka": calculWeka(lib1);
		}
		
		switch (this.library2) {
		case "sparkml": calculSparkML(lib2);
		case "renjin": calculRenjin(lib2);
		case "weka": calculWeka(lib2);
		}
		
		request.setAttribute("lib1", this.library1);
		request.setAttribute("method1", this.methodeLibrary1);
		request.setAttribute("acc1", this.lib1.getAccuracy());
		
		request.setAttribute("lib2", this.library2);
		request.setAttribute("method2", this.methodeLibrary2);
		request.setAttribute("acc2", this.lib2.getAccuracy());
		
		getServletContext().getRequestDispatcher("/WEB-INF/bonjour.jsp").forward(request, response);

	}

	private void calculWeka(Library lib) {
		switch (this.methodeLibrary1) {
		case "randomForest": lib = new WekaLib(this.split,Methode.RANDOMFOREST);
		case "decisionTree": lib = new WekaLib(this.split,Methode.DECISIONTREE);
		case "J48": lib = new WekaLib(this.split,Methode.SVM);
		}
	}


	private void calculRenjin(Library lib) {
		switch (this.methodeLibrary1) {
		case "randomForest": lib = new RenjinLib(this.split,Methode.RANDOMFOREST);
		case "decisionTree": lib = new RenjinLib(this.split,Methode.DECISIONTREE);
		}
	}


	private void calculSparkML(Library lib) throws IOException {
		switch (this.methodeLibrary1) {
		case "randomForest": lib = new SparkMLLib(this.split,Methode.RANDOMFOREST);
		case "decisionTree": lib = new SparkMLLib(this.split,Methode.DECISIONTREE);
		}
		
	}


	public void getFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		filePath = getServletContext().getInitParameter("file-upload");
		isMultipart = ServletFileUpload.isMultipartContent(request);
		response.setContentType("text/html");
		java.io.PrintWriter out = response.getWriter( );

		if( !isMultipart ) {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");  
			out.println("</head>");
			out.println("<body>");
			out.println("<p>No file uploaded</p>"); 
			out.println("</body>");
			out.println("</html>");
			return;
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();

		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File("c:\\temp"));
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax( maxFileSize );
		try { 
			// Parse the request to get file items.
			List fileItems = upload.parseRequest((RequestContext) request);

			// Process the uploaded file items
			Iterator i = fileItems.iterator();

			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");  
			out.println("</head>");
			out.println("<body>");

			while ( i.hasNext () ) {
				FileItem fi = (FileItem)i.next();
				if ( !fi.isFormField () ) {
					// Get the uploaded file parameters
					String fieldName = fi.getFieldName();
					String fileName = fi.getName();
					String contentType = fi.getContentType();
					boolean isInMemory = fi.isInMemory();
					long sizeInBytes = fi.getSize();

					// Write the file
					if( fileName.lastIndexOf("\\") >= 0 ) {
						file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
					} else {
						file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
					}
					fi.write( file ) ;
					out.println("Uploaded Filename: " + fileName + "<br>");
				}
			}
			out.println("</body>");
			out.println("</html>");
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}


	private double doComparaison(String proportion, String librairie, String methode, String arbres,int moyenne) {
		double accuracy = 0;
		for(int i=1;i<=moyenne;i++) {
			/*avoir la proportion entre 0 et 1*/
			double propApp = Double.parseDouble(proportion)/100.0;
			/*avoir un nombre d'arbres = 10 au cas où il y aurait un problème dans la suite*/
			int nbtree=10;
			if(!arbres.equals("")) {
				nbtree=Integer.parseInt(arbres);
			}
			//Library l = new Library(this.path, this.vary, propApp, librairie.substring(0, 1).toLowerCase(), methode.substring(0, 1).toLowerCase(), nbtree);
			//accuracy = accuracy + l.run();
		}
		accuracy=accuracy/moyenne;
		accuracy=Math.round(accuracy*1000)/1000.0;
		return(accuracy);
	}

}