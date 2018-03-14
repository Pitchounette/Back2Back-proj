package libraries;

import java.util.HashMap;
import java.util.Map;

import librariesMethods.LibRenjin;
import librariesMethods.LibWeka;
import tools.SplitCSV;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;

public class RenjinLib extends Library {
	
	private LibRenjin renjin ;
	private Map<String,String> usedArgs;
	private Map<String,String> args;
	
	public RenjinLib(SplitCSV data, Methode methode, Map<String,String> args) {
		super(data, methode);
		try {
			renjin = new LibRenjin(data.getTestingPath(),data.getTrainingPath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.args=args;
		usedArgs = new HashMap<String,String>();
		usedArgs.put("ntree", "200");
		usedArgs.put("mtry", "4");
		usedArgs.put("transform", "true");
		usedArgs.put("minbucket", "10");
	}
	public RenjinLib(SplitCSV data, Methode methode) {
		super(data, methode);
		try {
			renjin = new LibRenjin(data.getTestingPath(),data.getTrainingPath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.args= new HashMap<String,String>();
		usedArgs = new HashMap<String,String>();
		usedArgs.put("ntree", "200");
		usedArgs.put("mtry", "4");
		usedArgs.put("transform", "true");
		usedArgs.put("minbucket", "10");
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
					accuracy =  this.renjin.treeAccuracy((int) Integer.parseInt(this.args.get("indY")), (int) Integer.parseInt(this.usedArgs.get("minbucket")));
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
