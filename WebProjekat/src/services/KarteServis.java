package services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.Karte;
import model.Karta;

@Path("karte")
public class KarteServis {
	
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/sveKarte")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Karta> sveKarte(){
		return this.getKarte().getKarte();
	}
	
	@GET
	@Path("/pretragaPoNazivu")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Karta> pretragaPoNazivu(@QueryParam("naziv")String naziv) {
		
		ArrayList<Karta> rezultati = new ArrayList<>();
		//pretrazujemo iz liste svih karata karte sa prosledjenim nazivom manifestacije
		for(Karta k : this.getKarte().getKarte()) {
			if(k.getManifestacijaZaKojuJeRezervisana().equalsIgnoreCase(naziv.trim())) {
				rezultati.add(k);
			}
		}
		return rezultati;
	}
	
	@GET
	@Path("pretragaPoCeni")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Karta> pretragaPoCeni(@QueryParam("cenaOd")String cenaOd, @QueryParam("cenaDo")String cenaDo){
		
		ArrayList<Karta> rezultati = new ArrayList<>();

		for(Karta k: this.getKarte().getKarte()) {
			if(k.getCena() < Double.parseDouble(cenaDo) &&
					k.getCena() > Double.parseDouble(cenaOd)) {
				rezultati.add(k);
			}
			else if(k.getCena() == Double.parseDouble(cenaDo) ||
						k.getCena() == Double.parseDouble(cenaOd)) {
				rezultati.add(k);
			}
		}
		
		return rezultati;
	}
	
	@GET
	@Path("pretragaPoDatumu")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Karta> pretragaPoDatumu(@QueryParam("datumOd")String datumOd, @QueryParam("datumDo")String datumDo){
		
		ArrayList<Karta> rezultati = new ArrayList<>();
		
		//datumu menjamo format
		System.out.println("////////// " + datumOd);
		String noviDatumOd = datumOd.replace("T", " ");
		String noviDatumDo = datumDo.replace("T", " ");
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime datumOdPars = LocalDateTime.parse(noviDatumOd, format);
		LocalDateTime datumDoPars = LocalDateTime.parse(noviDatumDo, format);


		for(Karta k: this.getKarte().getKarte()) {
			if(k.getDatumIvremeManifestaije().isBefore(datumDoPars) &&
					k.getDatumIvremeManifestaije().isAfter(datumOdPars)) {
				rezultati.add(k);
			}
			if(k.getDatumIvremeManifestaije().equals(datumDoPars) ||
					k.getDatumIvremeManifestaije().equals(datumOdPars)) {
				rezultati.add(k);
			}
		}
		
		return rezultati;
	} 
	
	
	public Karte getKarte() {
		Karte karte = (Karte) ctx.getAttribute("karte");
		
		if(karte == null) {
			karte = new Karte(ctx.getRealPath(""));
			
			//System.out.println("putanja: " + ctx.getRealPath("") + System.lineSeparator());
			ctx.setAttribute("karte", karte);
		}
		return karte;
	}
}
