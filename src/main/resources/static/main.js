const DifficultyLevel = {
    easy: 'EASY',
    medium: 'MEDIUM',
    hard: 'HARD',
}

const GameModes = {
    new: 'game/create',
    connect: 'game/connect',
    random: 'game/connect/random',
}

class Player {
    constructor(userName, id) {
        this.userName = userName?.toString().trim() ? userName.toString().trim() : null;
        this.id = id > 0 ? id : null;
    }
}

class GameConnection {
    constructor(secondPlayer, gameId) {
        this.secondPlayer = secondPlayer?.userName && secondPlayer instanceof Player ? secondPlayer : null;
        this.id = gameId > 0 ? gameId : null;
    }
}

class GameCreate {
    constructor(firstPlayer, difficultyLevel) {
        this.firstPlayer = firstPlayer?.userName && firstPlayer instanceof Player ? firstPlayer : null;
        this.difficultyLevel = Object.values(DifficultyLevel).includes(difficultyLevel) ? difficultyLevel : null;
    }
}

class GamePlay {
    constructor(pitId, id, version) {
        this.pitId = pitId >= 1 && pitId <= 14 ? pitId : null;
        this.id = id > 0 ? id : null;
        this.version = version >= 0 ? version : null;
    }
}

const boardMap = {
    firstPitPlayerA: 1,
    secondPitPlayerA: 2,
    thirdPitPlayerA: 3,
    forthPitPlayerA: 4,
    fifthPitPlayerA: 5,
    sixthPitPlayerA: 6,
    rightPitHouseId: 14,
    firstPitPlayerB: 8,
    secondPitPlayerB: 9,
    thirdPitPlayerB: 10,
    forthPitPlayerB: 11,
    fifthPitPlayerB: 12,
    sixthPitPlayerB: 13,
    leftPitHouseId: 7,
}

const url = 'http://localhost:8080';
let stompClient = null;
let state = {
    playerOne: null,
    playerTwo: null,
    playerTurn: null,
    gamePlay: new GamePlay(),
}

function newGame() {
    const difficulty = prompt(`Please select your preferred difficulty level, exactly between '${DifficultyLevel.easy}', '${DifficultyLevel.medium}' or '${DifficultyLevel.hard}'.`, DifficultyLevel.hard);
    if (Object.values(DifficultyLevel).includes(difficulty)) {
        newPlayer(GameModes.new, difficulty);
    }
}

function connectGame() {
    const gameId = prompt('Please enter the gameId.');
    if (gameId) {
        newPlayer(GameModes.connect, gameId)
    }
}

function randomGame() {
    newPlayer(GameModes.random);
}

function newPlayer(gameMode, param) {
    const userName = prompt('Please enter your userName.', gameMode === GameModes.new ? 'Player One' : 'Player Two');
    if (userName?.toString().trim()) {
        $.ajax({
            url: `${url}/player`,
            type: 'POST',
            host: url,
            contentType: 'application/json',
            dataType: 'json',
            crossDomain: true,
            crossOrigin: true,
            data: JSON.stringify(new Player(userName)),
            success: function (playerId) {
                if (Object.values(GameModes).includes(gameMode) && playerId) {
                    startGame(userName, playerId, gameMode, param)
                }
            },
            error: function (jqXHR) {
                var responseText = jQuery.parseJSON(jqXHR.responseText);
                alert('error: ' + responseText.detail);
            }
        })
    }
}

function startGame(userName, playerId, gameMode, param) {
    let data = null;
    quickReset();
    if (gameMode === GameModes.new) {
        data = new GameCreate(new Player(userName, playerId), param);
    } else if (gameMode === GameModes.random) {
        data = new Player(userName, playerId);
    } else if (gameMode === GameModes.connect) {
        data = new GameConnection(new Player(userName, playerId), param);
    }
    $.ajax({
        url: `${url}/${gameMode}`,
        type: 'POST',
        host: url,
        contentType: 'application/json',
        dataType: 'json',
        crossDomain: true,
        crossOrigin: true,
        data: JSON.stringify(data),
        success: function (game) {
            if (gameMode === GameModes.new) {
                alert(`Game id is ${game?.id}.`);
            }
            setGame(game);
            connectToSocket(game?.id);
        },
        error: function (jqXHR) {
            var responseText = jQuery.parseJSON(jqXHR.responseText);
            alert('error: ' + responseText.detail);
        }
    })
}

function connectToSocket(gameId) {
    if (gameId) {
        $('#gameId').css('opacity', '1').text(`gameId: ${gameId}`);
        const socket = new SockJS(url + "/gameplay");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
                let data = JSON.parse(response.body);
                setGame(data);
            })
        })
    }
}

function setGame(game) {
    if (game.firstPlayer?.userName) {
        state.playerOne = game.firstPlayer?.userName;
        $('.house.player-one .user-name').text(state.playerOne).css('opacity', '1');
    }
    if (game.secondPlayer?.userName) {
        state.playerTwo = game.secondPlayer?.userName;
        $('.house.player-two .user-name').text(state.playerTwo).css('opacity', '1');
    }

    state.gamePlay.version = game?.version;
    state.gamePlay.id = game?.id;
    if (game?.winner) {
        alert(`The winner is ${game?.winner}`);
        quickReset();
    } else {
        setStates(game?.board);
        setPlayerTurn(game?.playerTurn);
    }
}

function setStates(board) {
    if (board) {
        $('.cell .state').text('');
        $('.stone').css('opacity', '0');
        setTimeout(() => {
            $('.stone').remove();
            const keys = Object.keys(board);
            for (let i = 0; i < keys?.length; ++i) {
                $(`#${boardMap[keys[i]]} .state`).text(board[keys[i]]?.toString());
                for (let j = 0; j < board[keys[i]]; ++j) {
                    $(`#${boardMap[keys[i]]}.cell`).append(`<span class="stone"></span>`);
                }
            }
            $('.stone').css('opacity', '1');
        }, 300);
    }
}

function quickReset() {
    $('.cell .state').text('');
    $('.stone').remove();
    $('.user-name').text('').css('opacity', '0');
    $('.status').text('').css('opacity', '0');
}

function addOnClick(playerType) {
    $(`.pits-row.${playerType} .cell`).on('click', function () {
        pitId = $(this).attr('id');
        if (state.playerTurn === state.playerOne && pitId >= 1 && pitId <= 6) {
            state.gamePlay.pitId = pitId;
            playGame();
        } else if (state.playerTurn === state.playerTwo && pitId >= 8 && pitId <= 13) {
            state.gamePlay.pitId = pitId;
            playGame();
        } else {
            alert('Please Wait! This is not your turn.')
        }
    });
}

function setPlayerTurn(playerTurn) {
    if (playerTurn) {
        $('#playerTurn').css('opacity', '1').text(`playerTurn: ${playerTurn}`);
        $('.cell').removeClass('not-allowed');
        $('#pits .cell').off();
        state.playerTurn = playerTurn;
        if (state.playerTurn === state.playerOne) {
            $('.player-two .cell').addClass('not-allowed');
            addOnClick('player-one');
        } else if (state.playerTurn === state.playerTwo) {
            $('.player-one .cell').addClass('not-allowed');
            addOnClick('player-two');
        }
        alert(`This is ${playerTurn}'s turn`)
    }
}

function playGame() {
    $.ajax({
        url: `${url}/game/gameplay`,
        type: 'POST',
        host: url,
        contentType: 'application/json',
        dataType: 'json',
        crossDomain: true,
        crossOrigin: true,
        data: JSON.stringify(state.gamePlay),
        error: function (jqXHR) {
            var responseText = jQuery.parseJSON(jqXHR.responseText);
            alert('error: ' + responseText.detail);
        }
    })
}

























