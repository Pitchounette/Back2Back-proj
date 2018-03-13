package tools;

import java.io.IOException;
import java.util.HashMap;

import libraries.Library;
import libraries.SparkMLLib;
import libraries.WekaLib;

/*
 * Il s'agit du main de notre programme
 * Objectif : Etre capable de faire marcher ce script qui servira de base au futur amélioration
 * 
 */
public class Lanceur {

	public static void main(String[] args) throws IOException {
		// CHoix des données 
		String pathData = "src/main/resources/statsFSEVarFry.csv";
		SplitCSV dataSplit = new SplitCSV(pathData, "iris");
		HashMap argumentsLib1 = new HashMap<String,String>();
		argumentsLib1.put("maxDepth", "1");
		Library sparkML = new SparkMLLib(dataSplit,"RandomForest",argumentsLib1);
		Library sparkML2 = new SparkMLLib(dataSplit,"RandomForest");

		Comparateur comparateur = new Comparateur(sparkML,sparkML2);
		System.out.println(comparateur.getResult());

	}

}