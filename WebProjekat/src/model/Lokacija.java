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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adresa == null) ? 0 : adresa.hashCode());
		long temp;
		temp = Double.doubleToLongBits(geografskaDuzina);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(geografskaSirina);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lokacija other = (Lokacija) obj;
		if (adresa == null) {
			if (other.adresa != null)
				return false;
		} else if (!adresa.equals(other.adresa))
			return false;
		if (Double.doubleToLongBits(geografskaDuzina) != Double.doubleToLongBits(other.geografskaDuzina))
			return false;
		if (Double.doubleToLongBits(geografskaSirina) != Double.doubleToLongBits(other.geografskaSirina))
			return false;
		return true;
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
