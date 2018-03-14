package libraries;

import librariesMethods.LibRenjin;
import librariesMethods.LibWeka;
import tools.SplitCSV;
import weka.classifiers.trees.RandomForest;

public class RenjinLib extends Library {
	
	private LibRenjin renjin ;
	public RenjinLib(SplitCSV data, Methode methode) {
		super(data, methode);
		try {
			renjin = new LibRenjin(data.getTestingPath(),data.getTrainingPath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public boolean isValidMethode() {
		return renjin.getMethode().contains(this.methode);

	}
	@Override
	public double getAccuracy() {
		try {
			return this.renjin.getAccuracy();
		} catch (Exception e) {
			System.out.println("Error during calcul, accuracy = 0");
			e.printStackTrace();
			return 0;
		}
	}
}
