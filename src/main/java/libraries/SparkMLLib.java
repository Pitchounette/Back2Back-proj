package libraries;

import tools.SplitCSV;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import librariesMethods.AlgoSparkML;

public class SparkMLLib extends Library{
	private AlgoSparkML sparkMl;
	private ArrayList<String> categories ;
	static private String[] array = {"DecisionTree","RandomForest"};
	static public ArrayList<String> allowedMethods = new ArrayList<String>(Arrays.asList(array)) ;
	
	public SparkMLLib(SplitCSV data, String methode) throws IOException {
		super(data, methode);
		
		categories = new ArrayList<String>();
		
		// Chemin des fichiers dédié à SparkMl
		String testFile = "src/main/resources/test_spark.csv";
		String trainFile = "src/main/resources/train_spark.csv";
		
		/* On va modifier les tables csv pour convenir au format Pour test et train notamment transformer la variable catégoriel d'interet en 1.0,2.0,3.0*/
		
		transformLastColumn(data.getTestingPath(),testFile);
		transformLastColumn(data.getTrainingPath(),trainFile);
		
		sparkMl = new AlgoSparkML(testFile,trainFile);
		
		
	}

	// Write a new csv file that contain the same information that the one at pathIn but change the last colum
	// By transforming the categorial variable into 1.0,2.0,3.0 ...
	
	private void transformLastColumn(String pathIn, String pathOut) throws IOException {
		File file = new File(pathIn);
		List<String[]> dataTable = new ArrayList<String[]>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line = null; 
		while ((line = reader.readLine()) != null) { 
			String[] values = line.split(",");
			dataTable.add(values); 
		}
		
		// Apres avoir charger le CSV on le modifie
		
		
		String[] header = dataTable.remove(0);
		
		this.findCategories(dataTable); // We are looking for the categories Y
		for(int i=0;i<dataTable.size();i++) {


			dataTable.get(i)[dataTable.get(i).length-1] = String.valueOf((double) 1+categories.indexOf(dataTable.get(i)[dataTable.get(i).length-1]));

		}
		
		
		CSVWriter writerTest = new CSVWriter(new FileWriter(pathOut));
        writerTest.writeAll(dataTable);
        writerTest.close();
		
	}

	
	
	private void findCategories(List<String[]> dataTable) {
		// On stocke toutes les catégories possibles de la variable d'interet
		int lastCol = dataTable.get(0).length -1;
		for(int i=0; i < dataTable.size();i++) {
			if(!categories.contains(dataTable.get(i)[lastCol])) {
				categories.add(dataTable.get(i)[lastCol]);
			}
		}
		
	}

	// Renvoie true si la méthode est bien disponible pour la librairi spark ML
	public boolean isValidMethode() {
		
		return (SparkMLLib.allowedMethods.contains(this.methode) );
	}

	// Renvoie la valeur de l'accuracy pour la méthode souhaitée si celle ci est disponible renvoie 0 sinon
	public double getAccuracy() {
		double accuracy = 0;
		if(isValidMethode()) {
			accuracy = this.getResult();
		}
		else {
			System.out.println("Not a valid Method");
		}
		return accuracy;
	}

	// Renvoie la valeur de l'accuracy pour la méthode choisie
	private double getResult() {
		if(this.methode.equals("DecisionTree")) {
			return this.sparkMl.getResultTree();
		}
		if(this.methode.equals("RandomForest")) {
			return this.sparkMl.getResultRandomForest();
		}
		return 0;
	}

}
