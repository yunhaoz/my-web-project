let validFname = false;
let validLname = false;
let validEmail = false;
let validPassword = false;
let validConfirmPassword = false;
document.querySelector("form").onsubmit = function(event) {
	event.preventDefault();
	if (validFname && validLname && validEmail && validPassword && validConfirmPassword) {
		let email = $("#email").val();
		let password = $("#password").val();
		let fname = $("#fname").val();
		let lname = $("#lname").val();

		// Li (v0.4.5): add encodes password before sending to server again
		let encoded = CryptoJS.MD5(password + "ShoWTimE").toString();
		
		// B (v0.2.6): updated AJAX request
		$.ajax({
			method: "POST",
			url: "/api/user",
			// Fixed: contentType must be explicitly defined if submitted as a form
			// form auto-submits as "application/x-www-form-urlencoded"
			contentType: "application/json",
			// Fixed: for JSON, data must be explicitly parsed
			data: JSON.stringify({
				username: email,
	    		email: email,
	    		fname: fname,
				lname: lname,
				// Li(v0.4.5)
	    		password: encoded
			})
		})
		.done(function( data, textStatus, jqXHR ) {
			window.location.href = "/login";
		})
		.fail(function( jqXHR, textStatus, errorThrown ) {
			if (jqXHR.status == 409) {
				alert( "This email is already taken!" );
			} else {
				console.log("error!");
			}
		});
	} else {
		alert("Please fulfill all requirements first!");
	}
}

// validation of first name
document.querySelector("#fname").onchange = function() {
	let fname = this.value.trim();
	this.value = fname;
	if (fname.length == 0) {
		document.querySelector("#fname-error").style.display = "inline";
		this.classList.add("is-invalid");
		validFname = false;
	} else {
		document.querySelector("#fname-error").style.display = "none";
		this.classList.remove("is-invalid");
		validFname = true;
	}
}

// validation of last name
document.querySelector("#lname").onchange = function() {
	let lname = this.value.trim();
	this.value = lname;
	if (lname.length == 0) {
		document.querySelector("#lname-error").style.display = "inline";
		this.classList.add("is-invalid");
		validLname = false;
	} else {
		document.querySelector("#lname-error").style.display = "none";
		this.classList.remove("is-invalid");
		validLname = true;
	}
}

// validation of email
document.querySelector("#email").onchange = function() {
	let email = this.value.trim();
	this.value = "";
	this.value = email;
}
document.querySelector("#email").oninput = function() {
	let email = this.value.trim();
	if (!validateEmail(email)) {
		document.querySelector("#email-error").style.display = "inline";
		this.classList.add("is-invalid");
		validEmail = false;
	} else {
		document.querySelector("#email-error").style.display = "none";
		this.classList.remove("is-invalid");
		validEmail = true;
	}
}

// validation of password
document.querySelector("#password").oninput = function() {
	let password = this.value;
	let errorMessage = validatePassword(password);
	if ( errorMessage != "true" ) {
		document.querySelector("#password-error").innerHTML = errorMessage;
		document.querySelector("#password-error").style.display = "inline";
		this.classList.add("is-invalid");
		validPassword = false;
	} else {
		if (password.length < 8) {
			document.querySelector("#password-error").innerHTML = "Minimum length: 8!";
			document.querySelector("#password-error").style.display = "inline";
			this.classList.add("is-invalid");
			validPassword = false;
		} else {
			document.querySelector("#password-error").style.display = "none";
			this.classList.remove("is-invalid");
			document.querySelector("#password-note").style.display = "none";
			validPassword = true;
		}
	}
	if (document.querySelector("#confirmPassword").value != this.value) {
		document.querySelector("#confirmPassword-error").style.display = "inline";
		document.querySelector("#confirmPassword").classList.add("is-invalid");
		validConfirmPassword = false;
	} else {
		document.querySelector("#confirmPassword-error").style.display = "none";
		document.querySelector("#confirmPassword").classList.remove("is-invalid");
		validConfirmPassword = true;
	}
}

// validation of confirm password
document.querySelector("#confirmPassword").oninput = function() {
	if (this.value != document.querySelector("#password").value) {
		document.querySelector("#confirmPassword-error").style.display = "inline";
		this.classList.add("is-invalid");
		validConfirmPassword = false;
	} else {
		document.querySelector("#confirmPassword-error").style.display = "none";
		this.classList.remove("is-invalid");
		validConfirmPassword = true;
	}
}

// validating email function
function validateEmail(email) {
    const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

// validate password and return error message
function validatePassword(password) {
	let lowercaseLetters = /[a-z]/g;
	let uppercaseLetters = /[A-Z]/g;
	let numbers = /[0-9]/g;
	if (password.match(lowercaseLetters)) {
		if (password.match(uppercaseLetters)) {
			if (password.match(numbers)) {
				if (/\s/.test(password)) {
					return "No whitespace allowed!";
				} else {
					return "true";
				}
			} else {
				return "Must contain an number!";
			}
		} else {
			return "Must contain an uppercase letter!";
		}
	} else {
		return "Must contain a lowercase letter!";
	}
}