function getLoggedIn() {
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;

    var url = "http://localhost:9000/login";

    // fetch(url, {
    //     headers: {
    //         "Accept": "application/json",
    //         "Content-type": "application/json"
    //     },
    //     method: "POST",
    //     body: JSON.stringify({username: "platformAdmin", password: "platform"})
    // }).then(res => {
    //     console.log("res", res);
    // });

    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);

    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            console.log(xhr);
        }
    };

    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");

    var data = `{
        "username": "${username}",
        "password": "${password}"
    }`;

    xhr.send(data);
}

function forgetPassword() {
    var forgetPassword = document.getElementById('forget-password');
    alert("Go grab a beer and try to remember your password!!!");
}