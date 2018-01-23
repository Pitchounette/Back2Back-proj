package libraries;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class LibWeka {
	
	public String path;
	public Instances data;
	public Instances train;
	public Instances test;
	
	public LibWeka(String path, boolean header) throws Exception {
		this.path = path;
		
		File file = new File(path);
		CSVLoader loader = new CSVLoader();
		loader.setSource(file);
		this.data = loader.getDataSet();
		
		Instances data = loader.getDataSet();
		data.setClassIndex(data.numAttributes() - 1);
		
		if(header) {
			String[] options = new String[2];
			 options[0] = "-R";                                   
			 options[1] = "1";                                 
			 Remove remove = new Remove();                         
			 remove.setOptions(options);                           
			 remove.setInputFormat(data);                       
			 data = Filter.useFilter(data, remove);  
		}
	}
	
	public void splitData(double pourc) throws IOException {
		data.randomize(new java.util.Random());
		
		int trainTaille = (int) Math.round(data.numInstances() * pourc);
		int testTaille = data.numInstances() - trainTaille;
		this.train = new Instances(data, 0, trainTaille);
		this.test = new Instances(data, trainTaille, testTaille);
	}
	
	
	
	
	public double J48accuracy(Instances trainData, Instances testData) throws Exception {
		J48 tree = new J48();
		
		String[] options = new String[1];
		options[0] = "-U"; 
		tree.setOptions(options);
		
		tree.buildClassifier(trainData);

		Evaluation eval = new Evaluation(trainData);
		eval.evaluateModel(tree, testData);

		return eval.pctCorrect()/100;
	}
	
	
	
	public double NBaccuracy(Instances trainData, Instances testData) throws Exception {
		NaiveBayes cModel = new NaiveBayes();
		
		cModel.buildClassifier(trainData);
		 
		Evaluation eval = new Evaluation(trainData);
		eval.evaluateModel(cModel, testData);
		
		return eval.pctCorrect()/100;
	}
	
	
	public void accuracy(String methode, ArrayList param ) {
		
	}
	
	
}
