package model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Karta {
	public enum Status{
		REZERVISANA, ODUSTANAK;
	}
	public enum TipKarte{
		VIP, REGULAR, FAN_PIT;
	}
	private String id;
	private String manifestacijaZaKojuJeRezervisana;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime datumIvremeManifestaije;
	private double cena;
	private String kupacImeIprezime;
	private Status status;
	private TipKarte tipKarte;
	
	public Karta() {
		
	}
	
	public Karta(Karta k) {
		this.id = k.getId();
		this.manifestacijaZaKojuJeRezervisana = k.getManifestacijaZaKojuJeRezervisana();
		this.datumIvremeManifestaije = k.getDatumIvremeManifestaije();
		this.cena = k.getCena();
		this.kupacImeIprezime = k.getKupacImeIprezime();
		this.status = k.getStatus();
		this.tipKarte = k.getTipKarte();
	}
	
	public Karta(String id, String manifestacijaZaKojuJeRezervisana, LocalDateTime datumIvremeManifestaije,
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
	public String getManifestacijaZaKojuJeRezervisana() {
		return manifestacijaZaKojuJeRezervisana;
	}
	public void setManifestacijaZaKojuJeRezervisana(String manifestacijaZaKojuJeRezervisana) {
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
