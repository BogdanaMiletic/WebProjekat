package model;

import java.time.LocalDateTime;

public class Manifestacija {
	
	public enum TipManifestacije{
		KONCERT, FESTIVAL, POZORISTE;
	}
	public enum Status{
		AKTIVNO, NEAKTIVNO;
	}
	private String naziv;
	private TipManifestacije tip;
	private int brojMesta;
	private LocalDateTime datumIvremeOdrzavanja;
	private double cenaRegularKarte;
	private Status status;
	private Lokacija lokacija;
	
	public Manifestacija() {
		
	}

	public Manifestacija(String naziv, TipManifestacije tip, int brojMesta, LocalDateTime datumIvremeOdrzavanja,
			double cenaRegularKarte, Status status, Lokacija lokacija) {
		super();
		this.naziv = naziv;
		this.tip = tip;
		this.brojMesta = brojMesta;
		this.datumIvremeOdrzavanja = datumIvremeOdrzavanja;
		this.cenaRegularKarte = cenaRegularKarte;
		this.status = status;
		this.lokacija = lokacija;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + brojMesta;
		long temp;
		temp = Double.doubleToLongBits(cenaRegularKarte);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((datumIvremeOdrzavanja == null) ? 0 : datumIvremeOdrzavanja.hashCode());
		result = prime * result + ((lokacija == null) ? 0 : lokacija.hashCode());
		result = prime * result + ((naziv == null) ? 0 : naziv.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tip == null) ? 0 : tip.hashCode());
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
		Manifestacija other = (Manifestacija) obj;
		if (brojMesta != other.brojMesta)
			return false;
		if (Double.doubleToLongBits(cenaRegularKarte) != Double.doubleToLongBits(other.cenaRegularKarte))
			return false;
		if (datumIvremeOdrzavanja == null) {
			if (other.datumIvremeOdrzavanja != null)
				return false;
		} else if (!datumIvremeOdrzavanja.equals(other.datumIvremeOdrzavanja))
			return false;
		if (lokacija == null) {
			if (other.lokacija != null)
				return false;
		} else if (!lokacija.equals(other.lokacija))
			return false;
		if (naziv == null) {
			if (other.naziv != null)
				return false;
		} else if (!naziv.equals(other.naziv))
			return false;
		if (status != other.status)
			return false;
		if (tip != other.tip)
			return false;
		return true;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public TipManifestacije getTip() {
		return tip;
	}

	public void setTip(TipManifestacije tip) {
		this.tip = tip;
	}

	public int getBrojMesta() {
		return brojMesta;
	}

	public void setBrojMesta(int brojMesta) {
		this.brojMesta = brojMesta;
	}

	public LocalDateTime getDatumIvremeOdrzavanja() {
		return datumIvremeOdrzavanja;
	}

	public void setDatumIvremeOdrzavanja(LocalDateTime datumIvremeOdrzavanja) {
		this.datumIvremeOdrzavanja = datumIvremeOdrzavanja;
	}

	public double getCenaRegularKarte() {
		return cenaRegularKarte;
	}

	public void setCenaRegularKarte(double cenaRegularKarte) {
		this.cenaRegularKarte = cenaRegularKarte;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Lokacija getLokacija() {
		return lokacija;
	}

	public void setLokacija(Lokacija lokacija) {
		this.lokacija = lokacija;
	}

	@Override
	public String toString() {
		return "Manifestacija [naziv=" + naziv + ", tip=" + tip + ", brojMesta=" + brojMesta
				+ ", datumIvremeOdrzavanja=" + datumIvremeOdrzavanja + ", cenaRegularKarte=" + cenaRegularKarte
				+ ", status=" + status + ", lokacija=" + lokacija + "]";
	}
}
