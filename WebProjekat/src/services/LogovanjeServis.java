package services;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.Korisnici;
import dao.Testiranje;
import model.Korisnik;

@Path("korisnici")
public class LogovanjeServis {
	
	@Context
	ServletContext ctx;
	
	private ObjectMapper objMaper = new ObjectMapper();
	
	//http metoda za logovanje
	
	@POST
	@Path("/logovanje")
	@Produces(MediaType.APPLICATION_JSON)
	public Korisnik logovanje(String podatak){
		
		String podaci[] = podatak.split("\\|");
		String korisnickoIme = podaci[0];
		String lozinka = podaci[1];
		Korisnik kor = new Korisnik();
		
		Korisnici sviKorisnici = (Korisnici) this.getKorisnici();
		
		for(Korisnik k : sviKorisnici.getKorisnici()) {
			if(k.getKorisnickoIme().equals(korisnickoIme) && k.getLozinka().equals(lozinka)) {
				kor = k;
				//>>postavi sesiju koja je aktivna na trenutnog korisnika.... >> da znamo ko je trenutni korisik...
				ctx.setAttribute("trenutniKorisnik", k);
			}
		}
		
		return kor;
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Korisnik> sviKorisnici() {
		Korisnici sviKorisnici = (Korisnici) this.getKorisnici();
		
		return sviKorisnici.getKorisnici();
	}
	
	@GET
	@Path("/trenutnoUlogovan")
	@Produces(MediaType.APPLICATION_JSON)
	public Korisnik koJeTrenutnoUlogovan() {
		return (Korisnik) ctx.getAttribute("trenutniKorisnik");
	}
	
	@POST
	@Path("/testiranje")
	public void testiranje(String podatak) throws IOException {
		
	
		this.getTest().getSadrzajLista().add(podatak);
		this.getTest().upisiSadrzaj(ctx.getRealPath(""));
			
	
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
	private Testiranje getTest() {
		
		Testiranje korisnici = (Testiranje) ctx.getAttribute("test");
		
		if(korisnici == null) {
			korisnici = new Testiranje(ctx.getRealPath(""));
			
			//System.out.println("putanja: " + ctx.getRealPath("") + System.lineSeparator());
			ctx.setAttribute("test", korisnici);
		}
		return korisnici;
	}
}
