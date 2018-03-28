package libraries;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import librariesMethods.LibRenjin;
import librariesMethods.LibWeka;
import tools.SplitCSV;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.converters.CSVLoader;

public class RenjinLib extends Library {
	
	private LibRenjin renjin ;
	private Map<String,String> usedArgs;
	private Map<String,String> args;
	
	public RenjinLib(SplitCSV data, Methode methode, Map<String,String> args,String scriptPath) throws IOException {
		super(data, methode);
		try {
			renjin = new LibRenjin(data.getTestingPath(),data.getTrainingPath(),scriptPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.args=args;
		File fileTest = new File(data.getTestingPath());
		CSVLoader loaderTest = new CSVLoader();
		loaderTest.setSource(fileTest);
		int Y = loaderTest.getDataSet().numAttributes() - 1;
		args.put("indY", String.valueOf(Y));
		usedArgs = new HashMap<String,String>();
		usedArgs.put("ntree", "200");
		usedArgs.put("mtry", "4");
		usedArgs.put("transform", "true");
		usedArgs.put("minbucket", "10");
	}
	public RenjinLib(SplitCSV data, Methode methode, String scriptPath) throws IOException {
		this(data, methode, new HashMap<String,String>(),scriptPath);
	}
	
	@Override
	public boolean isValidMethode() {
		return renjin.getMethode().contains(this.methode);

	}
	
	public void checkArgs(){
		for(Map.Entry<String,String> argsTest: this.args.entrySet()) {
			if(this.usedArgs.containsKey(argsTest.getKey())){
				this.usedArgs.replace(argsTest.getKey(),argsTest.getValue());
			}
		}
	}
	@Override
 	public double getAccuracy() {
		double accuracy = 0;
		checkArgs();
		if(isValidMethode()) {
			if(this.methode.equals(Methode.RANDOMFOREST)) {
				try { // Faire appel à chaque modèle statistique ici? Qu'en est il de celles qui ont des paramètres?
					accuracy =  this.renjin.randomForestAccuracy((int) Integer.parseInt(this.args.get("indY")), (int) Integer.parseInt(this.usedArgs.get("ntree")),
							(int) Integer.parseInt(this.usedArgs.get("mtry")),(boolean) Boolean.parseBoolean((this.usedArgs.get("transform"))));
				} catch (Exception e) {
					System.out.println("Error during calcul, accuracy = 0");
					e.printStackTrace();
					accuracy =  0;
				}
			}
			else {
				try { // Faire appel à chaque modèle statistique ici? Qu'en est il de celles qui ont des paramètres?
					accuracy =  this.renjin.treeAccuracy((int) Integer.parseInt(this.args.get("indY")), (int) Integer.parseInt(this.usedArgs.get("minbucket")),
							(boolean) Boolean.parseBoolean((this.usedArgs.get("transform"))));
				} catch (Exception e) {
					System.out.println("Error during calcul, accuracy = 0");
					e.printStackTrace();
					accuracy =  0;
				}
				
			}
		}
		
		return accuracy;
	}
}
