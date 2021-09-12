$(document).ready(function(){
	console.log("registracija pocinje...");
	
	$.get("../WebProjekat/rest/registracija/",
			function(data, status){
		console.log("Status je: "  + status);
	}
			);
	
	$("form").submit(function(event){
		let ime = $("input[name=ime]").val();
		let prezime = $("input[name=prezime]").val();
		let korisnickoIme = $("input[name=korisnickoIme]").val();
		let lozinka = $("input[name=lozinka]").val();
		let datum = $("input[name=datumRodjenja]").val();
		console.log( "Uneseni datum je: " + datum);
		
		//proveriti koje je selektovano muski ili zenski pol
		let muskiPol = $("input[name=muski]");
		let zenskiPol = $("input[name=zenski]");
		let pol = "";
		
		if($("input[name=muski]:checked").val()){
			pol = muskiPol.val();
			console.log("Pritisnut je muski pol : " + muskiPol.val());
			$("input[name=zenski]").prop("checked", false);
		}
		if($("input[name=zenski]:checked").val()){
			pol = zenskiPol.val();
			console.log("Pritisnut je zenski pol: " + zenskiPol.val());
			$("input[name=muski]").prop("checked", false);
		}
		
		let kupac = {
			"ime": ime,
			"prezime": prezime,
			"korisnickoIme": korisnickoIme,
			"lozinka": lozinka,
			"datumRodjenja": datum,
			"pol": pol,
			"uloga": "KUPAC"
		};
		
		console.log(ime + "," + prezime + ", " + datum + "pol je: " + pol);
		
		//***VALIDACIJA >> za prazne unose..
		
		let dozvoljenaRegistracija = true;
		
		if(ime == ""){
			dozvoljenaRegistracija = false;
			console.log("prazno ime..................");
			($("input[name=ime]")).css("border-color", "red");
			$("input[name=ime]").parent().append("<p style=\"color:red\">Morate uneti ime korisnika.</p>")
		}
		
		if(prezime==""){
			dozvoljenaRegistracija = false;

			$("input[name=prezime]").css("border-color", "red");
			$("input[name=prezime]").parent().append("<p style=\"color:red\">Morate uneti prezime korisnika.</p>")

		}
		
		if(korisnickoIme==""){
			dozvoljenaRegistracija = false;

			$("input[name=korisnickoIme]").css("border-color", "red");
			$("input[name=korisnickoIme]").parent().append("<p style=\"color:red\">Morate uneti korisnicko ime korisnika.</p>")

		}
		
		if(lozinka==""){
			dozvoljenaRegistracija = false;

			$("input[name=lozinka]").css("border-color", "red");
			$("input[name=lozinka]").parent().append("<p style=\"color:red\">Morate uneti lozinku korisnika.</p>")

		}
		
		if(pol ==""){
			dozvoljenaRegistracija = false;

			$("input[name=zenski]").parent().append("<p style=\"color:red\">Morate izabrati pol korisnika.</p>")
		}
		
		
		event.preventDefault();
		
		//provera jedinstvenosti korisnickogImena 
		
		let jedinstvenoKorisnickoIme= true;
		
		$.get(
			"../WebProjekat/rest/korisnici/", 
			function (data, status){
				
				for(let korisnik of data){
					if(korisnik.korisnickoIme == korisnickoIme){
						jedinstvenoKorisnickoIme = false;
						$("p[name=poruka]").text("Korisnicko ime mora biti jedinstvene vrednosti.."); 

						
					}
				}
				
				// REGISTRACIJA >> ajax poziv za registrovanje
				
				if(dozvoljenaRegistracija == true && jedinstvenoKorisnickoIme == true){
					console.log("Dozvoljena je....");
					
					$.post(
					"../WebProjekat/rest/registracija/registracijaKupaca",
					JSON.stringify(kupac),
					
					function(data, status){
						console.log("Status je: " + status);
						console.log("Korisnik je registrovan...");
						$("p[name=poruka]").text("Uspesno ste se registrovali."); 
						$("div[name=porukaUputstvo]").append("<a href=\"logovanje.html\">Uloguj se</a>")
					}
							
					);
				}
			}
		);
		
	});
	
});