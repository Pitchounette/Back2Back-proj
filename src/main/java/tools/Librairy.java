package tools;

public abstract class Librairy {
	public abstract boolean isValidMethode(); // Renvoie true si la méthode est adapté à la librairy
	public abstract double getAccuracy(); // Methode temporaire, renvoie l'accuracy de la librairy pour la méthode selectionnée sur le jeux de donnée
	private  SplitCSV data;
	private String methode;
	
	
	public Librairy(SplitCSV data,String methode) {
		this.data = data;
		this.methode = methode;
		
	}
	
}
