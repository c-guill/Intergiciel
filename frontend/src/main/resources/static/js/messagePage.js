const chat = document.getElementById('chat');

chat.scrollTo(0, chat.scrollHeight);

let stompClient = null;

function connect() {
    var userid = getCookie("id")
    if (userid == null || userid === "-1") return;
    const socket = new SockJS('http://localhost:8080/ws'); // Change the URL to your WebSocket endpoint
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/all/messages', function (response) {
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
        chat.scrollTo(0, chat.scrollHeight);
    } else {
        chat.insertAdjacentHTML('beforeend',
            "<div class='d-flex flex-column justify-content-start align-items-start'>" +
            "<label class='label-message'>"+message.user.nom + " - "+ formatDate(new Date(message.date)) +"</label>" +
            "<p class='message message-lui text-wrap'>"+message.contenu+"</p>");
        chat.scrollTo(0, chat.scrollHeight);
    }
}

function enter(element) {
    if (event.key === 'Enter') {
        sendMessage();
    }
}

function sendMessage() {
    const messageElement = document.getElementById('message-sender');
    const messageText = messageElement.value.trim();
    if (messageText === '') {
        return
    }
    messageElement.value = '';
    var userid = getCookie("id")
    if (userid == null || userid === "-1") return;
    var username = getCookie("username")
    var targetid = getCookie("targetid")
    const message = {'senderusername':username, 'senderid': userid, 'targetid': targetid, 'message':messageText, 'date':formatDate(new Date())};
    showMessage(message, true);
    stompClient.send("/app/application", {}, JSON.stringify(message));

}

function changeUser(idUser) {
    window.location.href = '/message/' + idUser;
}

function disconnect() {
    $.ajax({
        type: "DELETE",
        url: "/disconnect"});
    window.location.href = '/';
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

function formatDate(date) {
    const day = String(date.getUTCDate()).padStart(2, '0');
    const month = String(date.getUTCMonth() + 1).padStart(2, '0');
    const year = date.getUTCFullYear();

    const hours = String(date.getUTCHours()).padStart(2, '0');
    const minutes = String(date.getUTCMinutes()).padStart(2, '0');

    return `${day}/${month}/${year} ${hours}:${minutes}`;
}

window.onload = connect;
