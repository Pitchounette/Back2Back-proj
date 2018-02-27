package librariesMethods;

import java.io.FileNotFoundException;

import javax.script.*;
import org.renjin.script.*;
import org.renjin.sexp.Vector;

public class LibRenjin {
	
	public ScriptEngine engine;
	public String train;
	public String test;
	
	public void LibRenjn(String test, String train) {
		// create a script engine manager:
		RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
		// create a Renjin engine:
		ScriptEngine engine = factory.getScriptEngine();
		this.engine = engine;
		engine.put("repertoire", train+".csv");
		this.train=train;
		this.test=test;
	}
	
	public double treeAccuracy(String path, String name) throws Exception {
		engine.eval(new java.io.FileReader("src/main/resources/scriptR.R"));

		engine.put("y", "Species");
		engine.put("splitRatio", 0.7);
		engine.put("minBucket", 20);
		Vector accuracyVector = (Vector)engine.eval("arbre_classification(repertoire,y, splitRatio, minBucket)");
		double accuracy = accuracyVector.getElementAsDouble(0);
		return accuracy;
	}
	
	public double randomForestAccuracy() throws Exception {
		engine.eval(new java.io.FileReader("src/main/resources/scriptR.R"));
		engine.put("indY", 19);
		engine.put("ntree", 3);
		engine.put("mtry", 2);
		engine.put("transform", true);
		Vector accuracyVector = (Vector)engine.eval("foret_aléatoire(repertoire, indY, ntree, mtry, transform)");
		double accuracy = accuracyVector.getElementAsDouble(0);
		return accuracy;
	}

}
