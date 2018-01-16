package wekamain;
import java.io.File;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
public class AlgoWeka {

	public static void main(String[] args) throws Exception {
		// load CSV
		CSVLoader csvloader = new CSVLoader();
		csvloader.setSource(new File("src/main/resources/iris.csv"));
		Instances csvdata = csvloader.getDataSet();

		// Convert the CSV into a ARFF
		ArffSaver saver = new ArffSaver();
		saver.setInstances(csvdata);
		saver.setFile(new File("src/main/resources/iris.arff"));
		saver.setDestination(new File("src/main/resources/iris.arff"));
		saver.writeBatch();

		// Load the ARFF files
		ArffLoader loader= new ArffLoader();
		loader.setSource(new File("src/main/resources/iris.arff"));
		Instances data= loader.getDataSet();

		
		data.setClassIndex(data.numAttributes() - 1);
		data.randomize(new java.util.Random());	// randomize instance order before splitting dataset
		Instances trainData = data.trainCV(2, 0);
		Instances testData = data.testCV(2, 0);
				 
		//Make a tree (type J48)
		J48 tree = new J48();
		String[] options = new String[1];
		options[0] = "-U"; 
		tree.setOptions(options);
		tree.buildClassifier(trainData);

		Evaluation eval = new Evaluation(trainData);
		eval.evaluateModel(tree, trainData);
		System.out.println("1-NN accuracy on training data:\n" + eval.pctCorrect()/100);
		eval.evaluateModel(tree, testData);
		System.out.println("1-NN accuracy on separate test data:\n" + eval.pctCorrect()/100);
		/*
		 J48 cls = new J48();
		 Evaluation eval = new Evaluation(data);
		 
		 eval.crossValidateModel(cls, data, folds, rand);
		 
		 System.out.println(eval.toSummaryString());
		 System.out.println(eval.toClassDetailsString());
		 */
		 
		//Make tree NaiveBayes
		 NaiveBayes cModel = new NaiveBayes();
		 cModel.buildClassifier(trainData);
		 
		 eval.evaluateModel(cModel, trainData);
		 System.out.println("1-NN accuracy on training data:\n" + eval.pctCorrect()/100);
		 eval.evaluateModel(cModel, testData);
		 System.out.println("1-NN accuracy on separate test data:\n" + eval.pctCorrect()/100);

		//Make tree SMO
		 SMO smo = new SMO();
		 smo.buildClassifier(trainData);
	 
		 eval.evaluateModel(smo, trainData);
		 System.out.println("1-NN accuracy on training data:\n" + eval.pctCorrect()/100);
		 eval.evaluateModel(smo, testData);
		 System.out.println("1-NN accuracy on separate test data:\n" + eval.pctCorrect()/100);

		 
		//Make tree Random Forest
		 RandomForest rf = new RandomForest();
		 rf.setNumTrees(100);
		 rf.buildClassifier(trainData);
	
		 eval.evaluateModel(rf, trainData);
		 System.out.println("1-NN accuracy on training data:\n" + eval.pctCorrect()/100);
		 eval.evaluateModel(rf, testData);
		 System.out.println("1-NN accuracy on separate test data:\n" + eval.pctCorrect()/100);

	}

}
