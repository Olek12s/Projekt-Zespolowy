let stompClient = null;
let gameId = sessionStorage.getItem("gameId");
let token = sessionStorage.getItem("token");

let playerId = null;
let selectedPiece = null;

let chatOpen = false;

let clockState = null;
let clockInterval = null;

let lastListedMove = null;
let moveCount = 0;

function connect() {
    const socket = new SockJS(`http://localhost:8089/ws?token=${token}`);   // ????????????????????????????????????????? safe ??????????????????????????????
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        console.log("Connected to WS");

        stompClient.subscribe('/topic/game/' + gameId, function (msg) {
            const state = JSON.parse(msg.body);
            renderBoard(state);
     });

        stompClient.subscribe('/user/queue/state', function (msg) {
            const state = JSON.parse(msg.body);
            console.log("INITIAL STATE:", state);
            renderBoard(state);
        });

        stompClient.subscribe('/topic/game/' + gameId + '/chat', function (msg) {
            const chat = JSON.parse(msg.body);
            const div = document.createElement('div');
            div.className = 'chat-msg';
            div.innerHTML = `<b>${chat.sender}:</b> ${chat.content}`;
            const msgs = document.getElementById('chat-messages');
            msgs.appendChild(div);
            msgs.scrollTop = msgs.scrollHeight;
        });

        stompClient.send("/app/state", {}, {});
    });

}

function sendMove(from, to) {
    stompClient.send("/app/move", {}, JSON.stringify({
        gameId: gameId,
        move: from + to,
    }));
}

function dragstartHandler(ev) {
    selectedPiece = ev.target;
}

function dragoverHandler(ev) {
    ev.preventDefault();
}

function dropHandler(ev) {
    ev.preventDefault();

    const targetCell = ev.target.closest(".cell");
    if (!targetCell || !selectedPiece) return;

    const from = getSquare(selectedPiece);
    const to = {
        col: targetCell.dataset.col,
        row: targetCell.dataset.row
    };

    sendMove(
        toChess(from.col, from.row),
        toChess(to.col, to.row)
    );

    selectedPiece = null;
}

function getSquare(element) {
    const cell = element.closest(".cell");
    return {
        col: cell.dataset.col,
        row: cell.dataset.row
    };
}

function toChess(col, row) {
    const file = String.fromCharCode(97 + parseInt(col));
    const rank = 8 - parseInt(row);
    return file + rank;
}

function renderBoard(state) {
    const board = state.board;
    updateChessClock(state);

    for (let r = 0; r < 8; r++) {
        for (let c = 0; c < 8; c++) {

            const cell = document.getElementById(`cell-${r}-${c}`);
            cell.innerHTML = "";

            const piece = board[r][c];
            if (piece) {
                const img = document.createElement("img");

                img.src = getImage(piece);
                img.draggable = true;
                img.ondragstart = dragstartHandler;

                cell.appendChild(img);
            }
        }
    }
    renderMoveList(state.moves);
}

// temporary
function renderMoveList(moves) {
    const list = document.getElementById("move-list");
    list.innerHTML = ""; // reset

    moveCount = 0;

    for (let i = 0; i < moves.length; i++) {
        const move = moves[i];

        const isWhite = i % 2 === 0;

        if (isWhite) {
            moveCount++;

            const row = document.createElement("div");
            row.id = `move-row-${moveCount}`;
            row.style.display = "flex";
            row.style.gap = "10px";

            row.innerHTML =
                `<span style="color:gray">${moveCount}.</span>
                 <span id="white-${moveCount}">${move}</span>`;

            list.appendChild(row);

        } else {
            const whiteSpan = document.getElementById(`white-${moveCount}`);
            if (whiteSpan) {
                const blackSpan = document.createElement("span");
                blackSpan.textContent = move;
                whiteSpan.parentElement.appendChild(blackSpan);
            }
        }
    }

    list.scrollTop = list.scrollHeight;
}

function getImage(piece) {
    switch (piece) {
        case "wP": return "./images/pieces/whitePawn.png";
        case "bP": return "./images/pieces/blackPawn.png";
        case "wR": return "./images/pieces/whiteRook.png";
        case "bR": return "./images/pieces/blackRook.png";
        case "wN": return "./images/pieces/whiteKnight.png";
        case "bN": return "./images/pieces/blackKnight.png";
        case "wB": return "./images/pieces/whiteBishop.png";
        case "bB": return "./images/pieces/blackBishop.png";
        case "wQ": return "./images/pieces/whiteQueen.png";
        case "bQ": return "./images/pieces/blackQueen.png";
        case "wK": return "./images/pieces/whiteKing.png";
        case "bK": return "./images/pieces/blackKing.png";
    }
    return "";
}

function createBoard() {
    const board = document.getElementById("board");

    for (let r = 0; r < 8; r++) {
        for (let c = 0; c < 8; c++) {

            const cell = document.createElement("div");
            cell.classList.add("cell");

            if ((r + c) % 2 === 0) {
                cell.classList.add("white");
            } else {
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

function sendChat() {
    const input = document.getElementById('chat-input');
    const content = input.value.trim();
    if (!content) return;
    stompClient.send("/app/chat", {}, JSON.stringify({
        gameId: gameId,
        content: content
    }));
    input.value = "";
}

function toggleChat() {
    const panel = document.getElementById('chatPanel');
    panel.style.display = panel.style.display === 'none' ? 'block' : 'none';
}

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('chat-input').addEventListener('keydown', function(e) {
        if (e.key === 'Enter') sendChat();
    });
});

function updateChessClock(state) {
    clockState = {
        whiteTimeMs: state.whiteTimeMs,
        blackTimeMs: state.blackTimeMs,
        whiteToMove: state.whiteToMove,
        lastClockUpdateMs: state.lastClockUpdateMs,
        receivedAt: Date.now()
    };

    renderClocks();

    if (!clockInterval) {
        clockInterval = setInterval(renderClocks, 1000);
    }
}

function renderClocks() {
    if (!clockState) return;

    const elapsed = Math.max(0, Date.now() - clockState.lastClockUpdateMs);

    let whiteTimeMs = clockState.whiteTimeMs;
    let blackTimeMs = clockState.blackTimeMs;

    if (clockState.whiteToMove) {
        whiteTimeMs = Math.max(0, whiteTimeMs - elapsed);
    } else {
        blackTimeMs = Math.max(0, blackTimeMs - elapsed);
    }

    document.getElementById("white-timer").textContent = formatClock(whiteTimeMs);
    document.getElementById("black-timer").textContent = formatClock(blackTimeMs);

    document.querySelector(".white-clock").classList.toggle("active-clock", clockState.whiteToMove);
    document.querySelector(".black-clock").classList.toggle("active-clock", !clockState.whiteToMove);
}

function formatClock(timeMs) {
    const totalSeconds = Math.ceil(timeMs / 1000);
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;

    return `${minutes}:${seconds.toString().padStart(2, "0")}`;
}

function addMoveToList(move, isWhite) {
    const list = document.getElementById("move-list");
    if (isWhite) {
        moveCount++;
        const row = document.createElement("div");
        row.id = `move-row-${moveCount}`;
        row.style.display = "flex";
        row.style.gap = "10px";
        row.innerHTML = `<span style="color:gray">${moveCount}.</span><span id="white-${moveCount}">${move}</span>`;
        list.appendChild(row);
    } else {
        const whiteSpan = document.getElementById(`white-${moveCount}`);
        if (whiteSpan) {
            const blackSpan = document.createElement("span");
            blackSpan.textContent = move;
            whiteSpan.parentElement.appendChild(blackSpan);
        }
    }
    list.scrollTop = list.scrollHeight;
}

window.onload = function () {
    if (!gameId) {
        alert("Brak gameId — wróć do lobby");
        window.location = "mainpage.html";
        return;
    }

    createBoard();
    connect();
};