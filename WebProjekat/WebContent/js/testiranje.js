$(document).ready(function(){
	
	console.log("Ovo je testiranje");
	
	$.post("../WebProjekat/rest/korisnici/testiranje",
			"jflsdafljfjlljdsfa ",
			function(data, status){
				console.log("Status je: " + status);
				console.log(data);
						
	})
})