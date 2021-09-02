package model;

public class Test {
	private String sadrzaj;
	
	public Test() {
		
	}

	public String getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	@Override
	public String toString() {
		return "Test [sadrzaj=" + sadrzaj + "]";
	}
	
}
