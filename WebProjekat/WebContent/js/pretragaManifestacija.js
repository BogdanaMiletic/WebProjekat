$(document).ready(function(){
	
	//za range slider izlistamo vrednosti od minimalne do maximalne cene ulaznice u sistemu
	//ajax poziv za vracanje svih manifestacija.
	$.get("../WebProjekat/rest/manifestacije/",
			function(data, status){
		
				//trazimo minimalnu cenu ulaznice
				let minCena = pronadjiMinimalnuCenu(data);
				
				//trazimo maximalnu cenu ulaznice
				let maxCena = pronadjiMaximalnuCenu(data);
				
				// postavimo minimalnu i max cenu slider rank
				$("input[name=od]").attr("min", minCena);
				$("input[name=od]").attr("max", maxCena);
				$("input[name=do]").attr("min", minCena);
				$("input[name=do]").attr("max", maxCena);
				console.log("minimalna cena je: " + minCena);
		}
	);
	
	let cenaOdUnesena = false;
	let cenaDoUnesena = false;
	
	//prilikom klika na slider prikazi koja vrednost je izabrana
	$("input[name=od]").change(function(event, value){
		$("label[name=odPasus").text($("input[name=od]").val());
		cenaOdUnesena = true;
		console.log("Promenjena vrednost na: " + $("input[name=od]").val());
	})
	$("input[name=do]").change(function(event, value){
		$("label[name=doPasus").text($("input[name=do]").val());
		cenaDoUnesena = true;
		console.log("Promenjena vrednost na: " + $("input[name=do]").val());
	})
	
	
	
	//kad se potvrdi pretraga manifestacija klikom na submit
	$("#formaZaPretraguManifestacija").submit(function(event){
		
		//>>preuzimamo popunjene podatke za pretragu
		let naziv = $("input[name=nazivPretraga]").val();
		let lokacija= $("input[name=lokacijaPretraga]").val();
		let datumOd = $("input[name=odDatum]").val();
		let datumDo= $("input[name=doDatum]").val();
		
		let cenaOd = "";
		if(cenaOdUnesena)
			cenaOd = $("input[name=od]").val();
		
		let cenaDo=""
		if(cenaDoUnesena)	
			cenaDo = $("input[name=do]").val();
		
		//treba uraditi validaciju opsega cene..
		
		
		
	//>> sad serveru saljemo te podatke i vrsimo objedinjenu pretragu..
		$.get(
			"../WebProjekat/rest/manifestacije/pretraga?naziv="+naziv+"&lokacija=" +lokacija+"&datumOd="+datumOd+"&datumDo="+
			datumDo+"&cenaOd="+cenaOd+"&cenaDo="+cenaDo,
			function(data,status){
				console.log("***********REZULTATI PRETRAGE SU************");
					
				for(d of data){
					console.log(JSON.stringify(d));
				}
				
				//sad treba prikazati ovo sto je pronadjeno..>>>>
				prikaziRezultatePrtrage(data);
			}
		);
		
		
		
		
		event.preventDefault();
	});
	
	
});

function pronadjiMinimalnuCenu(data){
	//trazimo minimalnu cenu ulaznice
	let minimalnaCena = 0;
	let brojac = 0;
	
	for(manifestacija of data){
		if(brojac === 0)
			minimalnaCena = manifestacija.cenaRegularKarte;
		else{
			if(minimalnaCena > manifestacija.cenaRegularKarte){
				minimanlnaCena = manifestacija.cenaRegularKarte;
			}
		}
		brojac += 1;
	}
	return minimalnaCena;
}
function pronadjiMaximalnuCenu(data){
	//trazimo maximalnu cenu ulaznice
	let maxCena = 0;
	let brojacMax = 0;
	
	for (manifestacija of data){
		if(brojacMax === 0)
			maxCena = manifestacija.cenaRegularKarte;
		else{
			if(manifestacija.cenaRegularKarte > maxCena){
				maxCena = manifestacija.cenaRegularKarte;
			}
		}
		brojacMax += 1;
	}
	return maxCena;
}

function validacijaUneseneCene(cenaOd, cenaDo){
	
		if(cenaOd <= ceneDo)
			return true
		else
			return false;
	
}
function prikaziRezultatePrtrage(data){
	
	//automatski obrisemo sve redove i kolone koje su trenutno prikazane
	$(".row").remove();
	
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