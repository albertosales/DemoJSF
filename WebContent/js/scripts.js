/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function showAlert(message) {
    document.getElementById("alert").innerHTML = message;
    document.getElementById("alert").classList.toggle("hidden");
    setTimeout(1000, function() {
        document.getElementById("alert").classList.toggle("hidden");
    document.getElementById("alert").innerHTML = "";
    });
}

