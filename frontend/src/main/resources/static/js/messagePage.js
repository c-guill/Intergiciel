const chat = document.getElementById('chat');

chat.scrollTo(0, chat.scrollHeight);

let stompClient = null;

function connect() {
    const socket = new SockJS('http://localhost:8080/ws'); // Change the URL to your WebSocket endpoint
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        // console.log('Connected: ' + frame);
        stompClient.subscribe('/all/messages', function (response) {
            var userid = getCookie("id")
            message = JSON.parse(response.body);
            if (message.idDestination === parseInt(userid)) {
                showMessage(message, false)
            }

        });
    });
}

function showMessage(message, selfMessage) {
    if (selfMessage) {
        chat.insertAdjacentHTML('beforeend',
            "<div class='d-flex flex-column justify-content-end align-items-end'>" +
            "<label class='label-message'>"+message.senderusername + " - "+ message.date +"</label>" +
            "<p class='message message-moi'>"+message.message+"</p>");
    } else {
        chat.insertAdjacentHTML('beforeend',
            "<div class='d-flex flex-column justify-content-start align-items-start'>" +
            "<label class='label-message'>"+message.user.nom + " - "+ message.date +"</label>" +
            "<p class='message message-lui text-wrap'>"+message.contenu+"</p>");
    }
}

function sendMessage() {
    var messageText = $('#message').val();
    var userid = getCookie("id")
    var username = getCookie("username")
    var targetid = getCookie("targetid")
    const message = {'senderusername':username, 'senderid': userid, 'targetid': targetid, 'message':messageText, 'date':'now'};
    showMessage(message, true);
    stompClient.send("/app/application", {}, JSON.stringify(message));

}

function changeUser(test) {
    window.location.href = '/message/' + test; //one level up
}

function getCookie(name) {
    const cookieArray = document.cookie.split(';');

    for (let i = 0; i < cookieArray.length; i++) {
        const cookie = cookieArray[i].trim();
        if (cookie.startsWith(`${name}=`)) {
            return cookie.substring(name.length + 1);
        }
    }

    return null;
}

window.onload = connect;
