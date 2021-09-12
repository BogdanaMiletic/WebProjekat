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
			var manifestacijaProsledi  = data;
			
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
					if(podatak == null){
						//ako niko nije ulogovan komentarisanje je zabranjeno..
						$("#dodajKomentar").hide();
						
					}
					
					//undefined je ako niko nije ulogovan..
					if(podatak != undefined){
						
						//********** PRIKAZ KOMENTARA ***************
						prikazKomentara(podatak, manifestacijaProsledi);
						
						console.log("Ulogovan je: " + podatak);
						if(podatak.uloga == "PRODAVAC"){
							console.log("Ulogovan je prodavac...");
							console.log("POdaci: " + data.lokacija.adresa + " ," + data.lokacija.geografskaSirina + " ," + data.lokacija.geografskaDuzina);
							$("#prikazManifestacije").append("<a id=\"dugmeIzmena\" href=\"izmenaPodatakaOmanifestaciji.html?naziv="+data.naziv + "&datum="+ data.datumIvremeOdrzavanja +"&lokacija="+data.lokacija.adresa+ "&sirina="+data.lokacija.geografskaSirina +"&duzina="+data.lokacija.geografskaDuzina+ "\">Izmeni podatke o manifestaciji</a>")
						
							//oni ne daju komentare>>>>
							$("#dodajKomentar").hide();
							
							//omogucimo odbijanje i prihvatanje komentara
							
							
						}
						else if(podatak.uloga == "KUPAC"){
							//***** REZERVACIJA KARTI *********
							//omoguci rezervaciju karte
							console.log("Ulogovan je kupac");
							console.log("\t\t^^^^^^^^^^^^^^^^^^^^^^^^^ Manifestacija je: " + JSON.stringify(data));
							
							//omogucimo dodavanje komentara...
							dodajKomentar(podatak, manifestacijaProsledi);
							
							// ********* treba proveriti da li je manifestacija aktivna 
							//**** treba proveriti da li su rasprodate karte za tu manifestaciju
							//???????????????????????????
							let aktivnaRezervacijaKarata = daLiJeAktivnaManifestacija(data);
							
							if(aktivnaRezervacijaKarata){
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
									
									//rezervisiKartu(brojRezervisanihKarti, data, podatak, tipKarte);
									window.location.href = "potvrdaRezervacijeKarte.html?nazivManifestacije="+data.naziv+"&datum="+ 
										data.datumIvremeOdrzavanja+"&lokacija=" + data.lokacija.adresa + "&sirina=" + data.lokacija.geografskaSirina + 
										"&duzina=" + data.lokacija.geografskaDuzina+ "&tipKarte="+tipKarte+"&brojRezervisanih="+brojRezervisanihKarti;

									//potvrdiRezervaciju(brojRezervisanihKarti, data, podatak, tipKarte);
								});
							}
							
						}
						else{
							//oni ne daju komentare>>>>
							$("#dodajKomentar").hide();
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

function daLiJeAktivnaManifestacija(manifestacija){
	
	let aktivna = true;
	//proveravamo da li je manifestacija aktivna
	if(manifestacija.status == "NEAKTIVNO"){
		aktivna = false;
	}
	
	//PROVERA DA LI SU KARTE ZA TU MANIFESTACIJU RASPRODATE..
	let brojDostupnihKarata = manifestacija.brojMesta;
	
	//trazimo broj rezervisanih karata za konkretno tu manifestaciju>> 
	
	//preuzimamo sve karte
	$.get(
		"../WebProjekat/rest/karte/sveKarte",
		function(data, status){
			let brojRezervisanihKarti = 0;
			for(let karta of data){
				if(karta.datumIvremeManifestaije == manifestacija.datumIvremeOdrzavanja
						&& karta.manifestacijaZaKojuJeRezervisana == manifestacija.naziv){
					
					if(karta.status == "REZERVISANA"){
						brojRezervisanihKarti += 1;
					}
				}
			}
			
			if (brojRezervisanihKarti >= brojDostupnihKarata){
				aktivna = false;
			}
		}
	);
	return aktivna;
}


/************** DODAJ KOMENTAR ***********/
function dodajKomentar(korisnik, manifestacija){
	
	console.log("********* PROVERA KOMENTARISANJE MOZ LI **************");
	mozeLiDaKomentarise(korisnik, manifestacija);
	
	//funkcionalnost dodavanja novog komentara
	dodavanjeKomentaraFunkcionalnost(korisnik, manifestacija);
	
	
	
}

function mozeLiDaKomentarise(korisnik, manifestacija){
	// proverimo ima li ovaj korisnik rezervisanu kartu za ovu manifestaciju
	
	//??vracamo sve karte
	
	
	console.log("$$$$$$$ Manifestacija je: " + JSON.stringify(manifestacija));
	
	
	$.get(
			"../WebProjekat/rest/manifestacije/daLiJeMaifestacijaOdrzana?naziv="+ manifestacija.naziv+ "&datum=" + manifestacija.datumIvremeOdrzavanja,
			function(data, status){
				
				let daLiJeOdrzana = data;
				console.log("!!!!!!!!!!!!    Manifestacija je odrzana: " + data);
				
				//proveravamo da li ima kupc rezervisanu kartu za tu manifestaiciju>> da li je posetio..
				var imeIprezimeUlogovanog = korisnik.ime + "," + korisnik.prezime;
				
				$.get(
						"../WebProjekat/rest/karte/daLiJePosetioManifestaciju?naziv="+ manifestacija.naziv + "&datum=" + manifestacija.datumIvremeOdrzavanja + "&imeIprezime=" + imeIprezimeUlogovanog,
						function(imaRezervisanu, status){
							
							console.log("!!!!!!!!!!!! Kupac ima rezervisanu kartu: " + imaRezervisanu);
							
							if(imaRezervisanu == true && daLiJeOdrzana == true){
								console.log("Uspesno prikazuj>>>>>>>>>>>>> " + manifestacija.naziv + " " + true);
								
								
								console.log("***********Korinsik MOZE da komentarise.....");
							}
							else{
								console.log("NEUSPESNO PRIKAZUJ prikazuj>>>>>>>>>>>>> " + manifestacija.naziv + " " + false);
								console.log("Vrednosti: " + imaRezervisanu + " , " + daLiJeOdrzana);

								$("#dodajKomentar").hide();
							}
							
						}
				);
				
			}
	);
}	

function dodavanjeKomentaraFunkcionalnost(korisnik, manifestacija){
	
	//automatski prikazujemo izabranu vrednost ocene
	$("input[name=ocena]").change(function(){
		$("#vrednostOcene").text($("input[name=ocena]").val());
	})
	
	//ako je kliknuto na dugme dodaj komentar
	$("#dodajKomentarDugme").click(function(){
		
		console.log("Kliknuto je na dodavanje...");
		
		//preuzimamo text komentara
		let textKomentar = $("#noviKomentar").val();
		console.log("TExt komentara je: " + textKomentar);
		
		let vrednostOcene = $("input[name=ocena]").val();
		console.log("Vrednost ocenjena je: "+ vrednostOcene);		
		
		//sad ovaj komentar dodajemo 
		let noviKomentar = {
				"kupacKomentator": korisnik,
				"manifestacija": manifestacija,
				"textKomentara": textKomentar,
				"ocena": vrednostOcene,
				"status": "KREIRAN"
		}
		$.post(
				"../WebProjekat/rest/komentari/noviKomentar",
				JSON.stringify(noviKomentar),
				function(data, status){
					console.log("*****   Dodat je komentar: " + JSON.stringify(data)); 
					alert("Komentar je dodat...");
				}
		);
		
	})	
}

function prikazKomentara(korisnik, manifestacija){
	
	$.get(
			"../WebProjekat/rest/komentari/sviKomentari",
			function(data, status){
				
				//ako su neki prikazani sklonimo ih...
				$(".kom").hide();
				
				
				
				if(korisnik.uloga == "KUPAC"){
					//PRIKAZUJEMO SAMO ODOBRENE KOMENTARE
					
					for(let komentar of data){
						if(komentar.status == "ODOBREN"){
							//prikazujemo samo komentare koji odgovaraju konkretnoj manifestaciji
							if(komentar.manifestacija.naziv == manifestacija.naziv &&
									komentar.manifestacija.datumIvremeOdrzavanja == manifestacija.datumIvremeOdrzavanja){
							
								$("#pregledKomentara").append("<div class=\"kom\"> </div>");
								$(".kom").append("<h5>" + komentar.kupacKomentator.ime +" "+ komentar.kupacKomentator.prezime + "</h5>");
								$(".kom").append("<label>" + komentar.textKomentara + "</label>");
								$(".kom").append("<br/>")
								$(".kom").append("<label> Ocena: " + komentar.ocena + "</label>" );
								
								$(".kom").append("<label> Status: " + komentar.status + "</label>");
								$(".kom").append("<hr>");
							}
								
						}
					}
				}
				else if(korisnik.uloga == "ADMINISTRATOR" || korisnik.uloga == "PRODAVAC"){
					//ADMINISTARATORI I PRODAVCI VIDE SVE KOMENTARE (odbijene i odobrene)
					for(let komentar of data){
						//komentar prikazujemo ako odgovara toj manifestaciji
						if(komentar.manifestacija.naziv == manifestacija.naziv &&
								komentar.manifestacija.datumIvremeOdrzavanja == manifestacija.datumIvremeOdrzavanja){
							
							$("#pregledKomentara").append("<div class=\"kom\"> </div>");
							$(".kom").append("<h5>" + komentar.kupacKomentator.ime +" "+ komentar.kupacKomentator.prezime + "</h5>");
							$(".kom").append("<label>" + komentar.textKomentara + "</label>");
							$(".kom").append("<br/>");
							$(".kom").append("<label> Ocena: " + komentar.ocena + "</label>" );
							$(".kom").append("<label> Status: " + komentar.status + "</label>");
							
							if(korisnik.uloga == "PRODAVAC"){
								
								//*********** ODOBRAVANJE KOMENTARA ***************
								//AKO JE PRODAVAC IMA MOGUCNOST ODOBRAVANJA KOMENTARA i odbijanja
								$(".kom").append("<button class=\"odobri\">odobri komentar</button>")
								$(".kom").append("<button class=\"odbij\"> odbij komentar </button>");
								
								//funkcija za odobravanje komentara
								odobravanjeKomentara(komentar, manifestacija, korisnik);
								
							}
							$(".kom").append("<hr>")
							
						}
					}
				}
			}
	);
}

//******* ODOBRAVANJE KOMENTARA - funkcionalnost kupca ************/
function odobravanjeKomentara(komentar, manifestacija, korisnik){
	
		//proverimo da li je selektovano dugme za ODOBRAVANJE komentara
	$(".odobri").click(function(){
		$.ajax({
			url: "../WebProjekat/rest/komentari/odobravanjeKomentara/" + komentar.kupacKomentator.korisnickoIme + "/"+ komentar.manifestacija.naziv + "/"+ komentar.manifestacija.datumIvremeOdrzavanja+"/" + komentar.textKomentara + "/" + "ODOBREN", 
			type: 'PUT',
			success: function(data, status) {
			       console.log("Komentar je odobren....");
			       console.log(JSON.stringify(data));
			       alert("Komentar je odobren..");
			}
		});
	})
	
	
		//proverimo da li je selektovano dugme za ODBIJANJE komentara

	$(".odbij").click(function(){
		$.ajax({
			url: "../WebProjekat/rest/komentari/odobravanjeKomentara/" + komentar.kupacKomentator.korisnickoIme + "/"+ komentar.manifestacija.naziv + "/"+ komentar.manifestacija.datumIvremeOdrzavanja+"/" + komentar.textKomentara + "/" + "ODBIJEN", 
			type: 'PUT',
			success: function(result) {
			        console.log("Komentar je odbijen");
			        alert("Komentar je odbijen..");
			}
		});
	})		
}

/*function potvrdiRezervaciju(brojKarti, manifestacija, narucilac, tipKarte){
	console.log("Upali smo u potvrdu rezervacije...");
	
	$("#naziv").text(manifestacija.naziv);
	$("#tip").text(manifestacija.tip);
	$("#datum").text(manifestacija.datumIvremeOdrzavanja);
	
	//*** cena ukupna
	let cenaIzracunata = racunanjeCeneKarte(manifestacija, tipKarte);
	console.log("Cena karte je: " + cenaIzracunata);
	$("cenaUkupna").text(cenaIzracunata);
	
	$("#broj").text(brojKarti);
	
	$("#potvrdaRezervacije").append("<button id=\"rezervacijaKartePotvrda\"> rezervisi kartu</button>");
	
	$("#rezervacijaKartePotvrda").click(function(){
		
		//rezervisiKartu(brojRezervisanihKarti, data, podatak, tipKarte);
		//potvrdiRezervaciju(brojRezervisanihKarti, data, podatak, tipKarte);
	});
	
	
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
*/