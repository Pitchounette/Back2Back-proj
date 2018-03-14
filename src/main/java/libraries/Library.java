package libraries;

import tools.SplitCSV;

public abstract class Library {
	
	protected  SplitCSV data;
	protected Methode methode;
	
	public abstract boolean isValidMethode(); // Renvoie true si la méthode est adapté à la librairy
	public abstract double getAccuracy(); // Methode temporaire, renvoie l'accuracy de la librairy pour la méthode selectionnée sur le jeux de donnée
	
	public Library(SplitCSV data,Methode methode) {
		this.data = data;
		this.methode = methode;
		
	}
	
}
