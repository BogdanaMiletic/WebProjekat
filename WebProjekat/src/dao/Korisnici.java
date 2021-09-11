package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import model.Karta;
import model.Korisnik;
import model.Korisnik.Uloga;
import model.Kupac;
import model.Manifestacija;
import model.Prodavac;
import model.TipKupca;
import model.TipKupca.ImeTipa;

public class Korisnici {
	private ArrayList<Korisnik> korisnici = new ArrayList<>();
	private Karte ucitaneKarte = null;
	
	
	
	public Korisnici() {
		//ovdde mozemo pozvati defaultnu putanju ako zelimo iz praznog konstruktora...
	}
	
	public Korisnici(String putanja) {
		BufferedReader in = null;
		try {
			//prvo ucitavamo karte iz fajla.. 
			ucitaneKarte = new Karte(putanja); 
					
			File file = new File(putanja +"/utils"+"/korisnici.txt") ;
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			procitajKorisnike(in);
			
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
	 * Cita korisnike iz datoteke i smesta ih u listu svih korisnika
	 */
	public void procitajKorisnike(BufferedReader in) {
		String linija = "";
		
		try {
			while((linija = in.readLine()) != null) {
				linija = linija.trim();
				if(linija != "" || linija != "\n") {
					//linija = linija.substring(0,linija.length()-1); //odsecamo prelaz u novi red..
					System.out.println("Linija je: " + linija);
					System.out.println("-------------------------------");
					String podaci[] = linija.split("\\;");
					
					String korisnickoIme = podaci[0];
					System.out.println(korisnickoIme);
					String lozinka = podaci[1];
					System.out.println(lozinka);
					String ime = podaci[2];
					System.out.println(ime);
					String prezime = podaci[3];
					System.out.println(prezime);
					Korisnik.Pol pol = null;
					
					if( podaci[4].equals("muski")) {
						pol = Korisnik.Pol.MUSKI;
					}
					else {
						pol = Korisnik.Pol.ZENSKI;
					}
					
					LocalDate datum = LocalDate.parse(podaci[5]);
					
					Korisnik.Uloga uloga= null;
					
					if(podaci[6].trim().equalsIgnoreCase("administrator")) {
						//dodajemo administratora
						uloga = Korisnik.Uloga.ADMINISTRATOR;
						Korisnik admin = new Korisnik(korisnickoIme,lozinka, ime, prezime, pol, datum, uloga);
						korisnici.add(admin);
						
					}
					else if( podaci[6].trim().equalsIgnoreCase("kupac")) {
						System.out.println("77777777777777777 Linija: " + linija);
						//dodajemo kupca
						uloga = Korisnik.Uloga.KUPAC;
						//sve karte ucitavamo, sakupljeni bodovi, plus tip kupca...
						Korisnik kup = new Korisnik(korisnickoIme, lozinka,ime, prezime, pol, datum,uloga);
						//citamo za korisnika karte
						String idKarata[] = podaci[7].split("\\,");
						
						
						System.out.println("3333333333333333333333333 --- CITANJE KARATA UCITANIH IZ FAJLA");
						for(Karta k: this.ucitaneKarte.getKarte()) {
							System.out.println(k.toString());
							System.out.println("----------------------");
						}
						ArrayList<Karta> karte = new ArrayList<>();
						
						for(String id : idKarata) {
							
							if(this.ucitaneKarte.getKarte().size() != 0) {
								for(Karta k :this.ucitaneKarte.getKarte()) {
									if(id.equals(k.getId())) {
										karte.add(k);
									}
								}
							}
						}
						
						//????????????????? ovde odraditi izmenu da ucitava sve karte korisnika.....
						//////////////?????????????????????????????????????????????????????
						
						int brojBodova = Integer.parseInt(podaci[8]);
						System.out.println("////////////////////////////8 "+ podaci[8] );
						System.out.println("/////////////////////////"+ podaci[9]);
						
						TipKupca.ImeTipa tipKupca = null;
						if(podaci[9].equals("BRONZANI")) {
							tipKupca = TipKupca.ImeTipa.BRONZANI;
						}
						else if(podaci[9].equals("SREBRNI")) {
							tipKupca = TipKupca.ImeTipa.SREBRNI;
						}
						else if(podaci[9].equals("ZLATNI")) {
							tipKupca = TipKupca.ImeTipa.ZLATNI;
						}
						TipKupca tip = new TipKupca();
						tip.setImeTipa(tipKupca);
						
						//ovde treba upisati tipKupca
						Kupac kupac = new Kupac(kup, karte ,brojBodova, tip);
						
						//??? moramo im postaviti dodatna polja..
						korisnici.add(kupac);
					}
					else if(podaci[6].trim().equalsIgnoreCase("prodavac")) {
						//dodajemo prodavca
						uloga = Korisnik.Uloga.PRODAVAC;
						//treba ucitati sve manifestacije..
						//cuvacemo id-ijeve manifestacija u fajlovima..
						Korisnik kup = new Korisnik(korisnickoIme, lozinka,ime, prezime, pol, datum,uloga);
						Prodavac prodavac = new Prodavac(kup,new ArrayList<Manifestacija>());
						
						korisnici.add(prodavac);
					}
				}
				
			}
			
			
			//sad ispisemo sve sto smo dodali..
			
			System.out.println("************************************");
			System.out.println("Dodato je: ");
			for(Korisnik k : this.korisnici) {
				System.out.println(k.toString());
				System.out.println("-------------------------------");
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public Korisnik pronadjiKorisnika(String korisnickoIme, String lozinka) {
		for(Korisnik k : korisnici) {
			if(k.getKorisnickoIme().equals(korisnickoIme) && k.getLozinka().equals(lozinka)) {
				return k;
			}
		}
		return null;
	}
	
	public void upisiSveKorisnike(String putanja) {
		PrintWriter pw = null;
		try {
		
			pw = new PrintWriter(new FileWriter(putanja  + "/utils/korisnici.txt", false));
			
			for(Korisnik k : this.getKorisnici()) {
				if(k.getUloga()== Uloga.ADMINISTRATOR) {
					pw.println(k.getKorisnickoIme() + ";" + k.getLozinka() + ";" + k.getIme() + ";" + k.getPrezime() + ";" + k.getPol() + ";" + k.getDatumRodjenja() + ";" + k.getUloga()+ ";");
					pw.flush();
				}
				else if(k.getUloga() == Uloga.KUPAC) {
					 
					String karte = "";
					
					Kupac kupac = (Kupac) k;
					for(Karta karta : kupac.getSveKarte()) {
						//uzmi id-karte i dodaj ga ovde..
						karte += karta.getId() + ",";
					}
					String karteDodavanje = "";
					
					if (!karte.equals("")) {
						karteDodavanje = karte.substring(0, karte.length()-1);
					}
					
					//*????????????ovde treba ucitati i karte korisnika...
					//* za ime tipa korsinika moze da stoji i null
					String tipKupca = "";
					if((kupac.getTip().getImeTipa()) == ImeTipa.ZLATNI) {
						tipKupca = "ZLATNI";
					}
					else if(kupac.getTip().getImeTipa() == ImeTipa.SREBRNI){
						tipKupca="SREBRNI";
					}
					else if( kupac.getTip().getImeTipa() == ImeTipa.BRONZANI) {
						tipKupca = "BRONZANI";
					}
					
					
					System.out.println("========Upisujemo kupacaa: " + k.toString());
					pw.println((k.getKorisnickoIme() + ";" + k.getLozinka() + ";" + k.getIme() + ";" + k.getPrezime() + ";" +
							 k.getPol() + ";" + k.getDatumRodjenja() + ";" + k.getUloga() + ";" + karteDodavanje + ";" + kupac.getSakupljeniBodovi() + ";" +tipKupca+ ";" ));
				
					pw.flush();

				}
				else if(k.getUloga() == Uloga.PRODAVAC) {
					//????????????? resiti pitanje kako cemo cuvati MANIFESTACIJE prodavca...
					
					pw.println((k.getKorisnickoIme() + ";" + k.getLozinka() + ";" + k.getIme() + ";" + k.getPrezime() + ";" +
							 k.getPol() + ";" + k.getDatumRodjenja() + ";" + k.getUloga() + ";" +";" ));
				
					pw.flush();
					
				}
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

	public ArrayList<Korisnik> getKorisnici() {
		return this.korisnici;
	}

	public void setKorisnici(ArrayList<Korisnik> korisnici) {
		this.korisnici = korisnici;
	}	
	
	
	
}
