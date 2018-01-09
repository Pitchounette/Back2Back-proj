package wekamain;
import java.io.File;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class AlgoWeka {

	public static void main(String[] args) {
		
		File irisdata  = "/Back2Back/resources/iris.csv";
		//DataSource source = new DataSource("iris.csv");
		Instances data = source.getDataSet();
		if (data.classIndex() == -1)
		   data.setClassIndex(data.numAttributes() - 1);

	}

}
