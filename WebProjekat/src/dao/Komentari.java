package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import model.Karta;
import model.Komentar;
import model.Korisnik;
import model.Kupac;
import model.Manifestacija;

public class Komentari {
	private ArrayList<Komentar> komentari = new ArrayList<>();
	
	@Context
	ServletContext ctx;
	
	public Komentari() {
		//ovdde mozemo pozvati defaultnu putanju ako zelimo iz praznog konstruktora...
	}
	
	public Komentari(String putanja) {
		System.out.println("Upali smo....");
		BufferedReader in = null;
		try {
			File file = new File(putanja +"/utils"+"/komentari.txt") ;
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			procitajKarte(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if ( in != null ) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
	}
	
	/**
	 * Cita karte iz datoteke i smesta ih u listu svih karata
	 */
	public void procitajKarte(BufferedReader in) {
		String linija = "";
		
		try {
			while((linija = in.readLine()) != null) {
				linija = linija.trim();
				if(linija != "" || linija != "\n") {
					//linija = linija.substring(0,linija.length()-1); //odsecamo prelaz u novi red..
					System.out.println("Linija je: " + linija);
					System.out.println("-------------------------------");
					String podaci[] = linija.split("\\;");
					
					String kupacStr = podaci[0];
					System.out.println(kupacStr);
					
					String kupacInfo[] = kupacStr.split("\\,");
					String korisnickoIme = kupacInfo[0];
					String lozinka = kupacInfo[1];
					
					Kupac kupac = null;
					//pretrazimo tog kupca
					for(Korisnik k : this.getKorisnici().getKorisnici()) {
						if(k.getKorisnickoIme().equals(korisnickoIme) && k.getLozinka().equals(lozinka)) {
							kupac = (Kupac) k;
						}
					}
					
					String manifestacija[] = podaci[1].split("\\,");
					
					String nazivManifestacije = manifestacija[0];
					String datumManifestacije = manifestacija[1];
					
					
					DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					LocalDateTime datum = LocalDateTime.parse(datumManifestacije.replace("T", " "), format);
					
					Manifestacija manifestacijaKom = null;
					for(Manifestacija m: this.getManifestacije().getManifestacije()) {
						if(m.getDatumIvremeOdrzavanja().equals(datum) && m.getNaziv().equals(nazivManifestacije)) {
							manifestacijaKom = m;
						}
					}
					
					
					String textKomentara = podaci[2];
					
					int ocenaKomentar = Integer.parseInt(podaci[3]);
					
					
					//DODAJEMO KARTU>> UPISUJEMO JE..
					Komentar komentar = new Komentar(kupac, manifestacijaKom, textKomentara, ocenaKomentar);
					komentari.add(komentar);
				}
				
			}
			
			
			//sad ispisemo sve sto smo dodali..
			
			System.out.println("************************************");
			System.out.println("Dodato je: ");
			for(Komentar k : this.komentari) {
				System.out.println(k.toString());
				System.out.println("-------------------------------");
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}


	public void upisiSveKarte(String putanja) {
		PrintWriter pw = null;
		try {
		
			pw = new PrintWriter(new FileWriter(putanja  + "/utils/komentari.txt", false));
			
			for(Komentar k : this.komentari) {
				
				
				//******** ISPRAVAN FORMAT DATUMA *********
				//pravimo string od datuma
				String datumFormatiranje  = k.getManifestacija().getDatumIvremeOdrzavanja()+"";
				datumFormatiranje.replace("T", " ");
				
				
				System.out.println("\t>>>>>>>>>>>>>>>>>Datum i vrmee upississsssssssssssss: " + k.getManifestacija().getDatumIvremeOdrzavanja());
				
				pw.println(k.getKupacKomentator().getKorisnickoIme()+"," + k.getKupacKomentator().getLozinka() + ";" + k.getManifestacija().getNaziv() + "," + datumFormatiranje + ";" + k.getTextKomentara() + ";" + k.getOcena() + ";" );
				//System.out.println("NOva: "+ k.getId()+ ";" + k.getManifestacijaZaKojuJeRezervisana() + ";" + datumFormatiranje + ";" + k.getCena() + ";" + k.getKupacImeIprezime() + ";" + status + ";" + tipKarte + ";");
				System.out.println(" \t\tNovi komentar: "+ k.getKupacKomentator().getKorisnickoIme()+"," + k.getKupacKomentator().getLozinka() + ";" + k.getManifestacija().getNaziv() + "," + datumFormatiranje + ";" + k.getTextKomentara() + ";" + k.getOcena() + ";" );

				
				pw.flush();
			}				
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}	 
		finally {
			if ( pw != null ) {
				try {
					pw.close();
				}
				catch (Exception e) {
					System.out.println("***************Greska u zatvaranju fajla.....................................");
				}
			}
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
