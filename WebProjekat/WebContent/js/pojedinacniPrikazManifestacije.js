$(document).ready(function(){
	//preuzimamo parametre iz linka za manifestaciju
	let parametriPretrage = new URLSearchParams(window.location.search)
	//parametriPretrage.has('naziv') // true
	
	let naziv = parametriPretrage.get("naziv")
	let datum = parametriPretrage.get("datum");
	let adresa = parametriPretrage.get("lokacija");
	let sirinaPreuzeta = parametriPretrage.get("sirina");
	let duzinaPreuzeta = parametriPretrage.get("duzina");
	console.log(naziv);
	console.log(datum);
	console.log(adresa);
	
	//upucuejemo get zahtev da pronadjemo ovu manifestaciju
	$.get("../WebProjekat/rest/manifestacije/" + naziv + "/" + datum + "/" + adresa+"/" + sirinaPreuzeta + "/" + duzinaPreuzeta,
		function(data, status){
			console.log(JSON.stringify(data));
			
			//dinamicki podesavamo vrednosti za prikaz konkretrne manifestacije
			$("#naziv").text(data.naziv);
			$("#tip").text(data.tip);
			// za poster mozemo automatski da postavimo atribut sa putanjom..$("#poster").
			$("#brojMesta").text("Broj mesta: " + data.brojMesta);
			$("#preostaliBrojKarata").text("Preostali broj karata: ");
			//$("#datum").text("Datum i vreme odrzavanja: " +  data.datumIvremeOdrzavanja.dayOfMonth+"."+data.datumIvremeOdrzavanja.monthValue+"."+data.datumIvremeOdrzavanja.year +".  " +data.datumIvremeOdrzavanja.hour+":"+data.datumIvremeOdrzavanja.minute +"h");
			$("#datum").text("Datum i vreme odrzavanja: " + data.datumIvremeOdrzavanja)
			$("#cena").text("Cena regular karte: " + data.cenaRegularKarte);
			$("#status").text("Status manifestaijce: " + data.status);
			
			let adresaSplit = (data.lokacija.adresa).split(",");
			$("#adresa").text(adresaSplit[0] );
			$("#grad").text(adresaSplit[1] +" "+ adresaSplit[2]);
			
			//ukoliko je ulogovani korisnik sa ulogom prodavac>> omoguci IZMENU PODATAKA O MANIFESTACIJAMA
			$.get(
				"../WebProjekat/rest/korisnici/trenutnoUlogovan",
				function( podatak,status){
					
					//undefined je ako niko nije ulogovan..
					if(podatak != undefined){
						console.log("Ulogovan je: " + podatak);
						if(podatak.uloga == "PRODAVAC"){
							console.log("Ulogovan je prodavac...");
							console.log("POdaci: " + data.lokacija.adresa + " ," + data.lokacija.geografskaSirina + " ," + data.lokacija.geografskaDuzina);
							$("#prikazManifestacije").append("<a href=\"izmenaPodatakaOmanifestaciji.html?naziv="+data.naziv + "&datum="+ data.datumIvremeOdrzavanja +"&lokacija="+data.lokacija.adresa+ "&sirina="+data.lokacija.geografskaSirina +"&duzina="+data.lokacija.geografskaDuzina+ "\">Izmeni podatke o manifestaciji</a>")
						}
						else if(podatak.uloga == "KUPAC"){
							//***** REZERVACIJA KARTI *********
							//omoguci rezervaciju karte
							console.log("Ulogovan je kupac");
							
							// ********* treba proveriti da li je manifestacija aktivna 
							//**** treba proveriti da li su rasprodate karte za tu manifestaciju
							//???????????????????????????
							
							$("#prikazManifestacije").append("<br/>");
							$("#prikazManifestacije").append("<div class=\"rezervisanjeKarte\"> </div>");
							
							$(".rezervisanjeKarte").append("<label>Broj karti: </label>");
							
							$(".rezervisanjeKarte").append("<input type=\"text\" name=\"brojRezervisanih\">")
							$(".rezervisanjeKarte").append("<br/>");
							$(".rezervisanjeKarte").append("<label>Tip karte: </label>");
							$(".rezervisanjeKarte").append("<select id=\"tipKarteRezervacija\"> " +
										"</select>");
							
							$("#tipKarteRezervacija").append("<option value=\"REGULAR\">REGULAR</option");
							$("#tipKarteRezervacija").append("<option value=\"FAN_PIT\">FAN_PIT</option");
							$("#tipKarteRezervacija").append("<option value=\"VIP\">VIP</option");
							
							$(".rezervisanjeKarte").append("<button id=\"rezervacijaKarte\"> rezervisi kartu</button>"); 
							
							
							$("input[name=brojRezervisanih]").val("1");
							
							//ukoliko se unese nesto drugo preuzimamo to i saljemo u okviru linka za rezervaciju
							let brojRezervisanihKarti = "1";
							$("input[name=brojRezervisanih]").change(function(){
								brojRezervisanihKarti = $("input[name=brojRezervisanih]").val();
								console.log("Broj rezervisanih karti je promenjen: " + brojRezervisanihKarti);
							})
						
							
							//obradjivac dogadjaja za klik na rezervaciju karte
							$("#rezervacijaKarte").click(function(){
								//preuzimamo tip karte
								let tipKarte = $("#tipKarteRezervacija option:selected").val();
								console.log("tip karte je: " + tipKarte);
								
								rezervisiKartu(brojRezervisanihKarti, data, podatak, tipKarte);
								
							});
							
							
						}
					}
					
				}
			);
		}
	);
	
});

function formatirajDatum(datum){
	
	return novi;
}

function rezervisiKartu(brojKarti, manifestacija, narucilac, tipKarte){
	console.log("Upali smo u rezervaciju...");
	
	console.log("Manifestacija za koju rezervisemo: " + JSON.stringify(manifestacija) );
	// pronalazimo id ove karte>> sve karte ucitane + 1 je id
	
	$.get("../WebProjekat/rest/karte/sveKarte",
			function(data, status){
		let idBroj = 1;
		for( let e of data){
			idBroj= idBroj + 1;
		}
		console.log("Broj idija ********: "+ idBroj);
		
		let cenaIzracunata = racunanjeCeneKarte(manifestacija, tipKarte);
		console.log("Cena karte je: " + cenaIzracunata);
		
		let datumFormat = (manifestacija.datumIvremeOdrzavanja).substring(0, (manifestacija.datumIvremeOdrzavanja).length-3);
		console.log("???????????? Datum: " + datumFormat);
		let karta = {
				"id": idBroj,
				"manifestacijaZaKojuJeRezervisana": manifestacija.naziv,
				"datumIvremeManifestaije": datumFormat,
				"cena": cenaIzracunata,
				"kupacImeIprezime": narucilac.ime + "," + narucilac.prezime,
				"status": "REZERVISANA",
				"tipKarte": tipKarte
			};
			console.log("Podaci koje saljemo: " + karta);
			
			// saljemo post zahtev >> kreiramo novu kartu koja ima status rezervisana
			$.post(
					"../WebProjekat/rest/karte/rezervacijaKarte",
					JSON.stringify(karta),
					function (data, status){
						console.log("Status je: " + status);
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

function racunanjeCeneKarte(manifestacija, tipKarte){
	let cenaRegular = manifestacija.cenaRegularKarte;
	
	if (tipKarte == "REGULAR"){
		return cenaRegular;
	}
	else if(tipKarte == "FAN_PIT"){
		let cenaFan = cenaRegular*2;
		return cenaFan;
	}
	else if(tipKarte == "VIP"){
		let vipCena = cenaRegular * 4;
		return vipCena;
	}
}