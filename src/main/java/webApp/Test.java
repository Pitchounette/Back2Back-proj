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
	
	
	/*  Form attributes  */
	// doGET
	public static final String 
		VUE = "/WEB-INF/ProjetGL.html";

	// general attributes
	public String hasImport;
	public String fileName;

	
	// attributes for library 1
	public String library1;	
	public String methodeLibrary1=null;
	public String maxDepth1;
	public String numTrees1;

	
	// attributes for library 2
	public String library2= null;
	public String methodeLibrary2=null;
	public String maxDepth2=null;
	public String numTrees2= null;

	// FILE
	private String filePath= null;
	
	/*  Java attributes  */
	public SplitCSV split;
	public Library lib1= null;	
	public Library lib2= null;	

	public void doGet( 
			HttpServletRequest request, 
			HttpServletResponse response ) 
			throws ServletException, IOException {
		/* Displays the form */
		this.getServletContext().
			getRequestDispatcher( VUE ).
			forward( request, response );
	}

	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		
		
		hasImport = request.getParameter("import");
		fileName = request.getParameter("file");
		System.out.println(fileName);

		if (hasImport == "noimport") {
			filePath=request.getServletContext().getRealPath("")+"WEB-INF/"+fileName;
		} else {
			filePath=fileName;
		}
		System.out.println(fileName);
		String scriptPath = request.getServletContext().getRealPath("")+"WEB-INF/scriptR.R";
		//filePath=request.getServletContext().getRealPath("")+"WEB-INF/iris.csv";
		split = new SplitCSV(filePath, "test");

		library1= request.getParameter("library1");
		methodeLibrary1=request.getParameter("methodeLibrary1");
		maxDepth1=request.getParameter("maxDepth1");
		numTrees1=request.getParameter("numTrees1");

		library2= request.getParameter("library2");
		methodeLibrary2=request.getParameter("methodeLibrary2");
		maxDepth2=request.getParameter("maxDepth2");
		numTrees2= request.getParameter("numTrees2");

		System.out.println(maxDepth1+" "+maxDepth2+" "+numTrees1+" "+numTrees2);
		
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
		switch (this.library1) {
		case "sparkml": lib1 = calculSparkML(args1);
		break;
		case "renjin": lib1 = calculRenjin(args1,scriptPath);
		break;
		case "weka":lib1 = calculWeka(args1);
		break;
		}
		
		switch (this.library2) {
		case "sparkml": lib2 = calculSparkML(args2);
		break;
		case "renjin": lib2 = calculRenjin(args2,scriptPath);
		break;
		case "weka": lib2 = calculWeka(args2);
		break;
		}
		

		System.out.println(this.lib2+" "+this.lib1);

		request.setAttribute("library1", this.library1);
		request.setAttribute("method1", this.methodeLibrary1);
		request.setAttribute("acc1", this.lib1.getAccuracy());

		request.setAttribute("library2", this.library2);
		request.setAttribute("method2", this.methodeLibrary2);
		request.setAttribute("acc2", this.lib2.getAccuracy());

		getServletContext().getRequestDispatcher("/WEB-INF/bonjour.jsp").
			forward(request, response);
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


	private Library calculRenjin(Map<String,String> args, String scriptPath) throws IOException {
		Library libRF = new RenjinLib(this.split,Methode.RANDOMFOREST,args,scriptPath);
		Library libDT = new RenjinLib(this.split,Methode.DECISIONTREE, args,scriptPath);
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


}