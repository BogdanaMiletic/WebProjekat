$(document).ready(function(){
	//preuzimamo parametre iz linka za manifestaciju
	let parametriPretrage = new URLSearchParams(window.location.search)
	//parametriPretrage.has('naziv') // true
	
	let naziv = parametriPretrage.get("naziv")
	let datum = parametriPretrage.get("datum");
	console.log(naziv);
	console.log(datum);
	
	//upucuejemo get zahtev da pronadjemo ovu manifestaciju
	$.get("../WebProjekat/rest/manifestacije/" + naziv + "/" + datum,
		function(data, status){
			console.log(JSON.stringify(data));
			
			//dinamicki podesavamo vrednosti za prikaz konkretrne manifestacije
			$("#naziv").text(data.naziv);
			$("#tip").text(data.tip);
			// za poster mozemo automatski da postavimo atribut sa putanjom..$("#poster").
			$("#brojMesta").text("Broj mesta: " + data.brojMesta);
			$("#preostaliBrojKarata").text("Preostali broj karata: ");
			$("#datum").text("Datum i vreme odrzavanja: " +  data.datumIvremeOdrzavanja.dayOfMonth+"."+data.datumIvremeOdrzavanja.monthValue+"."+data.datumIvremeOdrzavanja.year +".  " +data.datumIvremeOdrzavanja.hour+":"+data.datumIvremeOdrzavanja.minute +"h");
			$("#cena").text("Cena regular karte: " + data.cenaRegularKarte);
			$("#status").text("Status manifestaijce: " + data.status);
			
			let adresaSplit = (data.lokacija.adresa).split(",");
			$("#adresa").text(adresaSplit[0] );
			$("#grad").text(adresaSplit[1] +" "+ adresaSplit[2]);
			
		}
	);
	
});