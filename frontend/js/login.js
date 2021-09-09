function getLoggedIn() {
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;

    var url = "http://localhost:9000/login";

    var xhr = new XMLHttpRequest();
    xhr.open("POST", url);

    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            console.log(xhr.status);
            console.log(xhr.responseText);
        }};

    var data = {
      "username": username,
      "password": password
    };

    var response = xhr.send(data);

    alert(response);
}

function forgetPassword() {
    var forgetPassword = document.getElementById('forget-password');
    alert("Go grab a beer and try to remember your password!!!");
}