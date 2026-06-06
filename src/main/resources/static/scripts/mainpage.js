let client = null;

function connectWebSocket() 
{
    const token = sessionStorage.getItem("token");

    const socket = new SockJS(`http://localhost:8089/ws?token=${token}`);
    client = Stomp.over(socket);

    client.connect({}, () => 
    {
        console.log("Połączono z WebSocket");

        client.subscribe("/user/queue/game", (msg) => 
        {
            const data = JSON.parse(msg.body);
            console.log("MATCH RESPONSE:", data);

            if (data.color === "WAITING") 
            {
                alert("Szukamy przeciwnika...");
            } 
            else 
            {
                alert("Znaleziono gre! ID: " + data.gameId + " kolor: " + data.color);

                sessionStorage.setItem("gameId", data.gameId);
                window.location = "game.html";
            }
        });
    });
}

function playButtonClicked() 
{
    if (!client) 
    {
        connectWebSocket();

        setTimeout(() => 
        {
            client.send("/app/join", {}, {});
        }, 500);
    } 
    else 
    {
        client.send("/app/join", {}, {});
    }
}

function historyButtonClicked()
{
    window.location = "history.html"
}