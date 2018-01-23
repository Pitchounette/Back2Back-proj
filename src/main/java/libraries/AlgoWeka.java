package libraries;
import java.io.File;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
public class AlgoWeka {

	public static void main(String[] args) throws Exception {
		// load CSV
		CSVLoader csvloader = new CSVLoader();
		csvloader.setSource(new File("src/main/resources/iris.csv"));
		Instances data = csvloader.getDataSet();
		data.setClassIndex(data.numAttributes() - 1);
		data.randomize(new java.util.Random(0));
		
		int trainSize = (int) Math.round(data.numInstances() * 0.7);
		int testSize = data.numInstances() - trainSize;
		Instances train = new Instances(data, 0, trainSize);
		Instances test = new Instances(data, trainSize, testSize);
		

	
		
		if (true){
			String[] options = new String[2];
			 options[0] = "-R";                                   
			 options[1] = "1";                                 
			 Remove remove = new Remove();                         
			 remove.setOptions(options);                           
			 remove.setInputFormat(data);                       
			 data = Filter.useFilter(data, remove);  
		}
		
		
		
		J48 model = new J48();
		
		model.buildClassifier(train);	

		Evaluation eval = new Evaluation(train);
		
		eval.evaluateModel(model, test);
		System.out.println(model);
		
		System.out.println(eval.toSummaryString("\nResults\n======\n", false));
		
	/*		 
		//Make tree J48
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
		
		 J48 cls = new J48();
	
		 
		 System.out.println(eval.toSummaryString());
		 System.out.println(eval.toClassDetailsString());
		 
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
*/
	}

}