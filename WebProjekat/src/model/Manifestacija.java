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
