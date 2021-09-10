package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.Karta;
import model.Korisnik;
import model.Kupac;
import model.Manifestacija;
import model.Prodavac;
import model.TipKupca;
import model.Korisnik.Uloga;
import model.TipKupca.ImeTipa;

public class Karte {
private ArrayList<Karta> karte = new ArrayList<>();
	
	
	public Karte() {
		//ovdde mozemo pozvati defaultnu putanju ako zelimo iz praznog konstruktora...
	}
	
	public Karte(String putanja) {
		System.out.println("Upali smo....");
		BufferedReader in = null;
		try {
			File file = new File(putanja +"/utils"+"/karte.txt") ;
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
					
					String id = podaci[0];
					System.out.println(id);
					String nazivManifestacije = podaci[1];
					System.out.println(nazivManifestacije);
					DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					LocalDateTime datum = LocalDateTime.parse(podaci[2].replace("T", " "), format);
					System.out.println(datum);
					
					double cena = Double.parseDouble(podaci[3]);
					
					String imeIprezimeSplit[]= podaci[4].split("\\,");
					String ime = imeIprezimeSplit[0];
					String prezime = imeIprezimeSplit[1];
					System.out.println("Ime i prezime :" + ime + ", "+ prezime );
					
					Karta.Status status = null;
					
					if( podaci[5].equals("REZERVISANA")) {
						status = Karta.Status.REZERVISANA;
					}
					else {
						status = Karta.Status.ODUSTANAK;
					}
					
					Karta.TipKarte tipKarte = null;
					
					if(podaci[6].equals("REGULAR")) {
						tipKarte = Karta.TipKarte.REGULAR;
					}
					else if(podaci[6].equals("FAN_PIT")) {
						tipKarte = Karta.TipKarte.FAN_PIT;
					}
					else if(podaci[6].equals("VIP")) {
						tipKarte = Karta.TipKarte.VIP;
					}
					
					//DODAJEMO KARTU>> UPISUJEMO JE..
					Karta karta = new Karta(id, nazivManifestacije, datum, cena, podaci[4], status, tipKarte);
					karte.add(karta);
				}
				
			}
			
			
			//sad ispisemo sve sto smo dodali..
			
			System.out.println("************************************");
			System.out.println("Dodato je: ");
			for(Karta k : this.karte) {
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
		
			pw = new PrintWriter(new FileWriter(putanja  + "/utils/karte.txt", false));
			
			for(Karta k : this.getKarte()) {
				String status = "REZERVISANA";
				if(k.getStatus() == Karta.Status.ODUSTANAK) {
					status = "ODUSTANAK";
				}
				
				String tipKarte = "REGULAR";
				if(k.getTipKarte() == Karta.TipKarte.FAN_PIT) {
					tipKarte = "FAN_PIT";
				}
				else if(k.getTipKarte() == Karta.TipKarte.VIP) {
					tipKarte = "VIP";
				}
				
				//******** ISPRAVAN FORMAT DATUMA *********
				//pravimo string od datuma
				String datumFormatiranje  = k.getDatumIvremeManifestaije()+"";
				datumFormatiranje.replace("T", " ");
				
				
				System.out.println("\t>>>>>>>>>>>>>>>>>Datum i vrmee upississsssssssssssss: " + k.getDatumIvremeManifestaije());
				
				pw.println(k.getId()+ ";" + k.getManifestacijaZaKojuJeRezervisana() + ";" + datumFormatiranje + ";" + k.getCena() + ";" + k.getKupacImeIprezime() + ";" + status + ";" + tipKarte + ";");
				System.out.println("NOva: "+ k.getId()+ ";" + k.getManifestacijaZaKojuJeRezervisana() + ";" + datumFormatiranje + ";" + k.getCena() + ";" + k.getKupacImeIprezime() + ";" + status + ";" + tipKarte + ";");
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

	public ArrayList<Karta> getKarte() {
		return karte;
	}

	public void setKarte(ArrayList<Karta> karte) {
		this.karte = karte;
	}	
}
