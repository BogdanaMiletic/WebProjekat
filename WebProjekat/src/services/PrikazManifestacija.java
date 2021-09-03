package services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.Manifestacije;
import model.Kupac;
import model.Manifestacija;

@Path("manifestacije")
public class PrikazManifestacija {
	
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Manifestacija> sveManifestacije() {
		Manifestacije manifestacijeSve = (Manifestacije)this.getManifestacije();
		
		//sad sortiramo te manifestacije tako da prvo budu one najskorije
		List<Manifestacija> manifestacijeSortirane = (List)manifestacijeSve.getManifestacije();
		Comparator poDatumuOdrzavanja = new Comparator() {
		

			@Override
			public int compare(Object o1, Object o2) {
				if((((Manifestacija) o1).getDatumIvremeOdrzavanja()).isBefore(((Manifestacija) o2).getDatumIvremeOdrzavanja())) {
					return 1;
				}
				else {
					return -1;
				}
			}
		};
		
		Collections.sort(manifestacijeSortirane, poDatumuOdrzavanja);
		
		return manifestacijeSortirane;
	}
	
	/*
	 * metoda za pretragu manifestacija po raznim kriterijumima.
	 */
	
	@GET
	@Path("/pretraga")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Manifestacija> pretragaManifestacija(@QueryParam("naziv")String naziv, @QueryParam("lokacija")String lokacija,
			@QueryParam("datumOd")String datumOd, @QueryParam("datumDo")String datumDo, 
			@QueryParam("cenaOd") String cenaOd, @QueryParam("cenaDo")String cenaDo,
			@QueryParam("sortiranje") String sortiranje){
		
		ArrayList<Manifestacija> manifestacijeRezultati = new ArrayList<>();
		manifestacijeRezultati.addAll(this.getManifestacije().getManifestacije());
		
		System.out.println("Lokacija" + lokacija);
		System.out.println("Datum od: " + datumOd.toString());
		System.out.println("Datum do: " + datumDo.toString());
		System.out.println("Cena od: " + cenaOd);
		System.out.println("Cena do: " + cenaDo);
		
		if(!naziv.equals(""))  {
			//radimo pretragu po nazivu
			for(Manifestacija m : this.getManifestacije().getManifestacije()) {
				if(!m.getNaziv().equalsIgnoreCase(naziv.trim())) {
					manifestacijeRezultati.remove(m);
				}
			}
		}
		if(!lokacija.equals("")) {
			//radimo  pretragu po lokaciji..
			System.out.println("///////////////////////////////////////////////////////////////lokkkkacija");
			for(Manifestacija m : this.getManifestacije().getManifestacije()) {
				String adresa = m.getLokacija().getAdresa();
				String adresaSplit[] = adresa.split("\\,");
				System.out.println("Grad je: " + adresaSplit[1]);
				if(!adresaSplit[1].equalsIgnoreCase(lokacija.trim())) {
					//obrisi je iz ove liste rezultata ovu manifestaciju ukoliko je sadrzimanifestacija
					if(manifestacijeRezultati.contains(m))
						manifestacijeRezultati.remove(m);
				}
			}
		}
		if(!datumOd.equals("") || !datumDo.equals("")) {
			//radimo pretragu po opsegu datuma..
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime datumOdKastovan = LocalDateTime.parse(datumOd.replace("T", " "),format);
			LocalDateTime datumDoKastovan = LocalDateTime.parse(datumDo.replace("T", " "),format);
			
			for(Manifestacija m: this.getManifestacije().getManifestacije()) {
				if(!((m.getDatumIvremeOdrzavanja().isAfter(datumOdKastovan) && 
						m.getDatumIvremeOdrzavanja().isBefore(datumDoKastovan)) || 
						m.getDatumIvremeOdrzavanja().equals(datumOdKastovan) ||
						m.getDatumIvremeOdrzavanja().equals(datumDoKastovan))) {
					
					//ako ne odgoovara ovom upitu onda je samo obrisemo iz liste..
					if(manifestacijeRezultati.contains(m))
						manifestacijeRezultati.remove(m);
					
					
					
				}
			}
		}
		
		if(!cenaOd.equals("") || !cenaDo.equals("")) {
			//radimo pretragu po ceni.
			double cenaOdKast = Double.parseDouble(cenaOd);
			double cenaDoKast = Double.parseDouble(cenaDo);
			
			for(Manifestacija m : this.getManifestacije().getManifestacije()) {
				if(!((m.getCenaRegularKarte() > cenaOdKast && m.getCenaRegularKarte() < cenaDoKast) ||
					m.getCenaRegularKarte()==cenaOdKast || m.getCenaRegularKarte()==cenaDoKast)){
						if(manifestacijeRezultati.contains(m)) {
							manifestacijeRezultati.remove(m);
						}
					}
			}
		}
		// SORTIRANJE
		if(sortiranje.equals("NAZIV")) {
			manifestacijeRezultati = this.sortiranjePoNazivu(manifestacijeRezultati);
		}
		else if(sortiranje.equals("DATUM")) {
			manifestacijeRezultati = this.sortiranjePoDatumu(manifestacijeRezultati);
		}
		else if(sortiranje.equals("CENA")) {
			manifestacijeRezultati = this.sortiranjePoCeni(manifestacijeRezultati);
		}
		else if(sortiranje.equals("LOKACIJA")) {
			manifestacijeRezultati = this.sortiranjePoLokaciji(manifestacijeRezultati);
		}
		
		
		
		
		return manifestacijeRezultati;
	}
	
	//********** SORTIRANJE **********
	// sortiranje po nazivu
	private ArrayList<Manifestacija> sortiranjePoNazivu(ArrayList<Manifestacija> manifestacije) {
		
		Collections.sort(manifestacije, new Comparator<Manifestacija>() {
			
			@Override
			public int compare(Manifestacija m1, Manifestacija m2) {
				
				return m1.getNaziv().compareToIgnoreCase(m2.getNaziv());
			}
			
		});
		
		return manifestacije;
	}
	// sortiranje po datumu
	private ArrayList<Manifestacija> sortiranjePoDatumu(ArrayList<Manifestacija>manifestacije){
		Collections.sort(manifestacije, new Comparator<Manifestacija>() {
			
			@Override
			public int compare(Manifestacija m1, Manifestacija m2) {
				
				return m2.getDatumIvremeOdrzavanja().compareTo(m1.getDatumIvremeOdrzavanja());
			}
			
		});
		
		return manifestacije;
	}
	//sortiranje po ceni karte
	private ArrayList<Manifestacija> sortiranjePoCeni(ArrayList<Manifestacija> manifestacije){
		Collections.sort(manifestacije, new Comparator<Manifestacija>(){

			@Override
			public int compare(Manifestacija o1, Manifestacija o2) {
				if(o1.getCenaRegularKarte() < o2.getCenaRegularKarte())
					return 1;
					else
						return -1;
			}
			
		});
		return manifestacije;
	}
	//sortiranje prema lokaciji
	private ArrayList<Manifestacija> sortiranjePoLokaciji(ArrayList<Manifestacija> manifestacije){
		Collections.sort(manifestacije, new Comparator<Manifestacija>() {

			@Override
			public int compare(Manifestacija o1, Manifestacija o2) {
				return o1.getLokacija().getAdresa().compareToIgnoreCase(o2.getLokacija().getAdresa());
			}
			
		});
		return manifestacije;
	}
	
	
	
	private Manifestacije getManifestacije() {
		Manifestacije manifestacije = (Manifestacije) ctx.getAttribute("manifestacije");
		
		if(manifestacije == null) {
			manifestacije = new Manifestacije(ctx.getRealPath(""));
			
			System.out.println("putanja: " + ctx.getRealPath("") + System.lineSeparator());
			ctx.setAttribute("manifestacije", manifestacije);
		}
		return manifestacije;
	}
}
