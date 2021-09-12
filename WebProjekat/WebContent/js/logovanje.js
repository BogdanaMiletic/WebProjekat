$(document).ready(function(){
	
	let forma = $("form");
	
	forma.submit(function(event){
		
		let korisnickoIme = $("input[name=korisnickoIme]").val();
		let lozinka = $("input[name=lozinka]").val();
		
		console.log("Uneseno je korisicko ime: " + korisnickoIme + " " + lozinka);	
		
		let popunjenaPolja = true;
		if(korisnickoIme == ""){
			popunjenaPolja = false;
			$("#poruka").text("Morate popuniti korisnicko ime..");
		}
		if(lozinka == ""){
			popunjenaPolja = false;
			$("#poruka").text("Morate popuniti lozinku..");

		}
		
		if(popunjenaPolja == true){
			
			$.post("../WebProjekat/rest/korisnici/logovanje/",
					korisnickoIme + "|" + lozinka,
					function(data, status){
						console.log("Status je: " + status);
						console.log(data);
						
						
						if(data.korisnickoIme == null && data.lozinka == null){
							console.log("Neuspesno ste se ulgovali.....")
							$("#poruka").text("Uneseni su neispravni podaci...");
						}
						else{
							console.log("Ulogovani ste kao korisnik " + data.ime + " " + data.prezime + ".\nUloga korisnika je: " + data.uloga + ".");
							
							
							if(data.uloga == "KUPAC"){
								window.location.href="ulogovanKorisnik.html";
							}
							else if(data.uloga == "PRODAVAC"){
								window.location.href="prodavacUlogovan.html";
							
							}
							else if(data.uloga == "ADMINISTRATOR"){
								window.location.href="administratorUlogovan.html";

							}					
						}
					}
				);
			
		}
		
		event.preventDefault();
	});
	
	
	
});
