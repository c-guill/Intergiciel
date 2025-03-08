const chat = document.getElementById('chat');

chat.scrollTo(0, chat.scrollHeight);

var socket = new SockJS('/ws');
var stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    // console.log('Connecté: ' + frame);
    stompClient.subscribe('/all/messages', function (result) {
        showMessage(result.body, false);
    });
});

function showMessage(message, selfMessage) {
    // console.log(message);
    if (selfMessage) {
        chat.insertAdjacentHTML('beforeend',
            "<div class='d-flex flex-column justify-content-end align-items-end'>" +
            "<label class='label-message'>"+message.sender.username + " - "+ message.date +"</label>" +
            "<p class='message message-moi'>"+message.message+"</p>");
    } else {
        // console.log(message.sender);
        chat.insertAdjacentHTML('beforeend',
            "<div class='d-flex flex-column justify-content-start align-items-start'>" +
            "<label class='label-message'>"+"jsp" + " - "+ "date" +"</label>" +
            "<p class='message message-lui text-wrap'>"+"message.message"+"</p>");
        // chat.insertAdjacentHTML('beforeend',
        //     "<div class='d-flex flex-column justify-content-start align-items-start'>" +
        //     "<label class='label-message'>"+message.sender.username + " - "+ message.date +"</label>" +
        //     "<p class='message message-lui text-wrap'>"+message.message+"</p>");
    }
}

function sendMessage() {
    var message = $('#message').val();

    $.ajax({
        type: "POST",
        url: "/sendmessage",
        data: {message: message},
        success: function (response) {
            showMessage(response, true);

        },
        error: function() {
            alert("Error with the server, please contact an administrator.");
        }
    });
    // <div className=" d-flex flex-column justify-content-end align-items-end"><label
    //     th:text="${message.sender.username} + ' - ' + ${message.date}" className="label-message"></label><p
    //     th:text="${message.message}" className="message message-moi"></p></div>

}

function changeUser(test) {
    window.location.href = '/message/' + test; //one level up
}
