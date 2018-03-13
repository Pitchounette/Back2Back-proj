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




public class AlgoSparkML implements java.io.Serializable{

	private JavaRDD<LabeledPoint> dataTest;
	private JavaRDD<LabeledPoint> dataTrain;
	/*
	 * Create a AlgoSparkML ready to test on any methods
	 */
	public AlgoSparkML (String testPath,String trainingPath) {
		
		//Configuration of Spark
		SparkConf sparkConf = new SparkConf().setAppName("DecisionTreeExample").setMaster("local[2]").set("spark.executor.memory","1g");;
		
		// Start the Spark  and SQL Context
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		SQLContext sqlContext = new SQLContext(jsc);
			
	
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

		
		// Transform the JavaRDD<Row> into a JavaRDD<LabelPoint>
		// To do so he reads each line as a string and parse in the appropriate way
		// TO DO : For now he does not find the label of the data!! (The result category)
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
		/* Some test to see if everythings is allright*/
		System.out.println(trainDataInput.first());
		System.out.println(trainDataInput.first().toString());
		System.out.println(dataTrain.first());
		

		
	}
	
	public double getResultTree() {
		// Define parameters
		int numClasses = 4; // Number of category
		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap();// Indicate if there is any categorial variable
		String impurity = "gini";
		int maxDepth = 10; // Max depth of the tree
		int maxBins = 32; // Do not change for now
		
		// Creation of the tree
		DecisionTreeModel model = DecisionTree.trainClassifier(dataTrain, numClasses,
		          categoricalFeaturesInfo, impurity, maxDepth, maxBins);
		
		// Predict for the test data using the model trained
		JavaPairRDD<Double, Double> predictionAndLabel =
		        dataTest.mapToPair(p -> new Tuple2<>(model.predict(((LabeledPoint) p).features()), ((LabeledPoint) p).label()));
		// calculate the accuracy
		
		System.out.println(model.toDebugString());
		double accuracy =
		        predictionAndLabel.filter(pl -> pl._1().equals(pl._2())).count() / (double) dataTest.count();
		 
		return (accuracy);
		 
		
		

	}


}

