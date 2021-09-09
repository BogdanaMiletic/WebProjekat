$(document).ready(function(){
	// omogucujemo prikaz trenutno aktivnih podataka za konkretnog ulogovanog korisnika.
	console.log("Upali smo u profil...");
	
	$.get(
			"../WebProjekat/rest/profil/",
			function(data,status){
				console.log("Trenutno ulogovan je: " + JSON.stringify(data));
				imeSadrzaj = data.ime;
				prezimeSadrzaj = data.prezime;
				korisnickoImeSadrzaj = data.korisnickoIme;
				lozinikaSadrzaj = data.lozinka;
				
				//sad je potrebno postaviti na html stranicu placeholedere sa ovim podacima..
				$("input[name=ime]").attr("value", data.ime);
				$("input[name=prezime").attr("value", data.prezime);
				$("input[name=lozinka").attr("value", data.lozinka);
				$("input[name=korisnickoIme").attr("value", data.korisnickoIme);
				$("input[name=datumRodjenja").attr("value", data.datumRodjenja);
				$("label[name=uloga]").parent().append( data.uloga );
				if(data.pol == "ZENSKI"){
					$("input[name=zenski]").prop("checked", true);
					console.log("Checkirano je zenski... poll");
				}
				else{
					$("input[name=muski]").prop("checked", true);
					console.log("Chekiran je muski polll");
				}
				
				//********** PREGLED KARATA ZA KORISNIKE SA ULOGOM >>> KUPAC ***********
				if(data.uloga == "KUPAC"){
					console.log("Trenutno je ulogovan kupac....");
					//dodajemo u zaglavlje onda link za pregled rezervisanih karata
					$("#zaglavljePregledKarata").append("<a href=\"pregledRezervisanihKarata.html\"> pregled karata </a>")
					
					pregledRezervisanihKarata(data);
					pretragaKarataPoNazivu(data);
				}
				else{
					$(".pretragaKarata").hide();
					$(".pregledRezervisanihKarataKorisnika").hide();
				}
			}
			
	);
	
	$("input[name=ime]").change(function(){
		console.log("Promenjeno je ime...");
	});
	
	//ako je neki input polje menjano obelezimo to i upisujemo onda novu vrendost...
	
	
	//ako se desi neka promena u podacima i ako se klikne na promeni podatke..
	$("#formaPodatciProfila").submit(function(event){
		//validacija promenjenih podataka..
		let validacija = true;
		
		if($("input[name=ime]").val() == ""){
			$("input[name=ime]").parent().append("<p name=\"imePoruka\" style=\"color:red\">Ime ne sme biti prazno </p>");
			validacija = false;
		}
		else{
			$("p[name=imePoruka]").hide();
		}
		if($("input[name=prezime]").val() == ""){
			$("input[name=prezime]").parent().append("<p name=\"prezimePoruka\" style=\"color:red\">Prezime ne sme biti prazno </p>");
			validacija =  false;
		}
		else{
			$("p[name=prezimePoruka]").hide();
		}
		if($("input[name=korisnickoIme]").val() == ""){
			$("input[name=korisnickoIme").parent().append("<p name=\"korisnickoImePoruka\" style=\"color:red\">Korisnicko ime ne sme biti prazno </p>")
			validacija = false;
		}
		else{
			$("p[name=korisnickoImePoruka").hide();
		}
		if($("input[name=lozinka]").val() == ""){
			$("input[name=lozinka").parent().append("<p name=\"lozinkaPoruka\" style=\"color:red\">Lozinka  ne sme biti prazna </p>")
			validacija = false;
		}
		else{
			$("p[name=lozinkaPoruka").hide();
		}
		//dodatno ako je potrebno promeni datum rodjenja i pol..
		
		if(validacija === true){
			//ako su svi podaci ispravni promeni ih...
			console.log("Uspesna validacija...........................");
			//PROVERI STA JE MENJANO A STA NIJE >> AKO JE NESTO PROMENJENO ZADRZI STARE VREDNOSTI....
			
			let noviPodaci={
					"ime": $("input[name=ime]").val(),
					"prezime": $("input[name=prezime]").val(),
					"korisnickoIme": $("input[name=korisnickoIme]").val(),
					"lozinka": $("input[name=lozinka]").val(),
					"datumRodjenja":$("input[name=datumRodjenja]").val(),
					"pol": "MUSKI",
					"uloga": "KUPAC"
			};
			//datum rodjenja pol i ulogu svakako ne mozemo da menjamo zadrzacemo prethodne..
			
			$.ajax({
				type: "PUT",
				url: "../WebProjekat/rest/profil/promeniPodatkeProfila",
				data: JSON.stringify(noviPodaci),
				success: function(data,status){
						console.log("Status je: " + status);
				}
			});
			
			//alert("Uspesno ste promenili podatke sa korisnickog profila..");
			console.log("Uspesno promenjeni podatci s profila..");
		}
		else{
			//ako nismo prosli validaciju..
			console.log("Validacija je neuspesno prosla...");
		}	
		event.preventDefault();

	});
	
})

//************ PREGLED REZERVISANIH KARATA KORSNIKA ************

function pregledRezervisanihKarata(korisnik){
	$(".pregledRezervisanihKarataKorisnika").append("<p>Pregled rezervisanih karata korisnika..<p/>");
	
	for( let karte of korisnik.sveKarte){
		if(karte.status == "REZERVISANA"){
			$("#tabelaRezervisanihKarata").append("<tr class=\"redovi\">" +
					"" + "<td >" + karte.id + "</td>" +
					"" + "<td >" + karte.manifestacijaZaKojuJeRezervisana + "</td>" +
					"" + "<td >" + karte.datumIvremeManifestaije + "</td>" +
					"" + "<td >" + karte.cena + "</td>" +
					"" + "<td >" + karte.kupacImeIprezime + "</td>" +
					"" + "<td >" + karte.status + "</td>" +
					"" + "<td >" + karte.tipKarte + "</td>" +
					
					" </tr>");
		}
	}	
}

function pretragaKarataPoNazivu(korisnik){
	$("#pretragaPoNazivu").submit(function(event){
		let nazivZaPretragu = $("input[name=pretragaPoNazivu]").val();
		console.log("Naziv je: " + nazivZaPretragu);
		
		//upucujemo zahtev za pretragu ..
		
		$.get(
			"../WebProjekat/rest/karte/pretragaPoNazivu?naziv=" + nazivZaPretragu,
			
			function(data, status){
				console.log("Status je: " + status);
				console.log("Pronadjeno je: " + JSON.stringify(data));
				listaZaPrikaz(data, korisnik);
				
				
			}
		);
		event.preventDefault();
	});
	
function listaZaPrikaz(listaKarata, korisnik){
	// ****brisemo sve prethodno prikazane karte
	$(".redovi").hide();
	
	//sad dodajemo sve te karte u tabelu i prikazujemo ih kao redove
	for( let karte of listaKarata){
		if(karte.status == "REZERVISANA"){
			$("#tabelaRezervisanihKarata").append("<tr class=\"redovi\">" +
					"" + "<td >" + karte.id + "</td>" +
					"" + "<td >" + karte.manifestacijaZaKojuJeRezervisana + "</td>" +
					"" + "<td >" + karte.datumIvremeManifestaije + "</td>" +
					"" + "<td >" + karte.cena + "</td>" +
					"" + "<td >" + karte.kupacImeIprezime + "</td>" +
					"" + "<td >" + karte.status + "</td>" +
					"" + "<td >" + karte.tipKarte + "</td>" +
					
					" </tr>");
		}
	}	
}


}


