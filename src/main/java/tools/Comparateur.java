package tools;

import java.util.Scanner;

import libraries.Library;

public class Comparateur {
	private Library lib1;
	private Library lib2;
	
	public Comparateur(Library lib1,Library lib2) {
		this.lib1 = lib1;
		this.lib2 = lib2;
	}
	
	public String getResult() {
		Double resLib1 = lib1.getAccuracy();
		Double resLib2 = lib2.getAccuracy();
		return("Library 1 = "+resLib1+" vs Library 2 = " + resLib2);
	}
	
	
	/*
	 * 
	 
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.println("Veuillez saisir un mot :");

		String str = sc.nextLine();

		System.out.println("Vous avez saisi : " + str);


	}
	*/
}
