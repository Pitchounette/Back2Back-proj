package libraries;

import java.io.File;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class LibWeka {

	private Instances train;
	private Instances test;
	
	public LibWeka(String path,String name, boolean header) throws Exception {
		
		File fileTrain = new File(path+"train_"+name+".csv");
		CSVLoader loaderTrain = new CSVLoader();
		loaderTrain.setSource(fileTrain);
		this.train = loaderTrain.getDataSet();
		this.train.setClassIndex(this.train.numAttributes() - 1);
		
		File fileTest = new File(path+"test_"+name+".csv");
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
	
	public double accuracy(J48 tree) throws Exception {
		

		Evaluation eval = new Evaluation(this.train);
		eval.evaluateModel(tree, this.test);

		return eval.pctCorrect()/100;
	}
	
	
	public double nbAccuracy(Instances trainData, Instances testData) throws Exception {
		NaiveBayes cModel = new NaiveBayes();
		
		cModel.buildClassifier(trainData);
		 
		Evaluation eval = new Evaluation(trainData);
		eval.evaluateModel(cModel, testData);
		
		return eval.pctCorrect()/100;
	}
	
	

}
