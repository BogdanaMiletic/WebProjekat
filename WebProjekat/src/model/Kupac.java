package model;

import java.util.ArrayList;

public class Kupac extends Korisnik{
	private ArrayList<Karta> sveKarte;
	private int sakupljeniBodovi;
	private TipKupca tip;
	

	public Kupac(Korisnik k, ArrayList<Karta> sveKarte, int sakupljeniBodovi, TipKupca tipKupca) {
		super(k.getKorisnickoIme(), k.getLozinka(), k.getIme(), k.getPrezime(), k.getPol(), k.getDatumRodjenja(), k.getUloga());
		this.sveKarte = sveKarte;
		this.sakupljeniBodovi = sakupljeniBodovi;
		this.tip = tipKupca;
	}
	
	public Kupac() {
		super();
	}

	public ArrayList<Karta> getSveKarte() {
		return sveKarte;
	}

	public void setSveKarte(ArrayList<Karta> sveKarte) {
		this.sveKarte = sveKarte;
	}

	public int getSakupljeniBodovi() {
		return sakupljeniBodovi;
	}

	public void setSakupljeniBodovi(int sakupljeniBodovi) {
		this.sakupljeniBodovi = sakupljeniBodovi;
	}
	
	public TipKupca getTip() {
		return tip;
	}

	public void setTip(TipKupca tip) {
		this.tip = tip;
	}

	@Override
	public String toString() {
		return "Kupac [ " + super.toString() + " ,sveKarte=" + sveKarte + ", sakupljeniBodovi=" + sakupljeniBodovi + "]";
	}
	
}
