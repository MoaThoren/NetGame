if (window.WebSocket) {
    console.log("Start");
    var ws = new WebSocket("ws://192.168.10.243:8080/NetGame/message");
    //var ws = new WebSocket("ws://192.168.10.218:8080/NetGame/message");
    ws.onopen = function (event) {
        ws.send("all:::PetterKarlsson");
    };

    console.log("Start2");
    var i = 0;
    ws.onmessage = function(event) {
        var text = event.data;
        console.log('Text data: ' + text);
        if(i <= 1) {
            ws.send("all: Before single text");
        }
        i++;
        document.getElementById("chatList").insertAdjacentHTML( 'beforeend', '<li class="other"><div class="msg"><p>' + text + '</p></div></li>');
    };
}