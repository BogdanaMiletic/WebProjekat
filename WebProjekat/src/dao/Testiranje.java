package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Testiranje {
	
	private ArrayList<String> sadrzajLista = new ArrayList<String>();
	
	public Testiranje(String putanja) {
		BufferedReader in = null;
		try {
			File file = new File(putanja +"/utils"+"/test.txt") ;
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			procitajSadrzaj(in);
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
	
	private void procitajSadrzaj(BufferedReader in) throws IOException {
		String linija = "";
		
		while((linija = in.readLine())!= null) {
			
			if(linija != "" && linija != "\n") {
				sadrzajLista.add(linija);
			}
		}
	}
	
	public void upisiSadrzaj(String putanja) throws IOException {
		
		File fajl = new File(putanja +"/utils/test.txt");
		System.out.println(putanja);
		if(!fajl.exists()) {
			System.out.println("Pravim off sjaoooooooooooo");
			fajl.createNewFile();
		}
		
		PrintWriter pw = new PrintWriter(new FileWriter(fajl, false));
		sadrzajLista.add("bogy");
		
		for(String e : sadrzajLista) {
			pw.print(e + "\n");
			System.out.println("Upisujemo: " + e);
		}
		pw.close();
		
		
	}

	public ArrayList<String> getSadrzajLista() {
		return sadrzajLista;
	}

	public void setSadrzajLista(ArrayList<String> sadrzajLista) {
		this.sadrzajLista = sadrzajLista;
	}
	
	

}
