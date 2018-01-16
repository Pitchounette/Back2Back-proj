package comparateur;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class LibWeka {

	public void importData() throws IOException {
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

		data.setClassIndex(data.numAttributes() - 1);
		data.randomize(new java.util.Random());	// randomize instance order before splitting dataset
		Instances trainData = data.trainCV(2, 0);
		Instances testData = data.testCV(2, 0);
	}
	
	
	
	public double J48accuracy(Instances trainData, Instances testData) throws Exception {
		J48 tree = new J48();
		String[] options = new String[1];
		options[0] = "-U"; 
		tree.setOptions(options);
		tree.buildClassifier(trainData);

		System.out.println(tree);
		Evaluation eval = new Evaluation(trainData);
		eval.evaluateModel(tree, trainData);
		System.out.println("1-NN accuracy on training data:\n" + eval.pctCorrect()/100);
		eval.evaluateModel(tree, testData);
		System.out.println("1-NN accuracy on separate test data:\n" + eval.pctCorrect()/100);
		return eval.pctCorrect()/100;
	}
	
	
	
	public void accuracy(String methode, ArrayList param ) {
		
	}
	
	
}
