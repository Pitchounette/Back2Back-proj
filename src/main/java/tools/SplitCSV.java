package tools;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;


/*
 * Split CSV is a data structure that is used to have the same learning and test set when we compare different methods
 */

public class SplitCSV {
	
	private String path; // Path of the dataset to be split
	private File file; 
	private String name;
	List<String[]> dataTable = new ArrayList<String[]>(); // The full dataset
	
	String trainFile;
	String testFile;
	
	// Constructor of the class that import the dataset at the position path and split it
	public SplitCSV(String path,String name) throws IOException {
		this.path = path;
		this.name = name;
		this.file = new File(path);
		this.importData();
		this.splitCSV(0.7);
	}
	
	public String getTrainingPath() {
		return this.trainFile;
	}
	
	public String getTestingPath() {
		return this.testFile;
	}
	
	// Import the data set 
	public void importData() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(this.file));
		                
		String line = null; 
		while ((line = reader.readLine()) != null) { 
			String[] values = line.split(",");
			this.dataTable.add(values); 
		}
	}
	
	
	// Split the csv into 2 new csv files, a training one and a testing one
	public void splitCSV(double pourc) throws IOException  {
		String[] header = dataTable.remove(0);
		Collections.shuffle(this.dataTable);
		
		int nbrtrain = (int) Math.round(dataTable.size() * pourc);
		
		List<String[]> train = dataTable.subList(0, nbrtrain); 
		train.add(0, header);
		
		this.trainFile = path.substring(0, path.length()-4)+"train_"+this.name+".csv";
		
		CSVWriter writerTrain = new CSVWriter(new FileWriter(trainFile),',','\0');
        writerTrain.writeAll(train);
        writerTrain.close();
		
        List<String[]> test = dataTable.subList(nbrtrain+1, dataTable.size());
		test.add(0,header);
		this.testFile = path.substring(0, path.length()-4)+"test_"+this.name+".csv";
		CSVWriter writerTest = new CSVWriter(new FileWriter(testFile),',','\0');
        writerTest.writeAll(test);
        writerTest.close();
        
        System.out.println("test and train files were created successfully");

    }
				
		
	/*
	public static void main(String[] args) throws Exception {
		SplitCSV csv = new SplitCSV("src/main/resources/iris.csv","iris");
	}*/
	

}
