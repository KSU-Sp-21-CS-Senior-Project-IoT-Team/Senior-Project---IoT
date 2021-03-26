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