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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVWriter;
import librariesMethods.AlgoSparkML;

public class SparkMLLib extends Library{
	private AlgoSparkML sparkMl;
	private HashMap<Integer,ArrayList<String>> categories ;
	private boolean categoriesCreated = false;
	private Map<String,String> args;
	static private Methode[] array = {Methode.DECISIONTREE,Methode.RANDOMFOREST,Methode.SVM};
	static public ArrayList<Methode> allowedMethods = new ArrayList<Methode>(Arrays.asList(array)) ;

	public SparkMLLib(SplitCSV data, Methode methode) throws IOException {
		super(data, methode);
		args = new HashMap<String,String>();
		categories = new HashMap<Integer,ArrayList<String>>();

		// Chemin des fichiers dédié à SparkMl
		String testFile = "src/main/resources/test_spark.csv";
		String trainFile = "src/main/resources/train_spark.csv";

		/* On va modifier les tables csv pour convenir au format Pour test et train notamment transformer la variable catégoriel d'interet en 1.0,2.0,3.0*/

		transformColumn(data.getTestingPath(),testFile);
		transformColumn(data.getTrainingPath(),trainFile);

		sparkMl = new AlgoSparkML(testFile,trainFile,categories.size());


	}
	
	public SparkMLLib(SplitCSV data, Methode methode,Map<String,String> args) throws IOException {
		super(data, methode);
		this.args = args;
		categories = new HashMap<Integer,ArrayList<String>>();
		
		// Chemin des fichiers dédié à SparkMl
		String testFile = "src/main/resources/test_spark.csv";
		String trainFile = "src/main/resources/train_spark.csv";

		/* On va modifier les tables csv pour convenir au format Pour test et train notamment transformer la variable catégoriel d'interet en 1.0,2.0,3.0*/

		transformColumn(data.getTestingPath(),testFile);
		transformColumn(data.getTrainingPath(),trainFile);

		sparkMl = new AlgoSparkML(testFile,trainFile,categories.size());


	}
	

	// Write a new csv file that contain the same information that the one at pathIn but change the last colum
	// By transforming the categorial variable into 1.0,2.0,3.0 ...

	private void transformColumn(String pathIn, String pathOut) throws IOException {
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
		createCategories(header.length);
		for(int i =0; i < header.length;i++) {
			this.findCategories(dataTable,i); // We are looking for the categories Y
			
		}
		
		for(int col =0; col< header.length;col++) {
			if(categories.get(col).size() < 4) {
				for(int i=0;i<dataTable.size();i++) {
						dataTable.get(i)[col] = String.valueOf((double) 1+categories.get(col).indexOf(dataTable.get(i)[col]));
				}
			}
		}


		CSVWriter writerTest = new CSVWriter(new FileWriter(pathOut));
		writerTest.writeAll(dataTable);
		writerTest.close();

	}



	private void createCategories(int length) {
		if(!categoriesCreated){
			for(int i = 0;i < length;i++) {
				categories.put(i,new ArrayList<String>());
			}
			categoriesCreated = true;
		}
		
	}
	private void findCategories(List<String[]> dataTable,int col) {
		// On stocke toutes les catégories possibles de la variable d'interet

		for(int i=0; i < dataTable.size();i++) {
			if(!categories.get(col).contains(dataTable.get(i)[col])) {
				categories.get(col).add(dataTable.get(i)[col]);
			}
		}

	}

	// Renvoie true si la méthode est bien disponible pour la librairi spark ML
	public boolean isValidMethode() {
		// Amodifier ?
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
		double res = 0.0;

		if(this.methode.equals(Methode.DECISIONTREE)) {

			res = this.decisionTree();
		}
		else if(this.methode.equals(Methode.RANDOMFOREST)) {
			res = this.randomForest();
		}
		else if(this.methode.equals(Methode.SVM)) {
			res =  this.sparkMl.getResultSVM();
		}

		return res;

	}
	
	private double decisionTree() {
		String impurity = "gini";
		int maxDepth = 10; // Max depth of the tree
		
		if(args.containsKey("impurity")) {
			impurity = args.get("impurity");
		}
		if(args.containsKey("maxDepth")) {
			maxDepth = Integer.valueOf(args.get("maxDepth"));
		}
		
		return this.sparkMl.getResultTree(impurity, maxDepth);
	}
	
	private double randomForest() {
		String impurity = "gini";
		int maxDepth = 20;
		int numTrees = 100;
		
		if(args.containsKey("impurity")) {
			impurity = args.get("impurity");
		}
		if(args.containsKey("maxDepth")) {
			maxDepth = Integer.valueOf(args.get("maxDepth"));
		}
		if(args.containsKey("numTrees")) {
			numTrees = Integer.valueOf(args.get("numTrees"));
		}
		
		
		return this.sparkMl.getResultRandomForest(numTrees,impurity,maxDepth);
	}

}
