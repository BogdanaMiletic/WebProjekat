package services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.Karte;
import model.Karta;
import model.Manifestacija;

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
	
	///???????????? ispraviti ovo da radi sa kartama od kupca...
	
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
	
	
	//********** sortiranje ******************
	@GET
	@Path("/sortiranjeNazivOpadajuce")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Karta> sortiranjePoNazivuOpadajuce() {
		
		ArrayList<Karta> rezultati = new ArrayList<>();
		
		rezultati.addAll(this.getKarte().getKarte());
		
		Collections.sort(rezultati, new Comparator<Karta>() {
			
			@Override
			public int compare(Karta k1, Karta k2) {
				
				return k1.getManifestacijaZaKojuJeRezervisana().compareToIgnoreCase(k2.getManifestacijaZaKojuJeRezervisana());
			}
			
		});
		return rezultati;
	}
	
	@GET
	@Path("/sortiranjeNazivRastuce")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Karta> sortiranjePoNazivuRastuce() {
		
		ArrayList<Karta> rezultati = new ArrayList<>();
		
		rezultati.addAll(this.getKarte().getKarte());
		
		Collections.sort(rezultati, new Comparator<Karta>() {
			
			@Override
			public int compare(Karta k1, Karta k2) {
				
				return k2.getManifestacijaZaKojuJeRezervisana().compareToIgnoreCase(k1.getManifestacijaZaKojuJeRezervisana());
			}
			
		});
		return rezultati;
	}
	
	@GET
	@Path("/sortiranjeDatumRastuce")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Karta> sortiranjePoDatumuRastuce() {
		
		ArrayList<Karta> rezultati = new ArrayList<>();
		
		rezultati.addAll(this.getKarte().getKarte());
		
		Collections.sort(rezultati, new Comparator<Karta>() {
			
			@Override
			public int compare(Karta k1, Karta k2) {
				
				if( k1.getDatumIvremeManifestaije().isBefore(k2.getDatumIvremeManifestaije())){
					return -1;
				}
				return 1;
		
			}
			
		});
		return rezultati;
	}
	
	@GET
	@Path("/sortiranjeDatumOpadajuce")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Karta> sortiranjePoDatumuOpadajuce() {
		
		ArrayList<Karta> rezultati = new ArrayList<>();
		
		rezultati.addAll(this.getKarte().getKarte());
		
		Collections.sort(rezultati, new Comparator<Karta>() {
			
			@Override
			public int compare(Karta k1, Karta k2) {
				
				if( k2.getDatumIvremeManifestaije().isBefore(k1.getDatumIvremeManifestaije())){
					return -1;
				}
				return 1;
		
			}
			
		});
		return rezultati;
	}
	
	@GET
	@Path("/sortiranjeCenaOpadajuce")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Karta> sortiranjePoCeniOpadajuce() {
		
		ArrayList<Karta> rezultati = new ArrayList<>();
		
		rezultati.addAll(this.getKarte().getKarte());
		
		Collections.sort(rezultati, new Comparator<Karta>() {
			
			@Override
			public int compare(Karta k1, Karta k2) {
				
				if( k2.getCena()< k1.getCena()){
					return -1;
				}
				return 1;
		
			}
			
		});
		return rezultati;
	}
	
	@GET
	@Path("/sortiranjeCenaRastuce")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Karta> sortiranjePoCeniRastuce() {
		
		ArrayList<Karta> rezultati = new ArrayList<>();
		
		rezultati.addAll(this.getKarte().getKarte());
		
		Collections.sort(rezultati, new Comparator<Karta>() {
			
			@Override
			public int compare(Karta k1, Karta k2) {
				
				if( k1.getCena()< k2.getCena()){
					return -1;
				}
				return 1;
		
			}
			
		});
		return rezultati;
	}
	
	/**** FILTRIRANJE PO TIPU *****/
	@GET
	@Path("/filterRegular")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Karta> filtriranjeRegularKarti() {
		
		ArrayList<Karta> rezultati = new ArrayList<>();
		//pretrazujemo iz liste svih karata karte sa prosledjenim nazivom manifestacije
		for(Karta k : this.getKarte().getKarte()) {
			if(k.getTipKarte() == Karta.TipKarte.REGULAR) {
				rezultati.add(k);
			}
		}
		return rezultati;
	}
	
	@GET
	@Path("/filterFanPit")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Karta> filtriranjeFanPitKarti() {
		
		ArrayList<Karta> rezultati = new ArrayList<>();
		//pretrazujemo iz liste svih karata karte sa prosledjenim nazivom manifestacije
		for(Karta k : this.getKarte().getKarte()) {
			if(k.getTipKarte() == Karta.TipKarte.FAN_PIT) {
				rezultati.add(k);
			}
		}
		return rezultati;
	}
	
	@GET
	@Path("/filterVip")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Karta> filtriranjeVipKarti() {
		
		ArrayList<Karta> rezultati = new ArrayList<>();
		//pretrazujemo iz liste svih karata karte sa prosledjenim nazivom manifestacije
		for(Karta k : this.getKarte().getKarte()) {
			if(k.getTipKarte() == Karta.TipKarte.VIP) {
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
