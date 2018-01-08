const LAPTOP_URI = 'ws://192.168.10.243:8080/NetGame/message';
const DESKTOP_URI = 'ws://192.168.10.218:8080/NetGame/message';
const ENCODING_DELIMITER = ";;;";
const lightGreen = '#57ff44';
const lightRed = '#f76262';
const lightBlue = '#7fe2e8';
const lightGrey = '#777';
const white = '#ffffff';
const username = checkCookie('username');

let currentWord = '';
let gameMaster = false;

function setCookie(name, value, expiryInHours) {
    let date = new Date();
    date.setTime(date.getTime() + (expiryInHours * 60 * 60 * 1000));
    let expires = 'expires=' + date.toUTCString();
    document.cookie = name + '=' + value + ';' + expires + ';path=/';
}

function getCookie(cookieName) {
    let name = cookieName + '=';
    let decodedCookie = decodeURIComponent(document.cookie);
    let cookieArray = decodedCookie.split(';');
    for(let i = 0; i < cookieArray.length; i++) {
        let cookie = cookieArray[i];
        while (cookie.charAt(0) === ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(name) === 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }
    return '';
}

function checkCookie(name) {
    let username = getCookie(name);
    if (username !== '') {
        return username;
    } else {
        username = prompt('Please enter your name:', '');
        if (username !== '' && username !== null) {
            setCookie('username', username, 1);
            return username;
        }
    }
}

function Message(receiver, sender, message) {
    this.sender = sender;
    this.receiver = receiver;
    this.text = message;
    this.toString = function() {
      return this.sender + ' sent a message to ' + this.receiver + ', the text was: "' + this.text + '"';
    };
}

function encode(msg) {
    return msg.receiver + ENCODING_DELIMITER + msg.sender + ENCODING_DELIMITER + msg.text;
}

function decode(encodedMsg) {
    let decodedMsg = encodedMsg.split(ENCODING_DELIMITER);
    return new Message(decodedMsg[0], decodedMsg[1], decodedMsg[2]);
}

function sendMessage(message, receiver) {
    if (message === undefined) {
        let value = document.getElementById('sendMsgForm:inputMessage').value;
        if (gameMaster)
            value = value.replace(currentWord, '*');
        clearElementValue('sendMsgForm:inputMessage');
        sendMessage(value);
    } else if (receiver === undefined) {
        if (gameMaster) this.ws.send(encode(new Message('all', 'Game master: ' + username, message)));
        else            this.ws.send(encode(new Message('all', username, message)));
    } else this.ws.send(encode(new Message(receiver, username, message)));
}

function sendGuess() {
    let value = document.getElementById('sendGuessForm:guessMessage').value.toLowerCase();
    clearElementValue('sendGuessForm:guessMessage');
    sendMessage(value, 'server');
}

function returnChatBox(type, message, bColor, tColor) {
    let colorStyle = '';
    if (typeof bColor !== 'undefined') colorStyle = 'style="background: ' + bColor + '; color: ' + tColor + '"';
    if (type !== 'server')
        return document.getElementById('chatList').insertAdjacentHTML( 'beforeend',
            '<li class="' + type + '">' +
                '<div class="msg" ' + colorStyle + '>' +
                    '<p>' +
                        '<p>' + message.sender + '</p>' +
                        message.text +
                    '</p>' +
                '</div>' +
            '</li>');
    else
        return document.getElementById('chatList').insertAdjacentHTML( 'beforeend',
            '<li class="' + type + '">' +
                '<div class="msg" ' + colorStyle + '>' +
                    '<p>' + message.text + '</p>' +
                '</div>' +
            '</li>');
}

function replaceGuessBoxWithWordBoxForGameMaster(wordToDisplay) {
    let div = document.createElement('div');
    div.innerHTML = '<div id="wordArea" class="wordarea">Press word for help<a href="http://www.thesaurus.com/browse/' + wordToDisplay + '" target="_blank"><p>' + wordToDisplay + '</p></a></div>';
    let guessArea = document.getElementById("sendGuessForm") !== null ? document.getElementById('sendGuessForm') : document.getElementById('wordArea');
    guessArea.replaceWith(div.firstChild);
}

function scrollToBot() {
    document.getElementById('chatList').scrollIntoView({behavior: 'instant', block: 'end', inline: 'nearest'});
}

function clearElementValue(element) {
    document.getElementById(element).value = '';
}

function setupWebsocket() {
    //this.ws = new WebSocket(LAPTOP_URI);
    this.ws = new WebSocket(DESKTOP_URI);
    this.ws.onopen = function () {  sendMessage('name', 'server');  };

    this.ws.onmessage = function(event) {
        let message = decode(event.data);
        if (message.sender === username || message.sender === 'Game master: ' + username) {
            returnChatBox('self', message);
            scrollToBot();

        } else if (message.sender === 'server') {
            if (message.text.indexOf('The word you should explain is:') !== -1) {
                let guessWord = message.text.split(': ')[1];
                if (message.receiver === 'all') {
                    gameMaster = true;
                    replaceGuessBoxWithWordBoxForGameMaster(guessWord);

                } else if (message.receiver === 'gameMaster' && gameMaster === true)
                    replaceGuessBoxWithWordBoxForGameMaster(guessWord);

                currentWord = guessWord;

            } else if (message.text.indexOf('right answer') !== -1) returnChatBox('server', message, lightGreen, lightGrey);

            else if (message.text.indexOf('wrong answer') !== -1) returnChatBox('server', message, lightRed, white);

            else returnChatBox('server', message, lightGrey);

        } else if (message.sender.indexOf('master') !== -1) returnChatBox('other', message, lightBlue);

        else returnChatBox('other', message);

        scrollToBot();
    };

    this.ws.onclose = function() {  setTimeout(setupWebsocket(), 1000); };

    this.ws.onerror = function() {  console.log('Error!');  }
}

if (window.WebSocket)
    document.onload = setupWebsocket();
else
    returnChatBox('server',
        new Message('all', 'server', 'YOUR BROWSER DOESN\'T SUPPORT WEBSOCKET, THUS THIS APPLICATION CANNOT CONNECT.'));