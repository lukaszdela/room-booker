function calcReservation(){
var dateFrom = new Date(document.getElementById('dateFrom').value);
var dateTo = new Date(document.getElementById('dateTo').value);
var breakfast = document.getElementById('breakfast');
var parking = document.getElementById('parking');
var counter = 0;
var breakfastCost;
var parkingCost;
var price = 120;
dateTo.setDate(dateTo.getDate() + 1);

while (dateFrom < dateTo){
  dateFrom.setDate(dateFrom.getDate() + 1);
  counter++;
}

if(breakfast.checked == true){
  breakfastCost = counter * 5;
}else {
  breakfastCost = 0;
}

if(parking.checked == true){
  parkingCost = counter * 5;
}else {
  parkingCost = 0;
}

document.getElementById('result').innerHTML = ((counter * price) + breakfastCost + parkingCost) + ' $';
}

function calcUpdate(){
var dateFrom = new Date(document.getElementById('dateFrom').value);
var dateTo = new Date(document.getElementById('dateTo').value);
var breakfast = document.getElementById('breakfast');
var parking = document.getElementById('parking');
var counter = 0;
var price = 120;
var breakfastCost;
var parkingCost;
dateTo.setDate(dateTo.getDate() + 1);

while (dateFrom < dateTo){
  dateFrom.setDate(dateFrom.getDate() + 1);
  counter++;
}

if(breakfast.checked == true){
  breakfastCost = counter * 5;
}else {
  breakfastCost = 0;
}

if(parking.checked == true){
  parkingCost = counter * 5;
}else {
  parkingCost = 0;
}

document.getElementById('price').value = ((counter * price) + breakfastCost + parkingCost);
}
