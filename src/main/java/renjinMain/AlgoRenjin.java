package renjinMain;

import javax.script.*;
import org.renjin.script.*;
import org.renjin.sexp.Vector;


public class AlgoRenjin {
	public static void main(String[] args) throws Exception {
	// create a script engine manager:
	RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	// create a Renjin engine:
	ScriptEngine engine = factory.getScriptEngine();
	
	engine.eval(new java.io.FileReader("src/main/resources/scriptR.R"));
	
	
	// arbre de classification
	
	engine.put("repertoire", "src/main/resources/statsFSEVary.csv");
	engine.put("indY", 19);
	engine.put("splitRatio", 0.7);
	engine.put("minBucket", 200);
	engine.put("transform", true);
	Vector accuracyVector = (Vector)engine.eval("arbre_classification(repertoire,indY, splitRatio, minBucket,transform)");
	double accuracy = accuracyVector.getElementAsDouble(0);
	System.out.println(accuracy);
	
	
	//Random Forest
	
	engine.put("repertoire", "src/main/resources/statsFSEVary.csv");
	engine.put("indY", 19);
	engine.put("ntree", 3);
	engine.put("mtry", 2);
	engine.put("transform", true);
	accuracyVector = (Vector)engine.eval("foret_aléatoire(repertoire, indY, ntree, mtry, transform)");
	accuracy = accuracyVector.getElementAsDouble(0);
	System.out.println(accuracy);
	
	

	
	}

}
