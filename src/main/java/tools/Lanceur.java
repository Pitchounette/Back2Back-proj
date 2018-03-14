package tools;

import java.io.IOException;
import java.util.HashMap;

import libraries.Library;
import libraries.Methode;
import libraries.RenjinLib;
import libraries.SparkMLLib;
import libraries.WekaLib;

/* Cette classe permet de lancer les programmes il contient des exemples d'utilisation de notre projet
 * 
 * 
 * 
 */
public class Lanceur {
	
	public static void main(String[] args) throws IOException {
		
		// Exemple 1 Utilisation standard des 3 librairies pour le jeu de données iris et la méthode RandomForest
		String pathDataIris = "src/main/resources/iris.csv";
		SplitCSV dataSplit = new SplitCSV(pathDataIris, "exemple1"); // SplitCSV permet de séparer le jeu de donnée en une partie d'apprentissage et une partie de test
		
		// Création des trois librairies
		HashMap argumentsRenjin = new HashMap<String,String>();
		argumentsRenjin.put("indY", "5");
		Library sparkMLEx1 = new SparkMLLib(dataSplit,Methode.RANDOMFOREST);
		Library rEnginEx1 = new RenjinLib(dataSplit,Methode.RANDOMFOREST,argumentsRenjin);
		Library wekaEx1 = new WekaLib(dataSplit,Methode.RANDOMFOREST);
		
		// Afficher du taux de bonne prédiction
		System.out.println("SparkMl accuracy = " + sparkMLEx1.getAccuracy());
		System.out.println("RenjinLib accuracy = " + rEnginEx1.getAccuracy());
		System.out.println("WekaEx1 accuracy = " + wekaEx1.getAccuracy());
		
		
		// Exemple 2 Comparaison entre deux méthodes avec les valeurs par défauts sur le jeux statsFSEVaryB
		
		
		String pathDataEx2 = "src/main/resources/statsFSEVarFry.csv";
		SplitCSV dataSplitEx2 = new SplitCSV(pathDataEx2, "exemple2");
		HashMap argumentsRenjin2 = new HashMap<String,String>();
		argumentsRenjin2.put("indY", "19");
		
		Library sparkMLEx2 = new SparkMLLib(dataSplitEx2,Methode.RANDOMFOREST);
		Library RenjinEx2 = new RenjinLib(dataSplitEx2,Methode.RANDOMFOREST,argumentsRenjin2);

		Comparateur comparateur = new Comparateur(sparkMLEx2,RenjinEx2);
		System.out.println(comparateur.getResult());
		
		// Exemple 3 : Comparaison entre deux méthodes avec arguments sur le jeux statsFSEVaryB
		
		String pathDataEx3 = "src/main/resources/statsFSEVarFry.csv";
		SplitCSV dataSplitEx3 = new SplitCSV(pathDataEx3, "exemple3");
		HashMap argumentsLib1 = new HashMap<String,String>();
		argumentsLib1.put("maxDepth", "1");
		argumentsLib1.put("numTrees", "10");
		
		HashMap argumentsLib2 = new HashMap<String,String>();
		argumentsLib2.put("maxDepth", "10");
		
		
		Library sparkMLEx3 = new SparkMLLib(dataSplitEx3,Methode.RANDOMFOREST,argumentsLib1);
		Library sparkML2Ex3 = new SparkMLLib(dataSplitEx3,Methode.DECISIONTREE,argumentsLib2);

		Comparateur comparateurEx3 = new Comparateur(sparkMLEx3,sparkML2Ex3);
		System.out.println(comparateurEx3.getResult());
		
		// Exemple 4 : Comparaison entre deux méthodes avec arguments sur le jeux statsFSEVaryB
		
		String pathDataEx4 = "src/main/resources/statsFSEVarFry.csv";
		SplitCSV dataSplitEx4 = new SplitCSV(pathDataEx4, "exemple4");
		HashMap argumentsLib1Ex4 = new HashMap<String,String>();
		argumentsLib1Ex4.put("numTrees", "20");

		HashMap argumentsLib2Ex4 = new HashMap<String,String>();
		argumentsLib2Ex4.put("numTrees", "5");


		Library sparkMLEx4 = new SparkMLLib(dataSplitEx4,Methode.RANDOMFOREST,argumentsLib1Ex4);
		Library sparkML2Ex4 = new SparkMLLib(dataSplitEx4,Methode.RANDOMFOREST,argumentsLib2Ex4);

		Comparateur comparateurEx4 = new Comparateur(sparkMLEx4,sparkML2Ex4);
		System.out.println(comparateurEx4.getResult());


		

	}

}