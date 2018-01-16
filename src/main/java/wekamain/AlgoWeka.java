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
				 
		//Make tree J48
		J48 tree = new J48();
		String[] options = new String[1];
		options[0] = "-U"; 
		tree.setOptions(options);
		tree.buildClassifier(data);

		System.out.println(tree);
		
		
		 J48 cls = new J48();
		 Evaluation eval = new Evaluation(data);
		 Random rand = new Random(1);  // using seed = 1
		 int folds = 10;
		 eval.crossValidateModel(cls, data, folds, rand);
		 
		 System.out.println(eval.toSummaryString());
		 System.out.println(eval.toClassDetailsString());

		 
		//Make tree NaiveBayes
		 NaiveBayes cModel = new NaiveBayes();
		 cModel.buildClassifier(data);
		 
		 System.out.println(cModel);
		 
		 NaiveBayes nb = new NaiveBayes();
		 eval.crossValidateModel(nb, data, folds, rand);
		 
		 System.out.println(eval.toSummaryString());
		 System.out.println(eval.toClassDetailsString());
		 
		 
		//Make tree SMO
		 SMO smo = new SMO();
		 smo.buildClassifier(data);
		
		 System.out.println(smo);
		 
		 SMO smoo = new SMO();
		 eval.crossValidateModel(smoo, data, folds, rand);
		 
		 System.out.println(eval.toSummaryString());
		 System.out.println(eval.toClassDetailsString());
		 
		 
		//Make tree Random Forest
		 RandomForest rf = new RandomForest();
		 rf.setNumTrees(100);
		 rf.buildClassifier(data);
		
		 System.out.println(rf);
		 
		 RandomForest rff = new RandomForest();
		 eval.crossValidateModel(rff, data, folds, rand);
		 
		 System.out.println(eval.toSummaryString());
		 System.out.println(eval.toClassDetailsString());
	}

}
