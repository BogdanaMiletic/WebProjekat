package services;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.Komentari;
import model.Komentar;

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
			this.getKomentari().upisiSveKarte(ctx.getRealPath(""));
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
	
	public Komentari getKomentari() {
		Komentari komentari = (Komentari) ctx.getAttribute("komentari");
		
		if(komentari == null) {
			komentari = new Komentari(ctx.getRealPath(""));
			
			//System.out.println("putanja: " + ctx.getRealPath("") + System.lineSeparator());
			ctx.setAttribute("komentari", komentari);
		}
		return komentari;
	}
	
	

}
