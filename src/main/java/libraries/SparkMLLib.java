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
	private AlgoSparkML sparkMl; // L'instance chargée de faire les calculs statistiques
	private HashMap<Integer,ArrayList<String>> categories ; // Hashmap dont la clef correspond au numero de la colone, et les values sont la liste des différentes valeurs (unique) dans la colonne
	private boolean categoriesCreated = false; // Vaut true si le hashMap categories à déja été initialisé
	private Map<String,String> args; // Contient en clef le nom du paramétres que souhaite changé l'utilisateur et en value la valeur qu'il faut donnée à ce paramétre
	static private Methode[] array = {Methode.DECISIONTREE,Methode.RANDOMFOREST,Methode.SVM}; // Contient la liste des méthodes disponible pour sparkML
	static public ArrayList<Methode> allowedMethods = new ArrayList<Methode>(Arrays.asList(array)) ; // Contient la liste des méthodes disponible pour sparkML

	public SparkMLLib(SplitCSV data, Methode methode) throws IOException {
		this(data,methode,new HashMap<String,String>());
	}
	
	// Constructeur de la classe pour le cas ou l'on passe en argument un ensemble de parametres à utiliser pour la méthode
	public SparkMLLib(SplitCSV data, Methode methode,Map<String,String> args) throws IOException {
		super(data, methode);
		this.args = args;
		categories = new HashMap<Integer,ArrayList<String>>();
		
		// Chemin des fichiers dédié à SparkMl
		
		String testFile = data.getTestingPath();
		String trainFile = data.getTrainingPath();
		String newtestFile = testFile.substring(0, testFile.length()-4)+"_test_spark.csv";
		String newtrainFile = trainFile.substring(0, trainFile.length()-4)+"_train_spark.csv";
		/* On va modifier les tables csv pour convenir au format Pour test et train notamment transformer la variable catégoriel d'interet en 1.0,2.0,3.0*/

		transformColumn(trainFile,newtrainFile);
		transformColumn(testFile,newtestFile);

		// On cree une instance de sparkML 
		sparkMl = new AlgoSparkML(testFile,trainFile,categories.size());
	}
	
	// Write a new csv file that contain the same information that the one at pathIn but change the column where there is qualitative variables
	// By transforming the modalites into 1.0,2.0,3.0 ...

	private void transformColumn(String pathIn, String pathOut) throws IOException {
		// Import the dataset at path IN
		File file = new File(pathIn);
		List<String[]> dataTable = new ArrayList<String[]>();
		BufferedReader reader = new BufferedReader(new FileReader(file));

		String line = null; 
		while ((line = reader.readLine()) != null) { 
			String[] values = line.split(",");
			dataTable.add(values); 
		}
		// Apres avoir charger le CSV on le modifie

		// Stock all the unique  value of each column in the dataset
		String[] header = dataTable.remove(0);
		createCategories(header.length); // Create the map in which we will put all the unique value
		for(int i =0; i < header.length;i++) { // For each column add the unique value into the categories map
			this.findCategories(dataTable,i); //
		}
		
		// For each column, if it has less than 4 differents unique value, we assume it is qualitative and transform the column 
		for(int col =0; col< header.length;col++) {
			if(categories.get(col).size() < 4) {
				for(int i=0;i<dataTable.size();i++) {
						dataTable.get(i)[col] = String.valueOf((double) 1+categories.get(col).indexOf(dataTable.get(i)[col])); // Replace the value by the index of the value into the list of the different value of the column
				}
			}
		}

		// Write the new CSV file in the pathOut
		CSVWriter writerTest = new CSVWriter(new FileWriter(pathOut));
		writerTest.writeAll(dataTable);
		writerTest.close();
	}


	// Initiate the categories map by creating a ArrayList for each column in the dataset
	private void createCategories(int length) {
		if(!categoriesCreated){
			for(int i = 0;i < length;i++) {
				categories.put(i,new ArrayList<String>());
			}
			categoriesCreated = true;
		}
		
	}
	
	// Find and stock all the differents value in the dataset at the column col
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
	
	// Return the accuracy of the decision Tree model, and test if any arguments has been given
	private double decisionTree() {
		
		// Default arguments for the model
		String impurity = "gini";
		int maxDepth = 10; // Max depth of the tree
		
		// Test if any arguments has been given
		if(args.containsKey("impurity")) {
			impurity = args.get("impurity");
		}
		if(args.containsKey("maxDepth")) {
			maxDepth = Integer.valueOf(args.get("maxDepth"));
		}
		
		return this.sparkMl.getResultTree(impurity, maxDepth);
	}
	
	
	// Return the accuracy of the random forest model, and test if any arguments has been given
	private double randomForest() {
		
		// Default argument of the model
		String impurity = "gini";
		int maxDepth = 20;
		int numTrees = 100;
		
		// Test if any arguments has been given
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
