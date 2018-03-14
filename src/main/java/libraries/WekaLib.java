package libraries;

import librariesMethods.LibWeka;
import tools.SplitCSV;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;

public class WekaLib extends Library  {

	
	private LibWeka weka ;
	public WekaLib(SplitCSV data, Methode methode) {
		super(data, methode);
		try {
			weka = new LibWeka(data.getTestingPath(),data.getTrainingPath(),false);
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
