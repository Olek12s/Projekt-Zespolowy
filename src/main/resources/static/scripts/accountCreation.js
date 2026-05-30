function createAccount()
{
    var login = document.getElementById("login").value;
    var password = document.getElementById("password").value;

    fetch("http://127.0.0.1:8089/api/users/register",
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
    .then((response) => response.text())
    .then((text) =>
    {
        console.log(text);
        if(text != "Registered successfully")
        {
            document.getElementById("message").innerHTML = "<p class=\"messageIncorrect\">".concat(text, "</p>");
        }
        else
        {
            document.getElementById("message").innerHTML = "<p class=\"messageCorrect\">".concat(text, "</p>");
        }
    })
}

function goBack()
{
    window.location = "demo.html"
}