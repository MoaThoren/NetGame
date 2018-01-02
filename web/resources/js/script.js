const username = checkCookie("username");

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

function returnChatBox(type, message) {
    if(type !== "server")
        return document.getElementById("chatList").insertAdjacentHTML( 'beforeend',
            '<li class="' + type + '">' +
                '<div class="msg">' +
                    '<p>' +
                        '<p>' + message.sender + '</p>' +
                        message.text +
                    '</p>' +
                '</div>' +
            '</li>');
    else
        return document.getElementById("chatList").insertAdjacentHTML( 'beforeend',
            '<li class="' + type + '">' +
                '<div class="msg">' +
                    '<p>' +
                        message.text +
                    '</p>' +
                '</div>' +
            '</li>');
}

function setupWebsocket() {
    this.ws = new WebSocket("ws://192.168.10.243:8080/NetGame/message");
    //this.ws = new WebSocket("ws://192.168.10.218:8080/NetGame/message");
    this.ws.onopen = function () {
        console.log("Connection open");
    };
    this.ws.onmessage = function(event) {
        let message = decode(event.data);
        if(message.sender === username)
            returnChatBox("self", message);
        else if(message.sender === "server")
            returnChatBox("server", message);
        else
            returnChatBox("other", message);
    };
    this.ws.onclose = function() {
        setTimeout(setupWebsocket(), 1000);
        console.log("Connection was closed");
    };
    this.ws.onerror = function() {
        console.log("Error!");
    }
}

function sendMessage(message, receiver) {
    if(message === undefined)
        sendMessage(document.getElementById("sendMsgForm:inputMessage").value);
    else if(receiver === undefined)
        this.ws.send(encode(new Message(username, username, message)));
    else
        this.ws.send(encode(new Message(receiver, username, message)));
}

if (window.WebSocket) {
    setupWebsocket();
}