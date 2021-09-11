package services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.Komentari;
import model.Komentar;
import model.Komentar.Status;

@Path("komentari")
public class KomentariServis {
	
	@Context
	ServletContext ctx;
	
	
	/** DODAVANJE NOVOG KOMENTARA  *********/
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/noviKomentar")
	public Komentar noviKomentarDodavanje(String noviKomentar) throws JsonParseException, JsonMappingException, IOException{
		Komentar komentar = null;
		try {
			komentar = new ObjectMapper().readValue(noviKomentar, Komentar.class);
			System.out.println("Komentar je: " + komentar.toString());
			
			//doadajemo ga u listu svih komentara
			this.getKomentari().getKomentari().add(komentar);
			
			//ponovo upisemo sve komentare u fajl..
			this.getKomentari().upisiSveKomentare(ctx.getRealPath(""));
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		//ucitavamo sve komentare iz fajla..
		
		System.out.println("     >>>>>>>>>>>    ISPIS SVIH DODATIH KOMENTARA: ");
		
		for(Komentar k : this.getKomentari().getKomentari()) {
			System.out.println("   " + k.toString());
		}
		return komentar;
	}
	
	/*** vraca sve komentare *********/
	@GET
	@Path("/sviKomentari")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Komentar> sviKomentari(){
		
		return this.getKomentari().getKomentari();
	}
	
	public Komentari getKomentari() {
		Komentari komentari = (Komentari) ctx.getAttribute("komentari");
		
		if(komentari == null) {
			komentari = new Komentari(ctx.getRealPath(""));
			
			//System.out.println("putanja: " + ctx.getRealPath("") + System.lineSeparator());
			ctx.setAttribute("komentari", komentari);
		}
		return komentari;
	}
	
	@PUT
	@Path("/odobravanjeKomentara/{kupacKorisnickoIme}/{manifestacijaNaziv}/{manifestacijaDatum}/{textKomentara}/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public Komentar odobravanjeKomentara(@PathParam("kupacKorisnickoIme")String kupacKorisnickoIme, 
			
			@PathParam("manifestacijaNaziv")String manifestacijaNaziv, @PathParam("manifestacijaDatum")String manifestacijaDatum,
			@PathParam("textKomentara")String textKomentara, @PathParam("status")String status) {
				
		System.out.println("^^^^^^^          KOmentari: "+ kupacKorisnickoIme + manifestacijaNaziv + ", " + manifestacijaDatum + ", " + textKomentara + ", " + status);
		Komentar komentar = null;
		
		for(Komentar k : this.getKomentari().getKomentari()) {
			
			
			System.out.println("----------------------------------------------------");
			System.out.println(k.getKupacKomentator().getKorisnickoIme() + ", " + kupacKorisnickoIme);
			System.out.println(k.getManifestacija().getNaziv() + ", " + manifestacijaNaziv);
			System.out.println(k.getManifestacija().getDatumIvremeOdrzavanja() + ", " + manifestacijaDatum);
			System.out.println(k.getTextKomentara() + ", " + textKomentara);
			System.out.println("-------------------------------------------------------");
			
			if(k.getKupacKomentator().getKorisnickoIme().equals(kupacKorisnickoIme) &&
					k.getManifestacija().getNaziv().equals(manifestacijaNaziv) &&
					(k.getManifestacija().getDatumIvremeOdrzavanja()+"").equals(manifestacijaDatum.substring(0, manifestacijaDatum.length()-3)) &&
					k.getTextKomentara().equals(textKomentara)) {
				
				System.out.println("?????????????????         Jednakost zadovoljena...............");
				if(status.equals("ODBIJEN")) {
					k.setStatus(Status.ODBIJEN);
					komentar = k;
				}
				else if(status.equals("ODOBREN")) {
					k.setStatus(Status.ODOBREN);
					komentar = k;
				}
			}
		}
		
		this.getKomentari().upisiSveKomentare(ctx.getRealPath(""));;
		System.out.println("88888888888888888888888888888888888888888888");
		System.out.println("KOmentar Je:  " + komentar.toString());
		
		System.out.println("(((((((((((((((((((((((((((((((((((((((((((((((((((( KOMENTARRI))))))))))))))))))))))))");
		for(Komentar k: this.getKomentari().getKomentari()) {
			System.out.println(k.toString());
			System.out.println();
		}
		
		return komentar;

	}
}
