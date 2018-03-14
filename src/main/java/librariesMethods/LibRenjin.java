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
	private double accuracy;
	private static Methode array[]= {Methode.DECISIONTREE, Methode.RANDOMFOREST};
	static public ArrayList<Methode> allowedMethods = new ArrayList<Methode>(Arrays.asList(array)) ;
	
	public LibRenjin(String test, String train) {
		
		RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
		
		this.engine = factory.getScriptEngine();
		this.train=train;
		this.test=test;
	}
	
	public void treeAccuracy(int indY, int minBucket) throws Exception {
		engine.put("repertoireTrain",this.train);
		engine.put("repertoireTest",this.test);
		engine.put("indY",indY);
		engine.put("minBucket", minBucket);
		Vector accuracyVector = (Vector)engine.eval("arbre_classification(repertoireTrain,repertoireTest,y, splitRatio, minBucket)");
		this.accuracy = accuracyVector.getElementAsDouble(0);
	}
	
	public void randomForestAccuracy(int indY, int ntree, int mtry, boolean transform) throws Exception {
		engine.put("repertoireTrain",this.train);
		engine.put("repertoireTest",this.test);
		engine.put("indY", indY);
		engine.put("ntree", ntree);
		engine.put("mtry", mtry);
		engine.put("transform", transform);
		Vector accuracyVector = (Vector)engine.eval("foret_aléatoire(repertoireTrain,repertoireTest, indY, ntree, mtry, transform)");
		this.accuracy = accuracyVector.getElementAsDouble(0);
	}
	
	public double getAccuracy() {
		return this.accuracy;
	}
	
	public ArrayList<Methode> getMethode() {
		return allowedMethods;
	}
}

