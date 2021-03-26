var temp = 70;

function incTemp() {
    temp += 1;
    document.getElementById("p3").innerText = temp + String.fromCharCode(176);
}

function decTemp() {
    temp -= 1;
    document.getElementById("p3").innerText = temp + String.fromCharCode(176);
}

function cooling() {
    document.getElementById("p2").innerText = "Currently:\n" + "Cooling";
}
function emrg() {
    document.getElementById("p2").innerText = "Currently:\n" + "EMRG";
}
function auto() {
    document.getElementById("p2").innerText = "Currently:\n" + "Auto";
}
function heating() {
    document.getElementById("p2").innerText = "Currently:\n" + "Heating";
}
function off() {
    document.getElementById("p2").innerText = "Currently:\n" + "Off";
}

function weatherBalloon() {
  // Create variables to change forecast and access API
  var temp = document.querySelector('.highLow');
  var desc = document.querySelector('.description');
  var loc = document.querySelector('.location');
  var zip = 30061;
  var key = '63beed7f43a577544b31778ccacc2617';

  //Gather API data and insert into page
  fetch('https://api.openweathermap.org/data/2.5/weather?zip=' + zip+ '&units=imperial&appid=' + key)  
  .then(function(resp) { return resp.json() }) // Convert data to json
  .then(function(data) {
    temp.innerText = "High: "+data['main']['temp_max'] + String.fromCharCode(176);
    temp.innerHTML += "<br>&nbsp;";
    temp.innerText += "Low: "+data['main']['temp_min'] + String.fromCharCode(176);

    desc.innerText = data['weather'][0]['description'];

    loc.innerText += data['name'];
  })
  .catch(function() {
    // catch any errors
  });
}

window.onload = function() {
  weatherBalloon();
}
