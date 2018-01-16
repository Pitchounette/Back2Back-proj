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
	
	engine.put("repertoire", "src/main/resources/statsFSEVary.csv");
	engine.put("y", 19);
	engine.put("splitRatio", 0.7);
	engine.put("minBucket", 20);
	Vector accuracyVector = (Vector)engine.eval("arbre_classification(repertoire,y, splitRatio, minBucket)");
	double accuracy = accuracyVector.getElementAsDouble(0);
	System.out.println(accuracy);

	
	}

}
