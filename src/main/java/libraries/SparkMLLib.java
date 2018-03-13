package libraries;

import tools.SplitCSV;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import librariesMethods.AlgoSparkML;

public class SparkMLLib extends Library{
	private AlgoSparkML sparkMl;
	private ArrayList<String> categories ;
	
	public SparkMLLib(SplitCSV data, String methode) throws IOException {
		super(data, methode);
		
		
		/* On va modifier les tables csv pour convenir au format Pour test*/
		File file = new File(data.getTestingPath());
		List<String[]> dataTable = new ArrayList<String[]>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		categories = new ArrayList<String>();
		String line = null; 
		while ((line = reader.readLine()) != null) { 
			String[] values = line.split(",");
			dataTable.add(values); 
		}
		
		// Apres avoir charger le CSV on le modifie
		
		
		String[] header = dataTable.remove(0);
		
		this.findCategories(dataTable); // We are looking for the cateories Y
		for(int i=0;i<dataTable.size();i++) {
			System.out.println((double) 1+categories.indexOf(dataTable.get(i)[dataTable.get(i).length-1]));

			dataTable.get(i)[dataTable.get(i).length-1] = String.valueOf((double) 1+categories.indexOf(dataTable.get(i)[dataTable.get(i).length-1]));

		}
		
		String testFile = "src/main/resources/test_spark.csv";
		CSVWriter writerTest = new CSVWriter(new FileWriter(testFile));
        writerTest.writeAll(dataTable);
        writerTest.close();
        
        
    	/* On va modifier les tables csv pour convenir au format Pour train*/
		File fileTrain = new File(data.getTrainingPath());
		List<String[]> dataTableTrain = new ArrayList<String[]>();
		BufferedReader readerTrain = new BufferedReader(new FileReader(fileTrain));
		categories = new ArrayList<String>();
		String lineTrain = null; 
		while ((lineTrain = readerTrain.readLine()) != null) { 
			String[] valuesTrain = lineTrain.split(",");
			dataTableTrain.add(valuesTrain); 
		}
		
		// Apres avoir charger le CSV on le modifie
		
		
		String[] headerTrain = dataTableTrain.remove(0);
		
		this.findCategories(dataTableTrain); // We are looking for the cateories Y
		for(int i=0;i<dataTableTrain.size();i++) {
			

			dataTableTrain.get(i)[dataTableTrain.get(i).length-1] = String.valueOf((double) 1+categories.indexOf(dataTableTrain.get(i)[dataTableTrain.get(i).length-1]));

		}
		
		String TrainFile = "src/main/resources/train_spark.csv";
		CSVWriter writerTrain = new CSVWriter(new FileWriter(TrainFile));
        writerTrain.writeAll(dataTableTrain);
        writerTrain.close();
        
        
        
        
        
        
        
		sparkMl = new AlgoSparkML(testFile,TrainFile);
		
		
	}

	private void findCategories(List<String[]> dataTable) {
		// TODO Auto-generated method stub
		int lastCol = dataTable.get(0).length -1;
		for(int i=0; i < dataTable.size();i++) {
			if(!categories.contains(dataTable.get(i)[lastCol])) {
				categories.add(dataTable.get(i)[lastCol]);
			}
		}
		
	}

	@Override
	public boolean isValidMethode() {
		// TODO Auto-generated method stub
		return (this.methode == "Arbre");
	}

	@Override
	public double getAccuracy() {
		double accuracy = 0;
		if(isValidMethode()) {
			accuracy = sparkMl.getResultTree();
		}
		else {
			System.out.println("Not a valid Method");
		}
		return accuracy;
	}

}
