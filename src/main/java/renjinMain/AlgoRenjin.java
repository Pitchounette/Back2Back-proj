package renjinMain;
import javax.script.*;
import org.renjin.script.*;


public class AlgoRenjin {
	public static void main(String[] args) throws Exception {
	// create a script engine manager:
	RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	// create a Renjin engine:
	ScriptEngine engine = factory.getScriptEngine();
	
	engine.eval(new java.io.FileReader("/home/armel/Documents/Génie logiciel/scriptR.R"));
	
	}

}
