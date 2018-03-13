package tools;

import java.io.IOException;

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
		String pathData = "src/main/resources/iris.csv";
		SplitCSV dataSplit = new SplitCSV(pathData, "iris");

		Library sparkML = new SparkMLLib(dataSplit,"DecisionTree");
		Library sparkML2 = new SparkMLLib(dataSplit,"RandomForest");

		Comparateur comparateur = new Comparateur(sparkML2,sparkML);
		System.out.println(comparateur.getResult());

	}

}