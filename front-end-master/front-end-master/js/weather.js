const apiKey = '5f9aaccc5debb683b09516f40cefa44e';

function getLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(weatherCallback2);
	} else {
		alert("Geolocation is not supported by this browser.");
	}
}

function weatherCallback2(position) {
	fetch('https://api.openweathermap.org/data/2.5/weather?lat=' + position.coords.latitude + '&lon=' + position.coords.longitude + '&appid=' + apiKey)
	.then(function(resp){
		return resp.json() //convert data to json
	})
	.then(function(data){
		drawWeather(data);
	})
	.catch(function(){
		console.log("Error with weatherCallback!");
	});}


function drawWeather( d ) {
	var celcius = Math.round(parseFloat(d.main.temp)-273.15);
	var fahrenheit = Math.round(((parseFloat(d.main.temp)-273.15)*1.8)+32);
	var description = d.weather[0].description;
	document.getElementById('hohoho').innerHTML = description;
	document.getElementById('hehehe').innerHTML = celcius + '&deg;';
	document.getElementById('reroro').innerHTML =  d.name;
	if( description.indexOf('rain') > 0 ) {
		document.body.className = 'rainy';
	} else if( description.indexOf('cloud') > 0 ) {
		document.body.className = 'cloudy';
	} else if( description.indexOf('sunny') > 0 ) {
		document.body.className = 'sunny';
	} else {
		document.body.className = 'clear';
	}
}

window.onload = function() {
	getLocation();
}
