document.querySelector("form").onsubmit = function(e) {
	e.preventDefault();
	let title = document.querySelector("#title").value.trim();
	document.querySelector("#title").value = title;
	if (title.length == 0) {
		document.querySelector("#title + .error").style.display = "block";
	} else {
		document.querySelector("#title + .error").style.display = "none";
	}
	let deadline = new Date(document.querySelector("#date").value)
	if ( isNaN(deadline.getTime()) ) {
		document.querySelector("#date + .error").style.display = "block";
	} else {
		document.querySelector("#date + .error").style.display = "none";
	}
	let remindTime = new Date(document.querySelector("#remindTime").value)
	if ( isNaN(remindTime.getTime()) ) {
		document.querySelector("#remindTime + .error").style.display = "block";
	} else {
		document.querySelector("#remindTime + .error").style.display = "none";
	}
	let notes = document.querySelector("#notes").value;
	
	var myDate=new Date();
	myDate.setDate(myDate.getDate()+1);
	if ( $("#date + .error").css("display") == "block" || $("#remindTime + .error").css("display") == "block" || $("#title + .error").css("display") == "block" ) {
		return false;
	}
	let priority = document.querySelector("#priority").value;
	// let visibility = document.querySelector("#visibility").value;
	$.get("/api/userid", { email: "email" }, function(data){
  		$.ajax({
		method: "POST",
		url: "/api/event/reminder",
		// Fixed: contentType must be explicitly defined if submitted as a form
		// form auto-submits as "application/x-www-form-urlencoded"
		contentType: "application/json",
		// Fixed: for JSON, data must be explicitly parsed
		data: JSON.stringify({
			remind_time: document.querySelector("#remindTime").value,
			priority: priority,
    		userid: data,
    		start: document.querySelector("#date").value,
			end: document.querySelector("#date").value,
    		title: title,
			description: notes,
			visibility: 0,
			type: "reminder",
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