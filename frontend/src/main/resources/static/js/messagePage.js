const chat = document.getElementById('chat');

chat.scrollTo(0, chat.scrollHeight);


function sendMessage() {
    var message = $('#message').val();

    $.ajax({
        type: "POST",
        url: "/sendmessage",
        data: {message: message},
        success: function (response) {
            chat.insertAdjacentHTML('beforeend',
                "<div class='d-flex flex-column justify-content-end align-items-end'>" +
                "<label class='label-message'>"+response.sender.username + " - "+ response.date +"</label>" +
                "<p class='message message-moi'>"+response.message+"</p>");

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
