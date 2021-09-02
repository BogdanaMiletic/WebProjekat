package model;

import java.time.LocalDateTime;

public class Karta {
	public enum Status{
		REZERVISANA, ODUSTANAK;
	}
	public enum TipKarte{
		VIP, REGULAR, FAN_PIT;
	}
	private String id;
	private Manifestacija manifestacijaZaKojuJeRezervisana;
	private LocalDateTime datumIvremeManifestaije;
	private double cena;
	private String kupacImeIprezime;
	private Status status;
	private TipKarte tipKarte;
	
	private Karta() {
		
	}
	
	public Karta(String id, Manifestacija manifestacijaZaKojuJeRezervisana, LocalDateTime datumIvremeManifestaije,
			double cena, String kupacImeIprezime, Status status, TipKarte tipKarte) {
		this.id = id;
		this.manifestacijaZaKojuJeRezervisana = manifestacijaZaKojuJeRezervisana;
		this.datumIvremeManifestaije = datumIvremeManifestaije;
		this.cena = cena;
		this.kupacImeIprezime = kupacImeIprezime;
		this.status = status;
		this.tipKarte = tipKarte;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Manifestacija getManifestacijaZaKojuJeRezervisana() {
		return manifestacijaZaKojuJeRezervisana;
	}
	public void setManifestacijaZaKojuJeRezervisana(Manifestacija manifestacijaZaKojuJeRezervisana) {
		this.manifestacijaZaKojuJeRezervisana = manifestacijaZaKojuJeRezervisana;
	}
	public LocalDateTime getDatumIvremeManifestaije() {
		return datumIvremeManifestaije;
	}
	public void setDatumIvremeManifestaije(LocalDateTime datumIvremeManifestaije) {
		this.datumIvremeManifestaije = datumIvremeManifestaije;
	}
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	public String getKupacImeIprezime() {
		return kupacImeIprezime;
	}
	public void setKupacImeIprezime(String kupacImeIprezime) {
		this.kupacImeIprezime = kupacImeIprezime;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public TipKarte getTipKarte() {
		return tipKarte;
	}
	public void setTipKarte(TipKarte tipKarte) {
		this.tipKarte = tipKarte;
	}
	@Override
	public String toString() {
		return "Karta [id=" + id + ", manifestacijaZaKojuJeRezervisana=" + manifestacijaZaKojuJeRezervisana
				+ ", datumIvremeManifestaije=" + datumIvremeManifestaije + ", cena=" + cena + ", kupacImeIprezime="
				+ kupacImeIprezime + ", status=" + status + ", tipKarte=" + tipKarte + "]";
	}
}
