package services;

import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.Manifestacije;
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
