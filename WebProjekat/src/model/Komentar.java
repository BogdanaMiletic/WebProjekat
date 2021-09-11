package model;

public class Komentar {
	
	public enum Status{
		KREIRAN, ODOBREN, ODBIJEN;
	}
	private Kupac kupacKomentator;
	private Manifestacija manifestacija;
	private String textKomentara;
	private int ocena;
	private Status status;
	
	public Komentar() {
		
	}
	public Komentar(Kupac kupacKomentator, Manifestacija manifestacija, String textKomentara, int ocena, Status status) {
		super();
		this.kupacKomentator = kupacKomentator;
		this.manifestacija = manifestacija;
		this.textKomentara = textKomentara;
		this.ocena = ocena;
		this.status = status;
	}
	


	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kupacKomentator == null) ? 0 : kupacKomentator.hashCode());
		result = prime * result + ((manifestacija == null) ? 0 : manifestacija.hashCode());
		result = prime * result + ocena;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((textKomentara == null) ? 0 : textKomentara.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Komentar other = (Komentar) obj;
		if (kupacKomentator == null) {
			if (other.kupacKomentator != null)
				return false;
		} else if (!kupacKomentator.equals(other.kupacKomentator))
			return false;
		if (manifestacija == null) {
			if (other.manifestacija != null)
				return false;
		} else if (!manifestacija.equals(other.manifestacija))
			return false;
		if (ocena != other.ocena)
			return false;
		if (status != other.status)
			return false;
		if (textKomentara == null) {
			if (other.textKomentara != null)
				return false;
		} else if (!textKomentara.equals(other.textKomentara))
			return false;
		return true;
	}
	public Kupac getKupacKomentator() {
		return kupacKomentator;
	}
	public void setKupacKomentator(Kupac kupacKomentator) {
		this.kupacKomentator = kupacKomentator;
	}
	public Manifestacija getManifestacija() {
		return manifestacija;
	}
	public void setManifestacija(Manifestacija manifestacija) {
		this.manifestacija = manifestacija;
	}
	public String getTextKomentara() {
		return textKomentara;
	}
	public void setTextKomentara(String textKomentara) {
		this.textKomentara = textKomentara;
	}
	public int getOcena() {
		return ocena;
	}
	public void setOcena(int ocena) {
		this.ocena = ocena;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Komentar [kupacKomentator=" + kupacKomentator + ", manifestacija=" + manifestacija + ", textKomentara="
				+ textKomentara + ", ocena=" + ocena + ", status=" + status + "]";
	}
	
	
	

}
