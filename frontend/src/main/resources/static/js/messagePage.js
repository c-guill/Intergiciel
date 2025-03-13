const chat = document.getElementById('chat');

chat.scrollTo(0, chat.scrollHeight);

let stompClient = null;

var userid = getCookie("id")
var targetid = getCookie("targetid")

// let notificationBadge = null; // Définir notificationBadge ici
// Fonction qui affiche la notification
let messageNotification = null;
const notifList = []
function showNotification(id) {
    if (!notifList.includes(id)) {
        // Si le badge n'existe pas encore, crée-le
        notificationBadge = document.createElement('div');
        // notificationBadge.id = 'notification-badge';
        notificationBadge.className = 'notification-badge';
        notificationBadge.innerHTML = '🔔'; // Icône de notification
        messageNotification = document.getElementById('notification-'+id);
        if (messageNotification != null) {
            messageNotification.append(notificationBadge)
            notifList.push(id)
            notificationBadge.style.display = 'block';
        }
    }

    // On le rend visible

}

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connecté: ' + frame);

        stompClient.subscribe('/topic/message', function(message) {
            try {
                const jsonObject = JSON.parse(message.body);
                if (parseInt(targetid) === jsonObject.user.idUser) {
                    showMessage(jsonObject, false);
                } else {
                    showNotification(jsonObject.user.idUser)
                }
            }catch (error) {
                console.error("Parsing error:", error);
            }
        });
        stompClient.subscribe('/topic/broadcast', function(message) {
            try {
                const jsonObject = JSON.parse(message.body);
                console.log(jsonObject);
                console.log(parseInt(userid))
                console.log(parseInt(userid) === jsonObject.user.idUser)
                if (parseInt(userid) === jsonObject.user.idUser) return;
                if (parseInt(targetid) === -1) {
                    showMessage(jsonObject, false);
                } else {
                    showNotification('general')
                }
            }catch (error) {
                console.error("Parsing error:", error);
            }
        });
    });
}


function showMessage(message, selfMessage) {
    if (selfMessage) {
        chat.insertAdjacentHTML('beforeend',
            "<div class='d-flex flex-column justify-content-end align-items-end'>" +
            "<label class='label-message'>"+message.sender.username + " - "+ message.date +"</label>" +
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
    $.ajax({
        type: "POST",
        url: "/sendmessage",
        data: {message: messageText},
        success: function (response) {
            showMessage(response, true);

        },
        error: function() {
            alert("Error with the server, please contact an administrator.");
        }
    });

}

function changeUser(idUser) {
    localStorage.setItem("selectedUserId", idUser); // Sauvegarde l'ID sélectionné
    window.location.href = '/message/' + idUser; // Recharge la page avec l'ID sélectionné
    let badge = document.getElementById(`badge-${idUser}`);
    if (badge) {
        badge.style.display = "none";
    }

}
// Appliquer la classe au chargement de la page
document.addEventListener("DOMContentLoaded", function() {
    const selectedUserId = localStorage.getItem("selectedUserId");
    if (selectedUserId) {
        document.querySelectorAll(".contact-information").forEach(el => {
            el.classList.remove("selected-contact"); // Retire la sélection des autres
        });

        const selectedContact = document.querySelector(`[onclick="changeUser('${selectedUserId}')"]`);
        if (selectedContact) {
            selectedContact.classList.add("selected-contact");
        }
    }
});

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
