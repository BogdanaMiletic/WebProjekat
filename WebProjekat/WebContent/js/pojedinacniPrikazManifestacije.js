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
		}
	);
	
});