package tools;

import java.io.IOException;

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
		Librairy sparkML = new SparkMLLib(dataSplit,"Arbre");
		Librairy rEngin = new rEnginLib(dataSplit,"Arbre");
		Comparateur comparateur = new Comparateur(sparkML,rEngin);
		System.out.println(comparateur.getResult());

	}

}
