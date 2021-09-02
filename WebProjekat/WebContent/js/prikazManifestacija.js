$(document).ready(function(){
	//preuzmi ajax pozivom listu svih ucitanih manifestacija..
	$.get(
			"../WebProjekat/rest/manifestacije/",
			function(data, status){
				console.log("podaci su: " + JSON.stringify(data));
				
				let brojacKolona = 0;
				let brojacRedova = 0;
				
				//na pocetku automatski dodajemo prvi red koji ima index 0
				$("#prikazManifestacija").append("<div class=\"row\" id=\"row" + brojacRedova +"\"></div>");
				
				for(podatak of data){
					brojacKolona += 1;
					
					if(brojacKolona == 4){
						brojacRedova += 1;
						//ako imamo 4 elementa u jednom redu>> prelazimo u novi red i dodajemo ga..
						$("#prikazManifestacija").append("<div class=\"row\" id=\"row" + brojacRedova +"\"></div>")
						brojacKolona = 0;
						
					}
					console.log("POjedinacno: " + JSON.stringify(podatak));
					//prikazujemo elemente kao pojedinacne pasuse koje dodajemo
					
					//sad redu u kojem se trenutno nalazimo dodajemo novu kolonu sa karticom
					$("#row" + brojacRedova).append( 
							"<div class=\"column\"> " +
									" <div class=\"card\">" +
									"	 <p style=\"font-size:25px;\">"+ podatak.naziv +" </p> " +
										"<hr>" +
										"<p>" + podatak.tip + "</p>" +
										"<p> Lokacija: " + podatak.lokacija.adresa +"</p>"+
										"<img src=\"slike/pozoriste.jpg\" alt=\"slika pozorisnog plakata\" height=\"200px\" width=\"200px\">"+
										"<p>Datum odrzavanja: " + podatak.datumIvremeOdrzavanja.dayOfMonth+"."+podatak.datumIvremeOdrzavanja.monthValue+"."+podatak.datumIvremeOdrzavanja.year +".  " +podatak.datumIvremeOdrzavanja.hour+":"+podatak.datumIvremeOdrzavanja.minute +"h</p>"+
										"<p style=\"font-size:20px\"> Cena: " + podatak.cenaRegularKarte +" din.</p>"+
										"</div>" +
							" </div>");
				}
			}
	);
	
});