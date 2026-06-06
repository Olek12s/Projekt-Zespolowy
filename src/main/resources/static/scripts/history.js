function processHistoryGame(playerGame)
{
    document.getElementById("historyTable").innerHTML += "".concat(
        "<tr class=\"historyRow\">",
            "<td class=\"historyCell\">", playerGame.whitePlayer, "</td>",
            "<td class=\"historyCell\">", playerGame.blackPlayer, "</td>",
            "<td class=\"historyCell\">", playerGame.createdAt, "</td>",
            "<td class=\"historyCell\">", playerGame.finishedAt, "</td>",
            "<td class=\"historyCell\">", playerGame.pgn, "</td>",
        "</tr>"
    )
}

function getHistoryGames()
{
    count="5";
    fetch("http://127.0.0.1:8089/api/games/history?n=".concat(count),
    {
        headers:
        {
            "accept": "*/*",
            "Authorization": "Bearer ".concat(sessionStorage.getItem("token"))
        }
    }
    )
    .then((response) => response.json())
    .then((json) =>
    {
        json.forEach(processHistoryGame);
    });
}