package services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.Korisnici;
import dao.Manifestacije;
import model.Korisnik;
import model.Manifestacija;
import model.Manifestacija.TipManifestacije;
import model.Prodavac;

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
		//List<Manifestacija> manifestacijeSortirane = (List)manifestacijeSve.getManifestacije();
		List<Manifestacija> manifestacijeSortirane = new ArrayList<>();
		manifestacijeSortirane.addAll(manifestacijeSve.getManifestacije());
		
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
	 * metoda za vracanje konkretne manifestacije
	*/
	@GET
	@Path("/{naziv}/{datum}/{lokacija}/{sirina}/{duzina}")
	@Produces(MediaType.APPLICATION_JSON)
	public Manifestacija pronadjiManifestaciju(@PathParam("naziv")String naziv, @PathParam("datum")String datum, @PathParam("lokacija")String lokacija,
			@PathParam("sirina")String sirina, @PathParam("duzina")String duzina) {
		//iz svih manifestacija vracamo onu ciji se podaci poklapaju sa parametrom putanje
		//prvo vrsimo redefinisanje datuma tako da dodamo 0 kod meseci i dana ako je jednocifren broj >> da bi bilo u skladu sa formatom
		System.out.println("Datum je:                :               " + datum );
		/*String podela[] = datum.split("\\ ");
		System.out.println("POdela: " + podela[2]);
		String podela2[] = (podela[0]).split("\\-");
		if(podela2[1].length() == 1)
			podela2[1] = "0"+podela2[1];
		if(podela2[2].length() == 1)
			podela2[2] = "0" + podela2[2];
		String noviDatum = podela2[0] +"-" + podela2[1]+"-"+ podela2[2] + " " + podela[2];*/
		String datumNovi = datum.replace("T", " ");
		String noviDatum = datumNovi.substring(0, datumNovi.length()-3);
		System.out.println("NOviiiiiiiii: " + noviDatum);
				
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime datumParsiran = LocalDateTime.parse(noviDatum, format);
		
		for(Manifestacija m : this.getManifestacije().getManifestacije()) {
			if(m.getNaziv().equals(naziv) && m.getDatumIvremeOdrzavanja().equals(datumParsiran) 
					&& m.getLokacija().getAdresa().equals(lokacija))
				return m;
		}
		
		return null;
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
			@QueryParam("sortiranje") String sortiranje, @QueryParam("filtriranjeTip")String tipFiltriranje){
		
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
		
		// FILTRIRANJE
		System.out.println("tip je: " + tipFiltriranje);
		manifestacijeRezultati = filtriranjePremaTipu(manifestacijeRezultati, tipFiltriranje);
		
		
		
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
	
	//************ FILTRIRANJE **************
	//filtriranje manifesatacija prema tipu 
	private ArrayList<Manifestacija> filtriranjePremaTipu(ArrayList<Manifestacija> manifestacije, String tipFiltriranje){
		ArrayList<Manifestacija> filtrirano = new ArrayList<Manifestacija>();
		
		if(tipFiltriranje.equals("KONCERT")) {
			for(Manifestacija m : manifestacije) {
				if(m.getTip() == Manifestacija.TipManifestacije.KONCERT)
					filtrirano.add(m);
			}
		}
		else if(tipFiltriranje.equals("POZORISTE")) {
			for(Manifestacija m : manifestacije) {
				if(m.getTip() == TipManifestacije.POZORISTE)
					filtrirano.add(m);
			}
		}
		else if(tipFiltriranje.equals("FESTIVAL")) {
			for(Manifestacija m : manifestacije) {
				if(m.getTip() == TipManifestacije.FESTIVAL)
					filtrirano.add(m);
			}
		}
		else {
			filtrirano = manifestacije;
		}
		return filtrirano;
	}
	
	@POST
	@Path("/dodajManifestaciju")
	public void dodajNovuManifestaciju(String m) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("Pogodjeniii smooooooooooooo: " + m);
		try {
			Manifestacija manifestacija = (Manifestacija) new ObjectMapper().readValue(m, Manifestacija.class);
			//System.out.println("Parsirano: " + manifestacija.toString());
			
			//dodajemo manifestaicju u listu
			this.getManifestacije().getManifestacije().add(manifestacija);
			
			this.getManifestacije().upisiSveManifestacije(ctx.getRealPath(""));
			
			//**********upisujemo kod korisnika koji je prodavac u listu svih njegovih dodatih manifestacija ovu novu dodatu
			//ko je ulogovan?
			Korisnik ulogovanTrenutno = (Korisnik) ctx.getAttribute("trenutniKorisnik");

			for(Korisnik korisnik: this.getKorisnici().getKorisnici()) {
				if(korisnik.equals(ulogovanTrenutno)) {
					Prodavac prodavac = (Prodavac)korisnik;
					prodavac.getManifestacije().add(manifestacija);
				}
			}
			//na kraju prepisemo i sve korisnike u fajlu... >>
			this.getKorisnici().upisiSveKorisnike(ctx.getRealPath(""));
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		System.out.println("\t\t********* DODATE MANIFESTACIJE **********");
		
		for(Manifestacija m1: this.getManifestacije().getManifestacije()) {
			System.out.println(m1.toString());
			System.out.println("------------------------------------");
		}
		System.out.println("\t\t******Prodavac je: " + ((Korisnik) ctx.getAttribute("trenutniKorisnik")).toString());
	}
	
	
	@PUT
	@Path("/izmeniManifestaciju/{geografskaSirina}/{geografskaDuzina}/{datum}")
	//@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Manifestacija izmeniPodatkeManifestacije(@PathParam("datum")String datum, 
			@PathParam("geografskaSirina")String geografskaSirina, @PathParam("geografskaDuzina") String geografskaDuzina, String noviPodaci ) throws JsonParseException, JsonMappingException, IOException {
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		//formatiranje datuma
		String datumNovi = datum.replace("T", " ");
		String novi = datumNovi.substring(0, datumNovi.length()-3);
		
		LocalDateTime datumParsiran = LocalDateTime.parse(novi, format);
		Manifestacija mani = null;
		
		Manifestacija izmenjeniPodaci = null;
		try {
			izmenjeniPodaci = new ObjectMapper().readValue(noviPodaci, Manifestacija.class);
			System.out.println("Izmenjeni podaci su: " + izmenjeniPodaci.toString());
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		for(Manifestacija m : this.getManifestacije().getManifestacije()) {
			if (m.getDatumIvremeOdrzavanja().equals(datumParsiran) && 
					m.getLokacija().getGeografskaDuzina() == Double.parseDouble(geografskaDuzina)&&
					m.getLokacija().getGeografskaSirina() == Double.parseDouble(geografskaSirina)) {
				
				System.out.println("Pronadjne: " + m.toString());
				m.setNaziv(izmenjeniPodaci.getNaziv());
				m.setTip(izmenjeniPodaci.getTip());
				m.setBrojMesta(izmenjeniPodaci.getBrojMesta());
				m.setCenaRegularKarte(izmenjeniPodaci.getCenaRegularKarte());
				m.setDatumIvremeOdrzavanja(izmenjeniPodaci.getDatumIvremeOdrzavanja());
				m.setStatus(izmenjeniPodaci.getStatus());
				m.setLokacija(izmenjeniPodaci.getLokacija());
				mani = m;
	
			}
		}
		
		//vrsimo upis nove liste izmenjenih manifestacija u fajl..
		this.getManifestacije().upisiSveManifestacije(ctx.getRealPath(""));
		
		return mani;
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
	
	private Korisnici getKorisnici() {
		Korisnici korisnici = (Korisnici) ctx.getAttribute("korisnici");
		
		if(korisnici == null) {
			korisnici = new Korisnici(ctx.getRealPath(""));
			
			//System.out.println("putanja: " + ctx.getRealPath("") + System.lineSeparator());
			ctx.setAttribute("korisnici", korisnici);
		}
		return korisnici;
	}
}
