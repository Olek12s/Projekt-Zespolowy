function createAccount()
{
    var login = document.getElementById("login").value;
    var password = document.getElementById("password").value;

    fetch("http://127.0.0.1:8089/api/user/register",
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
    .then((response) => 
    {
        console.log(response);
        if(response == "Registered successfully")
        {
            document.getElementById("message").innerHTML = "<p class=\"messageCorrect>".concat(response, "</p>")
        }
        else
        {
            document.getElementById("message").innerHTML = "<p class=\"messageIncorrect>".concat(response, "</p>")
        }
    })
}

function goBack()
{
    window.location = "demo.html"
}