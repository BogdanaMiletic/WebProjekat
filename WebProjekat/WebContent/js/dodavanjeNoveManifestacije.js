$(document).ready(function(){
	$("#formaDodavanjeManifestacije").submit(function(event){
		console.log("Upaliiiii");
		let naziv = $("input[name=naziv]").val();
		let tip=$("#tip option:selected").val();
		let brojMesta= $("input[name=brojMesta]").val();
		let datum = $("input[name=datum]").val();
		let cena= $("input[name=cena]").val();
		/*let aktivnoStatus = "";
		let neaktivnoStatus = "";
		
		if($("input[name=aktivno]:checked").val()){
			aktivnoStatus = true;
			console.log("Selektovano je aktivno ... ");
		}
		if($("input[name=neaktivno]:checked").val()){
			neaktivnoStatus = true;
			console.log("Selektovano je neaktivno...");
		}*/
		
		let sirinaG = $("input[name=geografskaSirina]").val();
		let duzinaG = $("input[name=geografskaDuzina]").val();
		
		let grad = $("input[name=grad]").val();
		let ulicaIbroj = $("input[name=ulicaIbroj]").val();
		let postanskiBroj = $("input[name=postanskiBroj]").val();
		
		let poster = "";
		
		//validiramo podatke iz forme
		let validacijaPodataka = validacija(naziv, tip, brojMesta, datum, cena, sirinaG, duzinaG, grad, ulicaIbroj, postanskiBroj);
	
		let validacijaJedinstvenosti = validirajJedinstvenost(sirinaG, duzinaG, datum);
		if(validacijaJedinstvenosti == false){
			$("#poruka").text("Postoji vec zakazana manifestacija u zeljenom vremenu na istoj lokaciji.");
			console.log("***********POSTOJI VEC ZAKAZANA...");
		}
		else{
			console.log("Ne postoji vec zakazan..");
		}
		console.log("Vrednost validacije: " + validacijaJedinstvenosti);
		

		
		if (validacijaJedinstvenosti == true && validacijaPodataka == true){
			//>>>uputi poziv za dodavanje nove manifestacije..
			
			let adresaOblik = ulicaIbroj+"," + grad+"," + postanskiBroj;
			let lokacija = {
				"geografskaSirina": sirinaG,
				"geografskaDuzina": duzinaG,
				"adresa": adresaOblik
			}
			let novaManifestacija={
				"naziv": naziv,
				"tip": tip,
				"brojMesta": brojMesta,
				"datumIvremeOdrzavanja": datum,
				"cenaRegularKarte": cena,
				"status": "NEAKTIVNO",
				"lokacija": lokacija
			};
			
			$.post(
				"../WebProjekat/rest/manifestacije/dodajManifestaciju",
				JSON.stringify(novaManifestacija),
				function(data, status){
					console.log("Status je: " + status);
					$("#poruka").text("Manifestacija je uspesno dodata.....");
					//alert("Manifestacija je uspesno dodata...");
					
				}
				
			);
		}

		event.preventDefault();
	});
	
})

function validacija(naziv, tip, brojMesta, datum, cena, sirinaG, duzinaG, grad, ulicaIbroj, postanskiBroj){
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
	
	if(sirinaG==""){
		$("#porukaSirina").text("Morate uneti geografsku sirinu...");
		return false;
	}
	
	if(isNaN(sirinaG) && sirinaG != ""){
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
	}
	
		
	return true;
}

function validirajJedinstvenost(geografskaSirina, geografskaDuzina, vremeIdatum){
	//proveravamo da li postoji vec zakazana manifestacija u zeljenom vremenu na zeljenoj lokaciji
	
	var nePostoji = true;
	//vracamo listu svih manifestacija
	console.log("Upali smo.................."+ vremeIdatum);
	$.get(
			"../WebProjekat/rest/manifestacije/",
			function(data, status){
				for (podatak of data){
					let mesec = podatak.datumIvremeOdrzavanja.monthValue;
					let dan = podatak.datumIvremeOdrzavanja.dayOfMonth;
					if(mesec < 10){
						console.log("*******duzina meseca == 1");
						mesec = "0" + podatak.datumIvremeOdrzavanja.monthValue;
					}
					if(dan < 10){
						dan = "0" + podatak.datumIvremeOdrzavanja.dayOfMonth;
						console.log("DAn je "+ dan);
					}
					let datumOblik  = podatak.datumIvremeOdrzavanja.year+"-"+ mesec +"-"+ dan +"T" +podatak.datumIvremeOdrzavanja.hour+":"+podatak.datumIvremeOdrzavanja.minute;
					
					if( podatak.lokacija.geografskaSirina == geografskaSirina &&
							podatak.lokacija.geografskaDuzina == geografskaDuzina &&
							datumOblik == vremeIdatum){
						console.log("********8poklapa se...");
						nePostoji = false;
					}
				}
			}
		);
	return nePostoji;
}
