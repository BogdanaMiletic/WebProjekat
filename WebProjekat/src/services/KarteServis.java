package services;

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
