document.querySelector("form").onsubmit = function(e) {
	e.preventDefault();
	let title = document.querySelector("#title").value.trim();
	document.querySelector("#title").value = title;
	if (title.length == 0) {
		document.querySelector("#title + .error").style.display = "block";
	} else {
		document.querySelector("#title + .error").style.display = "none";
	}
	let method = document.querySelector("#method").value.trim();
	document.querySelector("#method").value = method;
	let date = new Date(document.querySelector("#time").value);
	if (isNaN(date.getTime())) {
		document.querySelector("#time + .error").style.display = "block";
	} else {
		document.querySelector("#time + .error").style.display = "none";
	}
	let amount = document.querySelector("#amount").value;
	if (amount.length == 0) {
		document.querySelector("#amount + .error").innerHTML = "Please enter amount of this transaction!";
		document.querySelector("#amount + .error").style.display = "block";
	} else if (amount == "0") {
		document.querySelector("#amount + .error").innerHTML = "Amount cannot be 0!";
		document.querySelector("#amount + .error").style.display = "block";
	} else {
		document.querySelector("#amount + .error").innerHTML = "";
		document.querySelector("#amount + .error").style.display = "none";
	}
	let content = document.querySelector("#content").value;
	if ( $("#amount + .error").css("display") == "block" || $("#time + .error").css("display") == "block" || $("#title + .error").css("display") == "block" ) {
		return false;
	}
	$.get("/api/userid", {}, function(data){
  		$.ajax({
		method: "POST",
		url: "/api/event/budget",
		// Fixed: contentType must be explicitly defined if submitted as a form
		// form auto-submits as "application/x-www-form-urlencoded"
		contentType: "application/json",
		// Fixed: for JSON, data must be explicitly parsed
		data: JSON.stringify({
			amount: amount,
			category: document.querySelector("#category").value,
    		userid: data,
    		start: document.querySelector("#time").value,
			end: document.querySelector("#time").value,
    		title: title,
			description: content,
			visibility: 1,
			type: "budget",
			location: "Los Angeles"
		})
		})
		.done(function( data, textStatus, jqXHR ) {
			// on success logic, redirect?			
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
	});
}



function validateEmail(email) {
    const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}