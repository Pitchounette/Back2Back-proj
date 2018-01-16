package comparateur;

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

public class LibWeka {

	public Instances importData() throws IOException {
		CSVLoader csvloader = new CSVLoader();
		csvloader.setSource(new File("src/main/resources/iris.csv"));
		Instances csvdata = csvloader.getDataSet();

		// save ARFF
		ArffSaver saver = new ArffSaver();
		saver.setInstances(csvdata);
		saver.setFile(new File("src/main/resources/iris.arff"));
		saver.setDestination(new File("src/main/resources/iris.arff"));
		saver.writeBatch();


		ArffLoader loader= new ArffLoader();
		loader.setSource(new File("src/main/resources/iris.arff"));
		Instances data= loader.getDataSet();
		
		return data;
	}
	
	public ArrayList<Instances> splitData(Instances data) throws IOException {
		data.setClassIndex(data.numAttributes() - 1);
		data.randomize(new java.util.Random());	// randomize instance order before splitting dataset
		
		Instances trainData = data.trainCV(2, 0);
		Instances testData = data.testCV(2, 0);
		
		ArrayList<Instances> result =  new ArrayList<Instances>();
		result.add(trainData);
		result.add(testData);
		
		return result;
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
