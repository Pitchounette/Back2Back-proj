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
	public String maxDepth1;
	public String numTrees1;

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
		
		
		hasImport = request.getParameter("import");
		fileName = request.getParameter("file");
		

		if (hasImport == "noimport") {
			filePath=request.getServletContext().getRealPath("")+"WEB-INF/"+fileName;
		} else {
			System.out.println(fileName);
		}
		
		split = new SplitCSV(filePath, "test");

		library1= request.getParameter("library1");
		methodeLibrary1=request.getParameter("methodeLibrary1");
		maxDepth1=request.getParameter("maxDepth1");
		numTrees1=request.getParameter("numTrees1");

		library2= request.getParameter("library2");
		methodeLibrary2=request.getParameter("methodeLibrary2");
		maxDepth2=request.getParameter("maxDepth2");
		numTrees2= request.getParameter("numTrees2");

		Map<String,String> args1= new HashMap<String,String>();
		Map<String,String> args2= new HashMap<String,String>();
		if (maxDepth1 != null) {
			args1.put("maxDepth", maxDepth1);
		}
		if (maxDepth2 != null) {
			args2.put("maxDepth", maxDepth2);
		}
		if (numTrees1 != null) {
			args1.put("numTrees", numTrees1);
		}
		if (maxDepth2 != null) {
			args2.put("numTrees", numTrees2);
		}
		//HashMap arguments1 = new HashMap<String,String>();
		//arguments1.put("indY", "19");

		//HashMap arguments2 = new HashMap<String,String>();
		System.out.println(this.library1);
		switch (this.library1) {
		case "sparkml": lib1 = calculSparkML(args1);
		break;
		case "renjin": lib1 = calculRenjin(args1);
		break;
		case "weka":lib1 = calculWeka(args1);
		break;
		}
		
		System.out.println("je suis allée jusque là 1");
		switch (this.library2) {
		case "sparkml": lib2 = calculSparkML(args2);
		break;
		case "renjin": lib2 = calculRenjin(args2);
		break;
		case "weka": lib2 = calculWeka(args2);
		break;
		}
		

		System.out.println(this.lib2.getAccuracy());

		request.setAttribute("lib1", this.lib1);
		request.setAttribute("method1", this.methodeLibrary1);
		request.setAttribute("acc1", this.lib1.getAccuracy());

		request.setAttribute("lib2", this.lib2);
		request.setAttribute("method2", this.methodeLibrary2);
		request.setAttribute("acc2", this.lib2.getAccuracy());

		getServletContext().getRequestDispatcher("/WEB-INF/bonjour.jsp").forward(request, response);
	}

	private Library calculWeka(Map<String,String> args) {
		Library libRF = new WekaLib(this.split,Methode.RANDOMFOREST,args);
		Library libDT = new WekaLib(this.split,Methode.DECISIONTREE,args);
		switch (this.methodeLibrary1) {
		case "randomForest": return libRF;
		case "decisionTree": return libDT;
		default : return null ;
		}
	}


	private Library calculRenjin(Map<String,String> args) {
		Library libRF = new RenjinLib(this.split,Methode.RANDOMFOREST,args);
		Library libDT = new RenjinLib(this.split,Methode.DECISIONTREE, args);
		switch (this.methodeLibrary1) {
		case "randomForest": return libRF;
		case "decisionTree": return libDT;
		default : return null ;
		}
	}


	private Library calculSparkML(Map<String,String> args) throws IOException {
		Library libRF = new SparkMLLib(this.split,Methode.RANDOMFOREST, args);
		Library libDT = new SparkMLLib(this.split,Methode.DECISIONTREE, args);
		switch (this.methodeLibrary1) {
		case "randomForest": return libRF;
		case "decisionTree": return libDT;
		default : return null;
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

}