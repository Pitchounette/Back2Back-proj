package libraries;

import java.util.HashMap;
import java.util.Map;

import librariesMethods.LibWeka;
import tools.SplitCSV;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;

public class WekaLib extends Library  {

	
	public LibWeka weka ;

	private Map<String,String> args;
	
	public WekaLib(SplitCSV data, Methode methode) {
		super(data, methode);
		try {
			weka = new LibWeka(data.getTestingPath(),data.getTrainingPath(),false);
			System.out.println(weka);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public WekaLib(SplitCSV data, Methode methode, Map<String,String> args) {
		super(data, methode);
		try {
			weka = new LibWeka(data.getTestingPath(),data.getTrainingPath(),false);
			System.out.println(weka);
			this.args=args;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean isValidMethode() {
		return weka.getMethode().contains(this.methode);
		
	}

	@Override
	public double getAccuracy() {
		double accuracy = 0;
		if(isValidMethode()) {
			if(this.methode.equals(Methode.RANDOMFOREST)) {
				RandomForest rf;
				try { // Faire appel à chaque modèle statistique ici? Qu'en est il de celles qui ont des paramètres?
					rf = weka.randomForest();
					if(!this.args.isEmpty()) {
						int max = Integer.parseInt(args.get("maxDepth"));
						int nbTree = Integer.parseInt(args.get("NumTrees"));
						rf.setMaxDepth(max);
						rf.setNumTrees(nbTree);
					}
					
					accuracy =  weka.accuracy(rf);
				} catch (Exception e) {
					System.out.println("Error during calcul, accuracy = 0");
					e.printStackTrace();
					accuracy =  0;
				}
			}
			else {
				J48 dt;
				try { // Faire appel à chaque modèle statistique ici? Qu'en est il de celles qui ont des paramètres?
					dt = weka.decisionTree();
					if(this.args!=null) {
						int max = Integer.parseInt(args.get("maxDepth"));
						//dt.set;
					}
					accuracy =  weka.accuracy(dt);
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
