package librariesMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import libraries.Methode;
import tools.SplitCSV;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class LibWeka {

	private Instances train;
	private Instances test;
	private static Methode array[]= {Methode.DECISIONTREE, Methode.RANDOMFOREST};
	static public ArrayList<Methode> allowedMethods = new ArrayList<Methode>(Arrays.asList(array)) ;
	
	public LibWeka(String testPath,String trainPath, boolean header) throws Exception {
		
		File fileTrain = new File(trainPath);
		CSVLoader loaderTrain = new CSVLoader();
		loaderTrain.setSource(fileTrain);
		this.train = loaderTrain.getDataSet();
		this.train.setClassIndex(this.train.numAttributes() - 1);
		
		File fileTest = new File(testPath);
		CSVLoader loaderTest = new CSVLoader();
		loaderTest.setSource(fileTest);
		this.test = loaderTest.getDataSet();
		this.test.setClassIndex(this.test.numAttributes() - 1);
		
		if(header) {
			 String[] options = new String[2];
			 options[0] = "-R";                                   
			 options[1] = "1"; 
			 
			 Remove removeTrain = new Remove();                         
			 removeTrain.setOptions(options);                           
			 removeTrain.setInputFormat(this.train);                       
			 this.train = Filter.useFilter(this.train, removeTrain);  
			 
			 Remove removeTest = new Remove();                         
			 removeTest.setOptions(options);                           
			 removeTest.setInputFormat(this.test);                       
			 this.test = Filter.useFilter(this.test, removeTest); 
		}
	}
	
	public J48 decisionTree() throws Exception {
		J48 tree = new J48();
		
		String[] options = new String[1];
		options[0] = "-U"; 
		tree.setOptions(options);
		
		tree.buildClassifier(this.train);
		return tree;
	}
	
	public RandomForest randomForest() throws Exception {
		RandomForest tree = new RandomForest();
		
		tree.buildClassifier(this.train);
		
		return tree;
	}
	
	public double accuracy(Classifier tree) throws Exception {
		
		Evaluation eval = new Evaluation(this.train);
		eval.evaluateModel(tree, this.test);

		return eval.pctCorrect()/100;
	}
	
	public static void main(String[] args) throws Exception {
		LibWeka weka = new LibWeka("src/main/resources/","iris", true);
		J48 tree = weka.decisionTree();
		System.out.println(weka.accuracy(tree));
		
		RandomForest rf = weka.randomForest();
		System.out.println(weka.accuracy(rf));
	}
	
	public ArrayList<Methode> getMethode() {
		return allowedMethods;
	}

}
