package model;

import java.util.ArrayList;

public class Prodavac extends Korisnik{
	private ArrayList<Manifestacija> manifestacije;
	
	public Prodavac(Korisnik k, ArrayList<Manifestacija> manifestacije) {
		super(k.getKorisnickoIme(), k.getLozinka(), k.getIme(), k.getPrezime(), k.getPol(), k.getDatumRodjenja(), k.getUloga());
		this.manifestacije = manifestacije;
	}
	
	public Prodavac() {
		super();
	}

	public ArrayList<Manifestacija> getManifestacije() {
		return manifestacije;
	}

	public void setManifestacije(ArrayList<Manifestacija> manifestacije) {
		this.manifestacije = manifestacije;
	}

	@Override
	public String toString() {
		return "Prodavac [manifestacije=" + manifestacije + "]";
	}
	
	
}
