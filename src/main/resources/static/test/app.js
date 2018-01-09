
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
    input.value = "";
    stompClient.send("/app/messages/send/" + userName, {}, JSON.stringify({ 'body': text }));
}

function chat() {
    selectUser.style.display = 'none';
    chatUser.style.display = 'block';

    userName = user.value;
    userNameSpan.innerText = userName;

    stompClient.subscribe('/topic/messages/' + userName, function(response) {
        var message = JSON.parse(response.body);

        var div = document.createElement('div');
        div.innerHTML = message.body;

        messages.appendChild(div);
    });
}