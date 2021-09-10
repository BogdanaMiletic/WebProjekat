$(document).ready(function(){
	//parametri iz zahteva..>>
	let parametriPretrage = new URLSearchParams(window.location.search)
	//parametriPretrage.has('naziv') // true
	
	let naziv = parametriPretrage.get("nazivManifestacije")
	let datum = parametriPretrage.get("datum");
	let adresa = parametriPretrage.get("lokacija");
	let tipKarte = parametriPretrage.get("tipKarte");
	let sirina = parametriPretrage.get("sirina");
	let duzina = parametriPretrage.get("duzina");

	let brojRezervisanih = parametriPretrage.get("brojRezervisanih");
	console.log(naziv);
	console.log(datum);
	console.log(adresa);
	console.log(sirina + " , " + duzina);
	console.log(tipKarte + ", " + brojRezervisanih);
	
	//trazimo tu manifestaciju u podacima >>
	$.get(
		"../WebProjekat/rest/manifestacije/"+ naziv + "/" + datum + "/" + adresa + "/" + sirina + "/"+ duzina, 
		function(data, status){
			console.log("Manifestacija je: " + JSON.stringify(data));
			
			//trazimo sad trenutno ulogovanog korisnika
			$.get(
				"../WebProjekat/rest/korisnici/trenutnoUlogovan",
				function(trentunoUlogovan, status){
					console.log("Trentuno ulogovan je: " + JSON.stringify(trentunoUlogovan));
					
					potvrdiRezervaciju(brojRezervisanih, data, trentunoUlogovan, tipKarte);
					
				}
			);
		}
	);
	
});

function potvrdiRezervaciju(brojKarti, manifestacija, narucilac, tipKarte){
	console.log("Upali smo u potvrdu rezervacije...");

	$("#naziv").text(manifestacija.naziv);
	$("#tip").text(manifestacija.tip);
	$("#datum").text("Datum odrzavanja: " + manifestacija.datumIvremeOdrzavanja);

	//*** cena ukupna
	let cenaIzracunata = racunanjeCeneKarte(manifestacija, tipKarte, brojKarti);
	console.log("Cena karte je: " + cenaIzracunata);
	
	
	$("#cenaUkupna").text("Ukupna cena: "+ cenaIzracunata);

	$("#broj").text("Broj karti: " + brojKarti);

	$("#prikazManifestacije").append("<button id=\"rezervacijaKartePotvrda\"> rezervisi kartu</button>");

	$("#rezervacijaKartePotvrda").click(function(){
	
		rezervisiKartu(brojKarti, manifestacija, narucilac, tipKarte);

	});
}
function rezervisiKartu(brojKarti, manifestacija, narucilac, tipKarte){
console.log("Upali smo u rezervaciju...");

console.log("Manifestacija za koju rezervisemo: " + JSON.stringify(manifestacija) );
// pronalazimo id ove karte>> sve karte ucitane + 1 je id

$.get("../WebProjekat/rest/karte/sveKarte",
		function(data, status){
			let idBroj = 0;
			for( let e of data){
				idBroj= idBroj + 1;
			}
			console.log("Broj idija ********: "+ idBroj);
	
			let cenaIzracunata = racunanjeCeneKarte(manifestacija, tipKarte, brojKarti);
			console.log("Cena karte je: " + cenaIzracunata);
	
			let datumFormat = (manifestacija.datumIvremeOdrzavanja).substring(0, (manifestacija.datumIvremeOdrzavanja).length-3);
			console.log("???????????? Datum: " + datumFormat);
			
			//cenu jedne karte dobijamo kad ukupnu izracunatu cenu podelimo sa brojem karti..
			let cenaJedneKarte = cenaIzracunata/brojKarti;
			console.log("\t\t??????????????????????????????  Cena jedne karte: " + cenaJedneKarte);
			
		
			
			let karta = {
						"id": idBroj,
						"manifestacijaZaKojuJeRezervisana": manifestacija.naziv,
						"datumIvremeManifestaije": datumFormat,
						"cena": cenaJedneKarte,
						"kupacImeIprezime": narucilac.ime + "," + narucilac.prezime,
						"status": "REZERVISANA",
						"tipKarte": tipKarte
			};
			console.log("????????????????????//// Podaci koje saljemo: " + JSON.stringify(karta));
			
			// saljemo post zahtev >> kreiramo novu kartu koja ima status rezervisana
			$.post(
					"../WebProjekat/rest/karte/rezervacijaKarte/"+brojKarti,
						JSON.stringify(karta),
						function (data, status){
							console.log("Status je: " + status);
							
							
							alert("Karta je rezervisana..");
							history.go(-2);
						}
					
			);
		
	});
}

function pronadjiIdZaNovuKartu(){
$.get("../WebProjekat/rest/karte/sveKarte",
		function(data, status){
	let idBroj = 1;
	for( let e of data){
		console.log(e);
		idBroj= idBroj + 1;
	}
	console.log("Broj idija ********: "+ idBroj);
	return idBroj;
});

}

function racunanjeCeneKarte(manifestacija, tipKarte, brojKarti){
	let cenaRegular = manifestacija.cenaRegularKarte;

	if (tipKarte == "REGULAR"){
	return cenaRegular * brojKarti;
	}
	else if(tipKarte == "FAN_PIT"){
		let cenaFan = cenaRegular*2*brojKarti;
		return cenaFan;
	}
	else if(tipKarte == "VIP"){
		let vipCena = cenaRegular * 4*brojKarti;
		return vipCena;
	}
}