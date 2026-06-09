function signIn()
{
    var login = document.getElementById("login").value;
    var password = document.getElementById("password").value;

    fetch("http://127.0.0.1:8089/api/auth/login",
    {
        method: "POST",
        body: JSON.stringify(
        {
            login: login,
            password: password
        }),
        headers:
        {
            "Content-type": "application/json; charset=UTF-8"
        }
    })
    .then((response) => response.json())
    .then((json) =>
    {
        console.log(json);
        sessionStorage.setItem("token", json.token);
        document.getElementById("demo").innerHTML = json.message
        if(json.token != null)
        {
            window.location = "mainpage.html"
        }
    });
}

function register()
{
    window.location = "accountCreation.html"
}