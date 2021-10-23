document.querySelector("form").onsubmit = function(event) {
	event.preventDefault();
	let email = document.querySelector("#email").value;
	let password = document.querySelector("#password").value;
	if (email.indexOf(" ") >= 0) {
		document.querySelector(".error").innerHTML = "You can't have whitespace in email!";
		document.querySelector(".error").style.display = "block";
	} else if (password.length < 8) {
		document.querySelector(".error").innerHTML = "Length of password should be at least 8!";
		document.querySelector(".error").style.display = "block";
	} else {
		document.querySelector(".error").innerHTML = "";
		document.querySelector(".error").style.display = "none";
		let password = $("#password").val();

		// Li (v0.4.5): add encrypts password before sending to server again
		var encoded = CryptoJS.MD5(password + "ShoWTimE");
		document.querySelector("#password").value = encoded;
		
		email = $("#email").val();

		// Li (v0.4.5): add encrypts password before sending to server again
		var encoded = CryptoJS.MD5(password + "ShoWTimE");
		document.querySelector("#password").value = encoded;
		
		password = $("#password").val();

		// B (v0.2.6): moved login-server.js inline
		$.ajax({
			method: "POST",
			url: "/api/auth/login",
			contentType: "application/x-www-form-urlencoded",
			data: {
				email: email,
	    		password: password
			}
		})
		.done(function( data, textStatus, jqXHR ) {
			// on success logic
			window.location.href = "/timeline";
		})
		.fail(function( jqXHR, textStatus, errorThrown ) {
			alert("Or here");
			if (jqXHR.status == 401) {
				alert( "Unmatched email and password!" );
			} else {
				console.log("error!");
			}
		});
		
	}
}