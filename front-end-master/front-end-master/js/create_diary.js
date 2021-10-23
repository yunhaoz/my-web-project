document.querySelector("form").onsubmit = function(e) {
	e.preventDefault();
	let title = document.querySelector("#title").value.trim();
	let date = new Date(document.querySelector("#date").value);
	let content = document.querySelector("#content").value;
	if (title.length == 0) {
		document.querySelector("#title + .error").style.display = "block";
	} else {
		document.querySelector("#title + .error").style.display = "none";
	}
	if (isNaN(date.getTime())) {
		document.querySelector("#date + .error").style.display = "block";
	} else {
		document.querySelector("#date + .error").style.display = "none";
	}
	if ( $("#title + .error").css("display") == "block" || $("#date + .error").css("display") == "block" ) {
		return false;
	}
	if (content.length == 0) {
		if ( !confirm("Are you sure you want to create an empty diary?") ) {
			return false;
		}
	}
	
	$.get("/api/userid", {}, function(data){
  		$.ajax({
		method: "POST",
		url: "/api/event/diary",
		// Fixed: contentType must be explicitly defined if submitted as a form
		// form auto-submits as "application/x-www-form-urlencoded"
		contentType: "application/json",
		// Fixed: for JSON, data must be explicitly parsed
		data: JSON.stringify({
    		userid: data,
    		start: document.querySelector("#date").value,
			end: document.querySelector("#date").value,
    		title: title,
			description: content,
			visibility: 1,
			type: "diary",
			location: "Los Angeles"
		})
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
	});
}

document.querySelector("#title").onchange = function() {
	let title = document.querySelector("#title").value.trim();
	if (title.length != 0) {
		document.querySelector("#title + .error").style.display = "none";
	}
	document.querySelector("#title").value = title;
}

document.querySelector("#date").onchange = function() {
	let date = new Date(document.querySelector("#date").value);
	if (isNaN(date.getTime())) {
		document.querySelector("#date + .error").style.display = "block";
	} else {
		document.querySelector("#date + .error").style.display = "none";
	}
}