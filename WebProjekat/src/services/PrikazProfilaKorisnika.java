package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.Korisnici;
import model.Korisnik;
import model.Kupac;

@Path("profil")
public class PrikazProfilaKorisnika {
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Korisnik vratiTrenutnoUlogovanogKorisnika() {
		System.out.println("&&&&&&&&&&& Trenutni ulogovani korisnik je: " + ctx.getAttribute("trenutniKorisnik").toString());
		return (Korisnik) ctx.getAttribute("trenutniKorisnik");
	}
	
	@PUT
	@Path("/promeniPodatkeProfila")
	public void promeniPodatkeKorisnickogProfila(String podaci) {
		try {
			Korisnik k = new ObjectMapper().readValue(podaci, Korisnik.class); //ovo su izmenjeni podaci
			
			//preuzmemo onog koji je trenutno ulogovani i kad nadjemo tog medju svim korisnicima samo mu promenimo podatke..
			Korisnik trenutnoUlogovani = (Korisnik) ctx.getAttribute("trenutniKorisnik");
			
			for(Korisnik korisnik :  this.getKorisnici().getKorisnici()) {
				if (korisnik.getKorisnickoIme().equals(trenutnoUlogovani.getKorisnickoIme())) {
					korisnik.setIme(k.getIme());
					korisnik.setPrezime(k.getPrezime());
					korisnik.setKorisnickoIme(k.getKorisnickoIme());
					korisnik.setLozinka(k.getLozinka());
				}
			}
			//sad sve to prepisemo u fajlu jednom iz liste sve podatke pregazimo..
			this.getKorisnici().upisiSveKorisnike(ctx.getRealPath(""));
			
			//sad procitamo sve sto je upisano u taj fajll
			System.out.println("**************** TEST ***********************");
			this.procitajFajl(ctx.getRealPath(""));
			

		}
		catch(Exception ex) {
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
	
	public void procitajFajl(String putanja) throws IOException {
		BufferedReader buf = new BufferedReader(new FileReader(putanja +"/utils/korisnici.txt" ));
		
		String linija = "";
		
		System.out.println("\n\n _____________procitano______________");
		while((linija= buf.readLine())!= null) {
			System.out.println(linija);
		}
	}

}
