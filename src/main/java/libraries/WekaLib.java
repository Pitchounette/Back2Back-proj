package libraries;

import librariesMethods.LibWeka;
import tools.SplitCSV;
import weka.classifiers.trees.RandomForest;

public class WekaLib extends Library  {

	
	private LibWeka weka ;
	public WekaLib(SplitCSV data, String methode) {
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
		return (methode == "Arbre");
	}

	@Override
	public double getAccuracy() {
		RandomForest rf;
		try {
			rf = weka.randomForest();
			return weka.accuracy(rf);
		} catch (Exception e) {
			System.out.println("Error during calcul, accuracy = 0");
			e.printStackTrace();
			return 0;
		}

	}

}
