const username = checkCookie("username");
window.addEventListener("beforeunload", function (e) {
    let confirmationMessage = "\o/";
    (e || window.event).returnValue = confirmationMessage; //Gecko + IE
    return confirmationMessage;                            //Webkit, Safari, Chrome
});
let currentWord = "";
let gameMaster = false;

function Message(receiver, sender, message) {
    this.sender = sender;
    this.receiver = receiver;
    this.text = message;
    this.toString = function() {
      return this.sender + " sent a message to " + this.receiver + ", the text was: \"" + this.text + "\"";
    };
}

let ENCODING_DELIMITER = ";;;";
function encode(msg) {
    return msg.receiver + ENCODING_DELIMITER + msg.sender + ENCODING_DELIMITER + msg.text;
}

function decode(encodedMsg) {
    let decodedMsg = encodedMsg.split(ENCODING_DELIMITER);
    return new Message(decodedMsg[0], decodedMsg[1], decodedMsg[2]);
}

function setCookie(cname, cvalue, exhours) {
    let d = new Date();
    d.setTime(d.getTime() + (exhours * 60 * 60 * 1000));
    let expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
    let name = cname + "=";
    let ca = document.cookie.split(';');
    for(let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function checkCookie(name) {
    let username = getCookie(name);
    if (username !== "") {
        return username;
    } else {
        username = prompt("Please enter your name:", "");
        if (username !== "" && username !== null) {
            setCookie("username", username, 1);
            return username;
        }
    }
}

function returnChatBox(type, message, color) {
    let colorStyle = "";
    if(typeof color !== 'undefined') {
        if(color === '#f76262')
            colorStyle = "style=\"background:" + color + "; color:white;\"";
        else
            colorStyle = "style=\"background:" + color + "; color:#777\"";
    }
    if(type !== "server")
        return document.getElementById("chatList").insertAdjacentHTML( 'beforeend',
            '<li class="' + type + '">' +
                '<div class="msg" ' + colorStyle + '>' +
                    '<p>' +
                        '<p>' + message.sender + '</p>' +
                        message.text +
                    '</p>' +
                '</div>' +
            '</li>');
    else
        return document.getElementById("chatList").insertAdjacentHTML( 'beforeend',
            '<li class="' + type + '">' +
                '<div class="msg" ' + colorStyle + '>' +
                    '<p>' +
                        message.text +
                    '</p>' +
                '</div>' +
            '</li>');
}

function noGuessBox(word) {
    let div = document.createElement('div');
    div.innerHTML = '<div id="wordArea" class="wordarea"><p>' + word + '</p></div>';
    let guessArea = document.getElementById("sendGuessForm") !== null ? document.getElementById("sendGuessForm") : document.getElementById("wordArea");
    guessArea.replaceWith(div.firstChild);
}

function setupWebsocket() {
    this.ws = new WebSocket("ws://192.168.10.243:8080/NetGame/message");
    //this.ws = new WebSocket("ws://192.168.10.218:8080/NetGame/message");
    this.ws.onopen = function () {
        sendMessage("name", "server");
    };
    this.ws.onmessage = function(event) {
        let message = decode(event.data);
        if (message.sender === username || message.sender === "Game master: " + username) {
            returnChatBox("self", message);
            scrollToBot();
        }
        else if (message.sender === "server") {
            if (message.text.indexOf("The word you should explain is:") !== -1) {
                if(message.receiver === "all") {
                    gameMaster = true;
                    noGuessBox(message.text.split(': ')[1]);
                } else if (message.receiver === "gameMaster" && gameMaster === true) {
                    noGuessBox(message.text.split(': ')[1]);
                }
                currentWord = message.text.split(': ')[1];
            } else if(message.text.indexOf("right answer") !== -1) {
                returnChatBox("server", message, '#57ff44');
            } else if(message.text.indexOf("wrong answer") !== -1) {
                returnChatBox("server", message, '#f76262');
            } else
                returnChatBox("server", message);
            scrollToBot();
        } else if(message.sender.indexOf("master") !== -1) {
            returnChatBox("other", message, '#7fe2e8')
        } else {
            returnChatBox("other", message);
            scrollToBot();
        }
    };
    this.ws.onclose = function() {
        setTimeout(setupWebsocket(), 1000);
    };
    this.ws.onerror = function() {
        console.log("Error!");
    }
}

function sendMessage(message, receiver) {
    let value = document.getElementById("sendMsgForm:inputMessage").value;
    if(gameMaster)
        value = value.replace(currentWord, "*");
    document.getElementById("sendMsgForm:inputMessage").value = "";
    if(message === undefined)
        sendMessage(value);
    else if(receiver === undefined) {
        if(gameMaster)
            this.ws.send(encode(new Message("all", "Game master: " + username, message)));
        else
            this.ws.send(encode(new Message("all", username, message)));
    } else
        this.ws.send(encode(new Message(receiver, username, message)));
}

function sendGuess() {
    let value = document.getElementById("sendGuessForm:guessMessage").value.toLowerCase();
    document.getElementById("sendGuessForm:guessMessage").value = "";
    sendMessage(value, "server");
}

function scrollToBot() {
    document.getElementById("chatList").scrollIntoView({behavior: "instant", block: "end", inline: "nearest"});
}

if (window.WebSocket) {
    document.onload = setupWebsocket();
}