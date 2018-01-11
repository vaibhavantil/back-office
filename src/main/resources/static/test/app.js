
var selectUser = document.querySelector(".select");
var chatUser = document.querySelector(".chat");

var stompClient;

var input = document.querySelector(".input-text");
var user = document.querySelector(".input-user");
var userNameSpan = document.querySelector(".user-name");

var messages = document.querySelector('.messages');

var userName = '123';

document.addEventListener("DOMContentLoaded", function () {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
    });
});

function send() {
    var text = input.value;
    var date = Date.now();

    input.value = "";
    stompClient.send("/app/messages/send/" + userName, {}, JSON.stringify({
        "id": "message.getname",
        "header": {
            "messageId": 4,
            "fromId": 1,
            "responsePath": "/response",
            "timeStamp": date
        },
        "body": {
            "type": "text",
            "id": 4,
            "text": text
        },
        "timestamp": date
    }));
}

function chat() {
    selectUser.style.display = 'none';
    chatUser.style.display = 'block';

    userName = user.value;
    userNameSpan.innerText = userName;

    stompClient.subscribe('/topic/messages/' + userName, function(response) {
        var div = document.createElement('div');
        var code = document.createElement('code');
        code.innerHTML = response.body;

        div.appendChild(code);
        messages.appendChild(div);
    });
}