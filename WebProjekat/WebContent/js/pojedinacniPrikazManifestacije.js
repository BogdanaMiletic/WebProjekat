$(document).ready(function(){
	//preuzimamo parametre iz linka za manifestaciju
	let parametriPretrage = new URLSearchParams(window.location.search)
	//parametriPretrage.has('naziv') // true
	
	let naziv = parametriPretrage.get("naziv")
	let datum = parametriPretrage.get("datum");
	let adresa = parametriPretrage.get("lokacija");
	console.log(naziv);
	console.log(datum);
	console.log(adresa);
	
	//upucuejemo get zahtev da pronadjemo ovu manifestaciju
	$.get("../WebProjekat/rest/manifestacije/" + naziv + "/" + datum + "/" + adresa,
		function(data, status){
			console.log(JSON.stringify(data));
			
			//dinamicki podesavamo vrednosti za prikaz konkretrne manifestacije
			$("#naziv").text(data.naziv);
			$("#tip").text(data.tip);
			// za poster mozemo automatski da postavimo atribut sa putanjom..$("#poster").
			$("#brojMesta").text("Broj mesta: " + data.brojMesta);
			$("#preostaliBrojKarata").text("Preostali broj karata: ");
			//$("#datum").text("Datum i vreme odrzavanja: " +  data.datumIvremeOdrzavanja.dayOfMonth+"."+data.datumIvremeOdrzavanja.monthValue+"."+data.datumIvremeOdrzavanja.year +".  " +data.datumIvremeOdrzavanja.hour+":"+data.datumIvremeOdrzavanja.minute +"h");
			$("#cena").text("Cena regular karte: " + data.cenaRegularKarte);
			$("#status").text("Status manifestaijce: " + data.status);
			
			let adresaSplit = (data.lokacija.adresa).split(",");
			$("#adresa").text(adresaSplit[0] );
			$("#grad").text(adresaSplit[1] +" "+ adresaSplit[2]);
			
			//ukoliko je ulogovani korisnik sa ulogom prodavac>> omoguci IZMENU PODATAKA O MANIFESTACIJAMA
			/*$.get(
				"../WebProjekat/rest/korisnici/trenutnoUlogovan",
				function( data,status){
					if(data.uloga == Korisnik.Uloga.PRODAVAC){
						console.log("Ulogovan je prodavac...");
						$("#prikazManifestacije").append("<a href=\"izmenaPodatakaOmanifestaciji.html?naziv="+data.naziv + "&datum="+data.datumIvremeOdrzavanja.year+"-"+data.datumIvremeOdrzavanja.monthValue+"-"+data.datumIvremeOdrzavanja.dayOfMonth +"  " +data.datumIvremeOdrzavanja.hour+":"+data.datumIvremeOdrzavanja.minute +"lokacija="+data.lokacija.adresa+"\">Izmeni podatke o manifestaciji</a>")
					}
				}
			);*/
		}
	);
	
});