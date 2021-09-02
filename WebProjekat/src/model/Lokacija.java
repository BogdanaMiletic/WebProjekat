package model;

public class Lokacija {
	private double geografskaSirina;
	private double geografskaDuzina;
	private String adresa;
	
	public Lokacija() {
		
	}
	
	public Lokacija(double geografskaSirina, double geografskaDuzina, String adresa) {
		super();
		this.geografskaSirina = geografskaSirina;
		this.geografskaDuzina = geografskaDuzina;
		this.adresa = adresa;
	}
	public double getGeografskaSirina() {
		return geografskaSirina;
	}
	public void setGeografskaSirina(double geografskaSirina) {
		this.geografskaSirina = geografskaSirina;
	}
	public double getGeografskaDuzina() {
		return geografskaDuzina;
	}
	public void setGeografskaDuzina(double geografskaDuzina) {
		this.geografskaDuzina = geografskaDuzina;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	@Override
	public String toString() {
		return "Lokacija [geografskaSirina=" + geografskaSirina + ", geografskaDuzina=" + geografskaDuzina + ", adresa="
				+ adresa + "]";
	}
	
	
}
