function processPlayerGame(playerGame)
{
    console.log(playerGame)
}

function getUserHistory()
{
    userId="0";
    count="5";
    fetch("http://127.0.0.1:8089/api/games/player/".concat(userId, "/recent/", count))
    .then((response) => response.json())
    .then((json) =>
    {
        json.forEach(processPlayerGame);
    });
}