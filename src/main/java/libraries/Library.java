package libraries;

import tools.SplitCSV;

public abstract class Library {
	public abstract boolean isValidMethode(); // Renvoie true si la méthode est adapté à la librairy
	public abstract double getAccuracy(); // Methode temporaire, renvoie l'accuracy de la librairy pour la méthode selectionnée sur le jeux de donnée
	protected  SplitCSV data;
	protected String methode;
	
	
	public Library(SplitCSV data,String methode) {
		this.data = data;
		this.methode = methode;
		
	}
	
}
