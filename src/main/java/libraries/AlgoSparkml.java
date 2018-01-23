package libraries;

import java.util.HashMap;
import java.util.Map;

import org.apache.parquet.io.RecordReader;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;
import org.apache.spark.SparkConf;



class AlgoSparkml {

	public static void main(String[] args) {
		
		//Configuration of Spark
		SparkConf sparkConf = new SparkConf().setAppName("DecisionTreeExample").setMaster("local[2]").set("spark.executor.memory","1g");;
		
		// Start the Spark Context
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);

			
		// provide path to data transformed as [feature vectors]
		String path = "src/main/resources/iris.csv";
		JavaRDD inputData = jsc.textFile(path);


		// split the data for training (60%) and testing (40%)
		JavaRDD[] tmp = inputData.randomSplit(new double[]{0.6, 0.4});
		JavaRDD trainingData = tmp[0]; // training set
		JavaRDD testData = tmp[1]; // test set
		System.out.println(inputData.first());
		
		


		
		// Define parameters
		int numClasses = 2;
		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap();
		categoricalFeaturesInfo.put(4, 2);
		String impurity = "gini";
		int maxDepth = 5;
		int maxBins = 32;
		
		// Creation of the tree
		DecisionTreeModel model = DecisionTree.trainClassifier(trainingData, numClasses,
		          categoricalFeaturesInfo, impurity, maxDepth, maxBins);
		
	
		
		

	}

}

