package model;

public class TipKupca {
	
	public enum ImeTipa{
		ZLATNI, SREBRNI, BRONZANI;
	}
	
	private ImeTipa imeTipa;
	private int popust;
	private int trazeniBrojBodova;
	
	public TipKupca() {
	}
	
	public TipKupca(ImeTipa i, int popust, int bodovi) {
		this.imeTipa = i;
		this.popust = popust;
		this.trazeniBrojBodova = bodovi;
	}

	public ImeTipa getImeTipa() {
		return imeTipa;
	}

	public void setImeTipa(ImeTipa imeTipa) {
		this.imeTipa = imeTipa;
	}

	public int getPopust() {
		return popust;
	}

	public void setPopust(int popust) {
		this.popust = popust;
	}

	public int getTrazeniBrojBodova() {
		return trazeniBrojBodova;
	}

	public void setTrazeniBrojBodova(int trazeniBrojBodova) {
		this.trazeniBrojBodova = trazeniBrojBodova;
	}

	@Override
	public String toString() {
		return "TipKupca [imeTipa=" + imeTipa + ", popust=" + popust + ", trazeniBrojBodova=" + trazeniBrojBodova + "]";
	}
}
