package librariesMethods;

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
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.feature.PCA;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.feature.VectorIndexer;
import org.apache.spark.ml.feature.VectorIndexerModel;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;
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
import org.apache.log4j.Logger;
import org.apache.log4j.Level;




public class AlgoSparkML implements java.io.Serializable{

	private JavaRDD<LabeledPoint> dataTest;
	private JavaRDD<LabeledPoint> dataTrain;
	private int numClasses;
	/*
	 * Create a AlgoSparkML ready to test on any methods
	 */

	// Static variables prevent two spark context to be started at the same time
	static private SparkConf sparkConf = new SparkConf().setAppName("backToBackProject").setMaster("local[2]").set("spark.executor.memory","1g");
	static private JavaSparkContext jsc = new JavaSparkContext(sparkConf);
	static private SQLContext sqlContext = new SQLContext(jsc);

	// Constructeur de la classe qui prends en argument, le path vers le dataset de test et celui vers le train, et le nombre de catégories pour la variable à expliquer
	public AlgoSparkML (String testPath,String trainingPath,int numClasses) {
		this.numClasses = numClasses +1 ;

		//Configuration of Spark
		SparkConf sparkConf = AlgoSparkML.sparkConf;

		// Start the Spark  and SQL Context
		JavaSparkContext jsc = AlgoSparkML.jsc;
		SQLContext sqlContext = AlgoSparkML.sqlContext;
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);


		// Create a DataSet of Row where he infer the schema of the line
		Dataset<Row> dataSetTest = sqlContext.read()
				.format("com.databricks.spark.csv")
				.option("inferSchema","true")
				.option("header", "true")
				.load(testPath);

		Dataset<Row> dataSetTrain = sqlContext.read()
				.format("com.databricks.spark.csv")
				.option("inferSchema","true")
				.option("header", "true")
				.load(trainingPath);

		// Transform the Dataset into a JavaRDD
		JavaRDD<Row> testDataInput = jsc.parallelize(dataSetTest.collectAsList());
		JavaRDD<Row> trainDataInput = jsc.parallelize(dataSetTrain.collectAsList());


		// Transform the JavaRDD<Row> for the test data into a JavaRDD<LabelPoint>

		this.dataTest = testDataInput.map(new Function<Row,
				LabeledPoint>() {
			public LabeledPoint call(Row line) throws Exception {
				String lineAsString=line.toString();
				lineAsString = lineAsString.replace("[","");
				lineAsString = lineAsString.replace("]","");
				String[] fields =lineAsString.split(",");

				double[] res = new double[fields.length-1];
				for(int i =0;i < fields.length-1;i++) {

					res[i] = (Double.parseDouble(fields[i]));
				}

				LabeledPoint labeledPoint = new
						LabeledPoint((Double.parseDouble(fields[fields.length-1])),
								Vectors.dense(res));
				return labeledPoint;
				
			}
		});

		// Transform the JavaRDD<Row> for the train data into a JavaRDD<LabelPoint>
		this.dataTrain = trainDataInput.map(new Function<Row,
				LabeledPoint>() {
			public LabeledPoint call(Row line) throws Exception {
				String lineAsString=line.toString();
				lineAsString = lineAsString.replace("[","");
				lineAsString = lineAsString.replace("]","");
				String[] fields =lineAsString.split(",");
				double[] res = new double[fields.length-1];
				for(int i =0;i < fields.length-1;i++) {

					res[i] = (Double.parseDouble(fields[i]));
				}

				LabeledPoint labeledPoint = new
						LabeledPoint((Double.parseDouble(fields[fields.length-1])),
								Vectors.dense(res));
				return labeledPoint;
			}

		});


	}

	// Take in input the way of calculate the impurity value and the max depth of the tree, and return the accuracy on the decision tree (train on the train data) on the test data
	public double getResultTree(String impurity,int maxDepth) {
		// Define parameters

		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap();// Indicate if there is any categorial variable

		int maxBins = 32; // Do not change for now

		// Creation of the tree
		DecisionTreeModel model = DecisionTree.trainClassifier(dataTrain, numClasses,
				categoricalFeaturesInfo, impurity, maxDepth, maxBins);

		// Predict for the test data using the model trained
		JavaPairRDD<Double, Double> predictionAndLabel =
				dataTest.mapToPair(p -> new Tuple2<>(model.predict(((LabeledPoint) p).features()), ((LabeledPoint) p).label()));
		// calculate the accuracy

		// System.out.println(model.toDebugString()); // Print the tree 
		double accuracy =
				predictionAndLabel.filter(pl -> pl._1().equals(pl._2())).count() / (double) dataTest.count();

		return (accuracy);




	}

	/* Return the accuracy of the prediction on the test data of a randomforest model train on the train set, with parameters
	 * numTrees : numbers of tree in the forest
	 * impurity : methods to calculate the impurity
	 * maxDepth : maximum depth of the tree 
	 */
	public double getResultRandomForest(int numTrees,String impurity,int maxDepth) {

		// Train a RandomForest model.

		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap();// Indicate if there is any categorial variable
		// Use more in practice.
		String featureSubsetStrategy = "auto"; // Let the algorithm choose.

		int maxBins = 32; // Do not change
		int seed = 120; // Do not change 

		// Train the model
		RandomForestModel model = RandomForest.trainClassifier(dataTrain, numClasses, categoricalFeaturesInfo,
				numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins,seed);

		// Evaluate model on test instances and compute test error
		JavaPairRDD<Double, Double> predictionAndLabel =
				dataTest.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
		double testErr =
				predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) dataTest.count();

		return (1-testErr);

	}

	// Return the accuracy on the test set of a SVM model train on the training set
	public double getResultSVM() {
		// Run training algorithm to build the model.
		int numIterations = 100;
		SVMModel model = SVMWithSGD.train(dataTrain.rdd(), numIterations);

		// Clear the default threshold.
		model.clearThreshold();

		// Compute raw scores on the test set.
		JavaRDD<Tuple2<Object, Object>> scoreAndLabels = dataTest.map(p ->
		new Tuple2<>(model.predict(p.features()), p.label()));
		double testErr =
				scoreAndLabels.filter(pl -> !pl._1().equals(pl._2())).count() / (double) dataTest.count();

		return testErr;
	}


}

