package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.Korisnici;
import model.Karta;
import model.Korisnik;
import model.Kupac;
import model.Manifestacija;
import model.Prodavac;
import model.TipKupca;

@Path("registracija")
public class Registracija {
	
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/")
	public void preuzmiFajl() throws IOException {
		this.procitajFajl(ctx.getRealPath(""));
	}

	@POST
	@Path("/registracijaKupaca")
	//@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.APPLICATION_JSON)
	public void registracijaKupaca(String kupac) throws JsonParseException, JsonMappingException, IOException {
		
		System.out.println("Pogodjen sam..............");
		System.out.println(kupac);
		
		try {
			Kupac k = new ObjectMapper().readValue(kupac, Kupac.class);
			k.setSakupljeniBodovi(0);
			k.setSveKarte(new ArrayList<Karta>());
			k.setTip(new TipKupca());
			System.out.println("Podatak: " + k.toString());
			
			//dodajemo novog korisnika
			this.getKorisnici().getKorisnici().add(k);
			
			System.out.println("******************** OVOO SU SVI KORISNICI NAKON DODAVANJA ****************\n\n");
			for(Korisnik kor : this.getKorisnici().getKorisnici()) {
				System.out.println(kor.toString());
			}
			System.out.println("******************************************************************************");
			//prepisujemo fajl sa dodatim novim korisnikom >> treba da pregazimo sve ne u apend rezimu da radimo.
			this.getKorisnici().upisiSveKorisnike(ctx.getRealPath(""));
			//this.upisiSveKorisnike(ctx.getRealPath(""),this.getKorisnici().getKorisnici());
			
			
			System.out.println("**************************************__________________________________");
			//sad procitamo sadrzaj fajla da vidimo sta je upisano
			System.out.println("Ovo su svi korisnici koji se nalaze u listi: ");
			for( Korisnik kor: this.getKorisnici().getKorisnici()) {
				System.out.println(kor.toString());
			}
			
			
			this.procitajFajl(ctx.getRealPath(""));
			
			
			
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		
	}
	@POST
	@Path("/registracijaProdavaca")
	//@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.APPLICATION_JSON)
	public void registracijaProdavaca(String prodavac) throws JsonParseException, JsonMappingException, IOException {
		
		System.out.println("Pogodjen sam..............");
		System.out.println(prodavac);
		
		try {
			Prodavac p = new ObjectMapper().readValue(prodavac, Prodavac.class);
			p.setManifestacije(new ArrayList<Manifestacija>());
			System.out.println("Podatak: " + p.toString());
			
			//dodajemo novog korisnika
			this.getKorisnici().getKorisnici().add(p);
			
			System.out.println("******************** OVOO SU SVI KORISNICI NAKON DODAVANJA ****************\n\n");
			for(Korisnik kor : this.getKorisnici().getKorisnici()) {
				System.out.println(kor.toString());
			}
			System.out.println("******************************************************************************");
			//prepisujemo fajl sa dodatim novim korisnikom >> treba da pregazimo sve ne u apend rezimu da radimo.
			this.getKorisnici().upisiSveKorisnike(ctx.getRealPath(""));
			//this.upisiSveKorisnike(ctx.getRealPath(""),this.getKorisnici().getKorisnici());
			
			
			System.out.println("**************************************__________________________________");
			//sad procitamo sadrzaj fajla da vidimo sta je upisano
			System.out.println("Ovo su svi korisnici koji se nalaze u listi: ");
			for( Korisnik kor: this.getKorisnici().getKorisnici()) {
				System.out.println(kor.toString());
			}
			
			
			this.procitajFajl(ctx.getRealPath(""));
			
			
			
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		
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
	
	public void upisiSveKorisnike(String putanja, ArrayList<Korisnik> korisnici) {
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter(new FileWriter(putanja + "/utils/korisnici.txt"));
			pw.println("jana123;jana;ana;anic;ZENSKI;2020-03-03;ADMINISTRATOR;");
			pw.flush();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally {
			if(pw != null) {
				pw.close();

			}		
		}
	}
	public void procitajFajl(String putanja) throws IOException {
		BufferedReader buf = new BufferedReader(new FileReader(putanja +"/utils/korisnici.txt" ));
		
		String linija = "";
		
		System.out.println("\n\n _____________procitano______________");
		while((linija= buf.readLine())!= null) {
			System.out.println(linija);
		}
	}
}
