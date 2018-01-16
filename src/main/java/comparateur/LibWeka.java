package comparateur;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class LibWeka {

	public void importData(String filepath, String filename) throws IOException {
		CSVLoader csvloader = new CSVLoader();
		csvloader.setSource(new File(filepath));
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
	
	
	
	
	
}
