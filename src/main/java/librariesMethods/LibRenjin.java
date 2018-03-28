package librariesMethods;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.script.*;
import org.renjin.script.*;
import org.renjin.sexp.Vector;

import libraries.Methode;

public class LibRenjin {
	
	public ScriptEngine engine;
	public String train;
	public String test;
	public String script;
	private static Methode array[]= {Methode.DECISIONTREE, Methode.RANDOMFOREST};
	static public ArrayList<Methode> allowedMethods = new ArrayList<Methode>(Arrays.asList(array)) ;
	
	public LibRenjin(String test, String train, String scriptPath) {
		
		RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
		
		this.engine = factory.getScriptEngine();
		this.train=train;
		this.test=test;
		this.script=scriptPath;
	}
	
	public double treeAccuracy(int indY, int minBucket, boolean transform) throws Exception {
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		this.engine.eval(new java.io.FileReader(script));
		engine.put("repertoireTrain",this.train);
		engine.put("repertoireTest",this.test);
		engine.put("indY",indY);
		engine.put("minBucket", minBucket);
		engine.put("transform", transform);
		Vector accuracyVector = (Vector)engine.eval("arbre_classification(repertoireTrain,repertoireTest,indY, minBucket, transform)");
		return accuracyVector.getElementAsDouble(0);
	}
	
	public double randomForestAccuracy(int indY, int ntree, int mtry, boolean transform) throws Exception {
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		this.engine.eval(new java.io.FileReader(script));
		engine.put("repertoireTrain",this.train);
		engine.put("repertoireTest",this.test);
		engine.put("indY", indY);
		engine.put("ntree", ntree);
		engine.put("mtry", mtry);
		engine.put("transform", transform);
		Vector accuracyVector = (Vector)engine.eval("foret_aleatoire(repertoireTrain,repertoireTest, indY, ntree, mtry, transform)");
		return accuracyVector.getElementAsDouble(0);
	}

	public ArrayList<Methode> getMethode() {
		return allowedMethods;
	}
}

