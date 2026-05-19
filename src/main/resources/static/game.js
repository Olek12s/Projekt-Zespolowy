let stompClient = null;

let gameId = sessionStorage.getItem("gameId");
let token = sessionStorage.getItem("token");

let playerId = null;
let selectedPiece = null;

function connect() 
{
    const socket = new SockJS(`http://localhost:8089/ws?token=${token}`);   // ????????????????????????????????????????? safe ??????????????????????????????
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () 
    {
        console.log("Connected to WS");

        stompClient.subscribe('/topic/game/' + gameId, function (msg) 
        {
            const state = JSON.parse(msg.body);
            renderBoard(state);
        });

        stompClient.subscribe('/user/queue/state', function (msg) 
        {
            const state = JSON.parse(msg.body);
            console.log("INITIAL STATE:", state);
            renderBoard(state);
        });

        stompClient.send("/app/state", {}, {});
    });
}

function sendMove(from, to) 
{
    stompClient.send("/app/move", {}, JSON.stringify(
    {
        gameId: gameId,
        move: from + to,
    }));
}

function dragstartHandler(ev) 
{
    selectedPiece = ev.target;
}

function dragoverHandler(ev) 
{
    ev.preventDefault();
}

function dropHandler(ev) 
{
    ev.preventDefault();

    const targetCell = ev.target.closest(".cell");
    if (!targetCell || !selectedPiece) return;

    const from = getSquare(selectedPiece);
    const to = 
    {
        col: targetCell.dataset.col,
        row: targetCell.dataset.row
    };

    sendMove(toChess(from.col, from.row), toChess(to.col, to.row));

    selectedPiece = null;
}

function getSquare(element) 
{
    const cell = element.closest(".cell");

    return { 
        col: cell.dataset.col, row: cell.dataset.row
    };
}

function toChess(col, row) 
{
    const file = String.fromCharCode(97 + parseInt(col));
    const rank = 8 - parseInt(row);
    return file + rank;
}

function renderBoard(state) 
{
    const board = state.board;

    for (let r = 0; r < 8; r++) 
    {
        for (let c = 0; c < 8; c++) 
        {
            const cell = document.getElementById(`cell-${r}-${c}`);
            cell.innerHTML = "";

            const piece = board[r][c];
            if (piece) 
            {
                const img = document.createElement("img");

                img.src = getImage(piece);
                img.draggable = true;
                img.ondragstart = dragstartHandler;

                cell.appendChild(img);
            }
        }
    }
}

function getImage(piece) 
{
    switch (piece) 
    {
        case "wP": return "whitePawn.png";
        case "bP": return "blackPawn.png";
        case "wR": return "whiteRook.png";
        case "bR": return "blackRook.png";
        case "wN": return "whiteKnight.png";
        case "bN": return "blackKnight.png";
        case "wB": return "whiteBishop.png";
        case "bB": return "blackBishop.png";
        case "wQ": return "whiteQueen.png";
        case "bQ": return "blackQueen.png";
        case "wK": return "whiteKing.png";
        case "bK": return "blackKing.png";
    }
    return "";
}

function createBoard() 
{
    const board = document.getElementById("board");

    for (let r = 0; r < 8; r++) 
    {
        for (let c = 0; c < 8; c++) 
        {
            const cell = document.createElement("div");
            cell.classList.add("cell");

            if ((r + c) % 2 === 0) 
            {
                cell.classList.add("white");
            }
            else 
            {
                cell.classList.add("black");
            }

            cell.id = `cell-${r}-${c}`;
            cell.dataset.row = r;
            cell.dataset.col = c;

            cell.ondrop = dropHandler;
            cell.ondragover = dragoverHandler;

            board.appendChild(cell);
        }
    }
}

window.onload = function () 
{
    if (!gameId) 
    {
        alert("Brak gameId — wróć do lobby");
        window.location = "mainpage.html";
        return;
    }

    createBoard();
    connect();
};