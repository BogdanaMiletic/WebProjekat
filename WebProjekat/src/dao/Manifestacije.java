package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.Lokacija;
import model.Manifestacija;
import model.Manifestacija.Status;
import model.Manifestacija.TipManifestacije;

public class Manifestacije {
	public ArrayList<Manifestacija> manifestacije = new ArrayList<Manifestacija>();
	
	public Manifestacije() {
		
	}
	
	public Manifestacije(String putanja) {
		BufferedReader in = null;
		try {
			System.out.println("Citanje manifestacija....................");
			File file = new File(putanja +"/utils"+"/manifestacije.txt") ;
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			procitajManifestacije(in);
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
	 * Cita manifestacije iz datoteke i smesta ih u listu svih manifestacija
	 */
	public void procitajManifestacije(BufferedReader in) {
		String linija = "";
		
		try {
			while((linija = in.readLine()) != null) {
				linija = linija.trim();
				if(linija != "" || linija != "\n") {
					//linija = linija.substring(0,linija.length()-1); //odsecamo prelaz u novi red..
					System.out.println("Linija je: " + linija);
					System.out.println("-------------------------------");
					String podaci[] = linija.split("\\;");
					
					String naziv = podaci[0];
					System.out.println(naziv);
					TipManifestacije tip = null;
					
					if(podaci[1].equals("KONCERT")) {
						tip = TipManifestacije.KONCERT;
					}
					else if(podaci[1].equals("POZORISTE")) {
						tip = TipManifestacije.POZORISTE;
					}
					else if(podaci[1].equals("FESTIVAL")) {
						tip = TipManifestacije.FESTIVAL;
					}

					System.out.println(tip.toString());
					int brojMesta = Integer.parseInt(podaci[2]);
					System.out.println(brojMesta);
					DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					LocalDateTime datumIvreme = LocalDateTime.parse(podaci[3].replace("T",  " "), format);
					System.out.println(datumIvreme);
					double cenaRegularKarte = Double.parseDouble(podaci[4]);
					
					Manifestacija.Status status = null;
					if(podaci[5].equals("AKTIVNO")) {
						status = Manifestacija.Status.AKTIVNO;
					}
					else if(podaci[5].equals("NEAKTIVNO")) {
						status = Manifestacija.Status.NEAKTIVNO;
					}
					//parsiranje lokacije..
					String lokacija[] = podaci[6].split("\\|");
					double geografskaSirina = Double.parseDouble(lokacija[0]);
					double geografskaDuzina = Double.parseDouble(lokacija[1]);
					String adresa = lokacija[2];
					
					Manifestacija manifestacija = new Manifestacija(naziv, tip, brojMesta, datumIvreme,
							cenaRegularKarte, status, new Lokacija(geografskaSirina, geografskaDuzina, adresa));
				
			
					manifestacije.add(manifestacija);
				}
			
			}
			//sad ispisemo sve sto smo dodali..
			
			System.out.println("************************************");
			System.out.println("Dodate manifestacije su: ");
			for(Manifestacija m : this.manifestacije) {
				System.out.println(m.toString());
				System.out.println("-------------------------------");
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void upisiSveManifestacije(String putanja) {
		PrintWriter pw = null;
		try {
		
			pw = new PrintWriter(new FileWriter(putanja  + "/utils/korisnici.txt", false));
			
			for(Manifestacija m : this.getManifestacije()) {
				
				String status = "";
				if(m.getStatus() == Status.AKTIVNO) {
					status = "AKTIVNO";
				}
				else {
					status = "NEAKTIVNO";
				}
				
				String tip = "";
				if(m.getTip() == Manifestacija.TipManifestacije.FESTIVAL) {
					tip = "FESTIVAL";
				}
				else if(m.getTip() == Manifestacija.TipManifestacije.KONCERT) {
					tip = "KONCERT";
				}
				else if (m.getTip() == Manifestacija.TipManifestacije.POZORISTE) {
					tip = "POZORISTE";
				}
				
				String datumFormatiranje  = m.getDatumIvremeOdrzavanja()+"";
				datumFormatiranje.replace("T", " ");
				
				pw.println(m.getNaziv() + ";" + tip + ";" + m.getBrojMesta()
						+ ";" + datumFormatiranje + ";" + m.getCenaRegularKarte() + ";" 
						+ status + ";" +m.getLokacija().getGeografskaSirina() + "|" + m.getLokacija().getGeografskaDuzina() + "|" + m.getLokacija().getAdresa()  + ";");
				
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

	public ArrayList<Manifestacija> getManifestacije() {
		return manifestacije;
	}

	public void setManifestacije(ArrayList<Manifestacija> manifestacije) {
		this.manifestacije = manifestacije;
	}
}
