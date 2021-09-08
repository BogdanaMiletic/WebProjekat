$(document).ready(function(){
	//podaci iz linka o manifestaciji
	//preuzimamo parametre iz linka za manifestaciju
	let parametriPretrage = new URLSearchParams(window.location.search)
	//parametriPretrage.has('naziv') // true
	
	let nazivStari = parametriPretrage.get("naziv")
	let datumStari = parametriPretrage.get("datum");
	let adresaStara = parametriPretrage.get("lokacija");
	let sirinaStara = parametriPretrage.get("sirina");
	let duzinaStara = parametriPretrage.get("duzina");
	console.log(nazivStari);
	console.log(datumStari);
	console.log(adresaStara);
	console.log(sirinaStara + " " + duzinaStara);

	
	// iz parametara u linku ucitavamo i pronalazimo konkretnu manifestaciju..
	
	pronadjiPodatkeOmanifestaciji(nazivStari, datumStari, adresaStara, sirinaStara, duzinaStara);
	
	
	// prepisemo sve stare vrednosti podataka o manifestacijama koje vec postoje..
	
	
	//reagujemo na dogadjaj promene statusa jedan se pali drugi se gasi
	$("input[name=aktivno]").change(function(){
		//ako se promeni ovaj se pali, a drugi se gasi
		$("input[name=aktivno]").checked = true;
		$("input[name=neaktivno]").checked = false;
	});
	
	$("input[name=neaktivno]").change(function(){
		//ako se promeni ovaj se pali, a drugi se gasi
		$("input[name=aktivno]").checked = false;
		$("input[name=neaktivno]").checked = true;
	});
		

		
	
	
	$("input[name=neaktivno]").change(function(){
		//ako se promeni ovaj se pali drugi se gasi
	});
	
	$("#formaDodavanjeManifestacije").submit(function(event){
		console.log("Upaliiiii");
		let naziv = $("input[name=naziv]").val();
		console.log(naziv);
		let tip=$("#tip option:selected").val();
		console.log(tip);
		let brojMesta= $("input[name=brojMesta]").val();
		console.log(brojMesta);
		let datum = $("input[name=datum]").val();
		console.log(datum);
		let cena= $("input[name=cena]").val();
		let aktivnoStatus = "";
		let neaktivnoStatus = "";
		
		if($("input[name=aktivno]:checked").val()){
			aktivnoStatus = true;
			console.log("Selektovano je aktivno ... ");
		}
		if($("input[name=neaktivno]:checked").val()){
			neaktivnoStatus = true;
			console.log("Selektovano je neaktivno...");
		}
		
		let sirinaG = $("input[name=geografskaSirina]").val();
		console.log(sirinaG);
		let duzinaG = $("input[name=geografskaDuzina]").val();
		
		let grad = $("input[name=grad]").val();
		let ulicaIbroj = $("input[name=ulicaIbroj]").val();
		let postanskiBroj = $("input[name=postanskiBroj]").val();
		
		let poster = "";
		
		//validiramo podatke iz forme
		let validacijaPodataka = validacija(naziv, tip, brojMesta, datum, cena, sirinaG, duzinaG, grad, ulicaIbroj, postanskiBroj, aktivnoStatus, neaktivnoStatus);
		console.log("VAlidacaija : " + validacijaPodataka);
		
		
		if( validacijaPodataka == true){
			//>>>uputi poziv za dodavanje nove manifestacije..
			
			let adresaOblik = ulicaIbroj+"," + grad+"," + postanskiBroj;
			let lokacija = {
				"geografskaSirina": sirinaG,
				"geografskaDuzina": duzinaG,
				"adresa": adresaOblik
			}
			let status = "NEAKTIVNO";
			if(aktivnoStatus != "")
				status = "AKTIVNO";
			
			let novaManifestacija={
				"naziv": naziv,
				"tip": tip,
				"brojMesta": brojMesta,
				"datumIvremeOdrzavanja": datum,
				"cenaRegularKarte": cena,
				"status": status,
				"lokacija": lokacija
			};
			
			console.log("Iiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
			event.preventDefault();
			//put zahtev za izmenu podataka izabrane manifestacije..
			$.ajax({
				   url: "../WebProjekat/rest/manifestacije/izmeniManifestaciju/" + sirinaStara + "/" + duzinaStara + "/" +datumStari, 
				   type: 'PUT',
				   data: JSON.stringify(novaManifestacija),
				   success: function(response) {
				     console.log("Manifestacija je izmenjena: ****" + JSON.stringify(response));
				     //sad postavimo alert da je izmenjena manifestacija.
				     alert("Podaci o manifestaciji su uspesno izmenjeni..");
				     //preusmerimo se na prethodnu stranu (tacnije dve prehtodne strane unazad iz istorije poseta stranica)
				     window.history.go(-2);
				     
				   }
				});
			
		}

		event.preventDefault();
	});
});


function validacija(naziv, tip, brojMesta, datum, cena, sirinaG, duzinaG, grad, ulicaIbroj, postanskiBroj, statusAktivno ,statusNeaktivno){
	//validiramo podatke koji su prosledjeni >> da li je neki podatak prazan..
	console.log("Tip je: " + tip);
	console.log("Naziv je: " + naziv);
	console.log("Broj mesta: " + brojMesta);
	console.log("Datum: " + datum);
	console.log("Sirina: " + sirinaG);
	console.log("Duzina: " + duzinaG);
	console.log("Grad: "  + grad);
	console.log("Ulica: " + ulicaIbroj);
	console.log("Posta: " + postanskiBroj);
	
	if(naziv == ""){
		$("#porukaNaziv").text("Morate uneti naziv manifestaicje");
		console.log("Poruka za naziv");
		return false;
	}
	
	if(tip == 'none'){
		$("#porukaIip").text("Morate uneti tip manifestacije..");
		console.log("poruka tip nije izabran");
		return false;
	}
	
	if(brojMesta == ""){
		$("#porukaBrojMesta").text("Morate uneti broj mesta.");
		console.log("Broj mesta morate uneti.//");
		return false;
	}
	
	if(isNaN(brojMesta) && brojMesta != ""){
		$("#porukaBrojMesta").text("Broj mesta mora imati brojcanu vrednost..");
		return false;

	}
	
	if(datum==""){
		$("#porukaDatum").text("Morate uneti datum..");
		return false;
	}
	
	if(cena==""){
		$("#porukaCena").text("Morate uneti cenu..");
		return false;
	}
	
	if(isNaN(cena) && cena != ""){
		$("#porukaCena").text("Cena mora imati brojcanu vrednost..");
		return false;
	}
	
	if(statusAktivno =="" && statusNeaktivno ==""){
		$("#porukaStatus").text("Morate uneti status...");
		return false;
	}
	
	if(sirinaG==""){
		$("#porukaSirina").text("Morate uneti geografsku sirinu...");
		return false;
	}
	
	/*if(isNaN(sirinaG) && sirinaG != ""){
		$("#porukaSirina").text("Geografska sirina mora imati brojcanu vrednost..");
		return false;
	}
	
	if(duzinaG==""){
		$("#porukaDuzina]").text("Morate uneti geografsku duzinu...");
		return false;
	}
	
	if(isNaN(duzinaG) && duzinaG != ""){
		$("#porukaDuzina").text("Geografska duzina mora imati brojcanu vrednost..");
		return false;
	}
	
	if(grad==""){
		$("#porukaGrad").text("Morate uneti grad...");
		return false;
	}
	
	if(ulicaIbroj==""){
		$("#porukaUlica").text("Morate uneti ulicu i broj");
		return false;
	}
	
	if(postanskiBroj==""){
		$("#porukaPosta").text("Morate uneti postanski broj...");
		return false;
	}

	if(isNaN(postanskiBroj) && postanskiBroj != ""){
		$("#porukaPosta").text("Broj mesta mora imati brojcanu vrednost..");
		return false;
	}*/
	
		
	return true;
}


function pronadjiPodatkeOmanifestaciji(naziv, datum, adresa, sirina, duzina){
		
	//upucuejemo get zahtev da pronadjemo ovu manifestaciju
	$.get("../WebProjekat/rest/manifestacije/" + naziv + "/" + datum + "/" + adresa + "/" + sirina + "/" + duzina ,
		function(data, status){
			console.log("Pronadjeno je: "+ JSON.stringify(data));
			
			//dinamicki podesavamo vrednosti za manifestaciju koje su vec unesene
			$("input[name=naziv]").attr("value", data.naziv);
			$("input[name=tip]").attr("value", data.tip);
			// za poster mozemo automatski da postavimo atribut sa putanjom..$("#poster").
			$("input[name=brojMesta]").attr("value", data.brojMesta);
			//$("input[name=preostaliBrojKarata]").attr("value", "Preostali broj karata: ");
			//$("#datum").text("Datum i vreme odrzavanja: " +  data.datumIvremeOdrzavanja.dayOfMonth+"."+data.datumIvremeOdrzavanja.monthValue+"."+data.datumIvremeOdrzavanja.year +".  " +data.datumIvremeOdrzavanja.hour+":"+data.datumIvremeOdrzavanja.minute +"h");
			$("input[name=datum]").val(data.datumIvremeOdrzavanja)
			$("input[name=cena]").val(data.cenaRegularKarte);
			
			if(data.status == "AKTIVNO"){
				$("input[name=aktivno]").attr("checked", true); 
				$("input[name=neaktivno]").attr("checked", false); 

			}
			if(data.status == "NEAKTIVNO"){
				$("input[name=neaktivno]").attr("checked", true); 
				$("input[name=aktivno]").attr("checked", false); 

			}

			$("input[name=geografskaSirina]").val(data.lokacija.geografskaSirina);
			$("input[name=geografskaDuzina]").val(data.lokacija.geografskaDuzina);

			
			let adresaSplit = (data.lokacija.adresa).split(",");
			$("input[name=ulicaIbroj]").attr("value", adresaSplit[0] );
			$("input[name=grad]").attr("value", adresaSplit[1]);
			$("input[name=postanskiBroj]").attr("value", adresaSplit[2]);
			

	});
			
}
