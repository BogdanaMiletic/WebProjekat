package services;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import dao.Karte;

public class KarteServis {
	
	@Context
	ServletContext ctx;
	
	
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
