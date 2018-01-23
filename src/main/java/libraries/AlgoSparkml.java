package libraries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.parquet.io.RecordReader;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.ml.feature.PCA;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import scala.Tuple2;
import org.apache.spark.SparkConf;




class AlgoSparkml {

	public static void main(String[] args) {
		
		//Configuration of Spark
		SparkConf sparkConf = new SparkConf().setAppName("DecisionTreeExample").setMaster("local[2]").set("spark.executor.memory","1g");;
		
		// Start the Spark  and SQL Context
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		SQLContext sqlContext = new SQLContext(jsc);
			
		// provide path to data transformed as [feature vectors]
		String path = "src/main/resources/iris.csv";

		

		
		
		// Create a DataSet of Row where he infer the schema of the line
		Dataset<Row> df = sqlContext.read()
		    .format("com.databricks.spark.csv")
		    .option("inferSchema","true")
		    .option("header", "true")
		    .load("src/main/resources/iristest.csv");
		

		// Transform the Dataset into a JavaRDD
		JavaRDD<Row> inputData = jsc.parallelize(df.collectAsList());


		
		// Transform the JavaRDD<Row> into a JavaRDD<LabelPoint>
		// To do so he reads each line as a string and parse in the appropriate way
		// TO DO : For now he does not find the label of the data!! (The result category)
		@SuppressWarnings("serial")
		JavaRDD<LabeledPoint> dataLabeledPoints = inputData.map(new Function<Row,
				LabeledPoint>() {
				             public LabeledPoint call(Row line) throws Exception {
				                String lineAsString=line.toString();
				                lineAsString = lineAsString.replace("[","");
				                lineAsString = lineAsString.replace("]","");
				                String[] fields =lineAsString.split(",");
				                double[] res = new double[fields.length];
				                for(int i =0;i < fields.length;i++) {
				                	
				                	res[i] = (Double.parseDouble(fields[i]));
				                }
				                		
				                LabeledPoint labeledPoint = new
				LabeledPoint(Integer.valueOf(0),
				Vectors.dense(res));
				                return labeledPoint;
				            }
				        });

		/* Some test to see if everythings is allright
		System.out.println(inputData.first());
		System.out.println(inputData.first().toString());
		System.out.println(dataLabeledPoints.first());
		*/
		
		// Split in train and test
		JavaRDD[] tmp = dataLabeledPoints.randomSplit(new double[]{0.6, 0.4});
		JavaRDD trainingData = tmp[0]; // training set
		JavaRDD testData = tmp[1]; // t
		
		// Define parameters
		int numClasses = 2; // Number of category
		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap(); // Indicate if there is any categorial variable
		String impurity = "gini";
		int maxDepth = 5; // Max depth of the tree
		int maxBins = 32; // Do not change for now
		
		// Creation of the tree
		DecisionTreeModel model = DecisionTree.trainClassifier(trainingData, numClasses,
		          categoricalFeaturesInfo, impurity, maxDepth, maxBins);
		
		// Predict for the test data using the model trained
		JavaPairRDD<Double, Double> predictionAndLabel =
		        testData.mapToPair(p -> new Tuple2<>(model.predict(((LabeledPoint) p).features()), ((LabeledPoint) p).label()));
		// calculate the accuracy
		double accuracy =
		        predictionAndLabel.filter(pl -> pl._1().equals(pl._2())).count() / (double) testData.count();
		 
		System.out.println("Accuracy is : "+accuracy);
		System.out.println("Trained Decision Tree model:\n" + model.toDebugString());
		 
		
		

	}

}

