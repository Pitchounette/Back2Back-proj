package wekamain;

import java.io.File;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class Test {

	public static void main(String[] args) throws Exception {
    // load CSV
    CSVLoader csvloader = new CSVLoader();
    csvloader.setSource(new File("/home/lydia/Back2Back-proj/src/main/resources/iris.csv"));
    Instances csvdata = csvloader.getDataSet();
 
    // save ARFF
    ArffSaver saver = new ArffSaver();
    saver.setInstances(csvdata);
    saver.setFile(new File("/home/lydia/Back2Back-proj/src/main/resources/iris.arff"));
    saver.setDestination(new File("/home/lydia/Back2Back-proj/src/main/resources/iris.arff"));
    saver.writeBatch();
    
   
    ArffLoader loader= new ArffLoader();
    loader.setSource(new File("/home/lydia/Back2Back-proj/src/main/resources/iris.arff"));
    Instances data= loader.getDataSet();
    
    data.setClassIndex(data.numAttributes() - 1);

    //Make tree
    J48 tree = new J48();
    String[] options = new String[1];
    options[0] = "-U"; 
    tree.setOptions(options);
    tree.buildClassifier(data);

    //Print tree
    System.out.println(tree);

    
	}
  }