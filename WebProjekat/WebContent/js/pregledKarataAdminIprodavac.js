$(document).ready(function(){
	console.log("Upali smo u pregled karata korisnika");
	
	//KOG JE TIPA TRENUTNO ULOGOVANI KORISNIK
	$.get(
			"../WebProjekat/rest/profil/",
			function(data,status){
				console.log("Trenutno ulogovan je: " + JSON.stringify(data));
			
				if(data.uloga == "PRODAVAC"){
					//PRODAVCIMA PRIKAZUJEMO SAMO REZERVISANE KARTE SVIH KUPACA
					pregledRezervisanihKarataProdavac();
					
				}
				else if(data.uloga == "ADMINISTRATOR"){
					//ADMINISTRATORIMA JE OMOGUCEN PREGLED SVIH KARATA
					pregledSvihKarata();
				}
			}
		
	);
})

function pregledRezervisanihKarataProdavac(){
	$.get(
			"../WebProjekat/rest/karte/sveKarte",
			
			function(data, status){
				
				for( let karte of data){
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
				
				console.log("Status je: " + status);
				console.log("Pronadjeno je: " + JSON.stringify(data));
				
				
			}
		);
}

function pregledSvihKarata(){
	$.get(
			"../WebProjekat/rest/karte/sveKarte",
			
			function(data, status){
				
				for( let karte of data){
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
				
				console.log("Status je: " + status);
				console.log("Pronadjeno je: " + JSON.stringify(data));
				
				
			}
		);
}



