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

public class SplitCSV {
	private String path;
	private String name;
	private File file;
	List<String[]> dataTable = new ArrayList<String[]>();
	
	public SplitCSV(String path,String name) {
		this.path = path;
		this.name = name;
		this.file = new File(path+name+".csv");
	}
	
	public void importData(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(this.file));
		                
		String line = null; 
		while ((line = reader.readLine()) != null) { 
			String[] values = line.split(",");
			this.dataTable.add(values); 
		}
	}
		
	public void splitCSV(double pourc) throws IOException  {
		String[] header = dataTable.remove(0);
		Collections.shuffle(this.dataTable);
		
		int nbrtrain = (int) Math.round(dataTable.size() * pourc);
		
		List<String[]> train = dataTable.subList(0, nbrtrain); 
		train.add(0, header);
		String trainFile = this.path+"train_"+this.name+".csv";
		CSVWriter writerTrain = new CSVWriter(new FileWriter(trainFile));
        writerTrain.writeAll(train);
        writerTrain.close();
		
        List<String[]> test = dataTable.subList(nbrtrain+1, dataTable.size());
		test.add(0,header);
		String testFile = this.path+"test_"+this.name+".csv";
		CSVWriter writerTest = new CSVWriter(new FileWriter(testFile));
        writerTest.writeAll(test);
        writerTest.close();
        
        System.out.println("test and train files were created successfully");

    }
				
		
		
	public static void main(String[] args) throws Exception {
		SplitCSV csv = new SplitCSV("src/main/resources/","iris");
		csv.importData(csv.file);
		csv.splitCSV(0.7);
	}
}
