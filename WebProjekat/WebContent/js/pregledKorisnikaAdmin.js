$(document).ready(function(){
	
	// ** svi registrovani korisnici ***
	$.get(
			"../WebProjekat/rest/korisnici/",
			function(data, status){
				pregledKorisnika(data);
			}
	);
});

function pregledKorisnika(korisnici){
	//skrivamo ako je nesto vec prikazano
	$(".korisnikRed").hide();
	
	for(let korisnik of korisnici){
		$("#tabelaRegistrovanihKorisnika").append("<tr class=\"korisnikRed\">" +
				"" + "<td >" + korisnik.ime + "</td>" +
				"" + "<td >" + korisnik.prezime + "</td>" +
				"" + "<td >" + korisnik.korisnickoIme + "</td>" +
				"" + "<td >" + korisnik.lozinka + "</td>" +
				"" + "<td >" + korisnik.pol + "</td>" +
				"" + "<td >" + korisnik.datumRodjenja + "</td>" +
				"" + "<td >" + korisnik.uloga+ "</td>" +
				
				" </tr>");
	}
	
}