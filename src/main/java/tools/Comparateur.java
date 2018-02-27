package tools;

import java.util.Scanner;

public class Comparateur {
	private Librairy lib1;
	private Librairy lib2;
	
	public Comparateur(Librairy lib1,Librairy lib2) {
		this.lib1 = lib1;
		this.lib2 = lib2;
	}
	public void Compare() {
		// TO DO : Implementer compare pour quelle calcule la valeur de test sur les deux librairy et les stockes au choix
	}
	public String getResult() {
		// TO DO : Implementer get Result pour quelle renvoie un string contenant les resultats
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.println("Veuillez saisir un mot :");

		String str = sc.nextLine();

		System.out.println("Vous avez saisi : " + str);


	}

}
