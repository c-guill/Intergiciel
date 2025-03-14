const chat = document.getElementById('chat');
const contactdiv = document.getElementById('contact-list');

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
        let existingBadge = document.querySelector(`#notification-${id} .notification-badge`);
        if (!existingBadge) { // Vérifie si la notification est déjà affichée
            notificationBadge = document.createElement('div');
            notificationBadge.className = 'notification-badge';
            notificationBadge.innerHTML = '🔔';
            messageNotification = document.getElementById('notification-' + id);

            if (messageNotification != null) {
                messageNotification.append(notificationBadge);
                notifList.push(id);
                notificationBadge.style.display = 'block';
            }
        }
    }
}



    function connect() {
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {

            stompClient.subscribe('/topic/message', function (message) {
                try {
                    const jsonObject = JSON.parse(message.body);
                    if (parseInt(targetid) === jsonObject.user.idUser) {
                        showMessage(jsonObject, false);
                    } else {
                        showNotification(jsonObject.user.idUser)
                    }
                } catch (error) {
                    console.error("Parsing error:", error);
                }
            });
            stompClient.subscribe('/topic/broadcast', function (message) {
                try {
                    const jsonObject = JSON.parse(message.body);
                    if (parseInt(userid) === jsonObject.user.idUser) return;
                    if (parseInt(targetid) === -1) {
                        showMessage(jsonObject, false);
                    } else {
                        showNotification('general')
                    }
                } catch (error) {
                    console.error("Parsing error:", error);
                }
            });
            stompClient.subscribe('/topic/manageuser', function (message) {
                message = message.body;
                try {
                    if(message.startsWith("add")) {
                        const contact = message.split("$")[1]
                        addUser(JSON.parse(contact));
                    } else if (message.startsWith("remove")) {
                        const id = parseInt(message.split("$")[1])
                        removeUser(id)

                    }
                } catch (error) {
                }
            });
            stompClient.send("/app/addUser",{},userid);
        });
    }

    function addUser(contact) {
        cid = contact.id ?? '';
        if (cid == userid) return;
        cusername = contact.username ?? '';
        contactdiv.insertAdjacentHTML('beforeend',"<div class='contact-information' onclick='changeUser("+cid+")' id='contact-online-"+cid +"'>"+
        "<div class='d-flex justify-content-between align-items-center'>" +
        "<div class='user row'>" +
        "<p class='bold'>      "+cusername+"</p>" +
        "<div class='available'>" +
        "</div>" +
        "<span class='notification-badge' style='display: none;'></span>" +
        "</div>" +
        "<p class='contact-date' ></p>" +
        "</div>" +
        "<div id='notifications-'"+cid+" class='message-notification' style='display: flex'>" +
        "<p class='text-truncate' ></p>" +
        "</div>" +
        "</div>");
    }

    function removeUser(id) {
        const div = document.getElementById("contact-online-"+id);
        if(div !=null){
            div.remove();
            if (targetid == id) {
                window.location.href = '/message/' + 0;
            }
        }
    }


    function showMessage(message, selfMessage) {
        if (selfMessage) {
            chat.insertAdjacentHTML('beforeend',
                "<div class='d-flex flex-column justify-content-end align-items-end'>" +
                "<label class='label-message'>" + message.sender.username + " - " + message.date + "</label>" +
                "<p class='message message-moi'>" + message.message + "</p>");
            chat.scrollTo(0, chat.scrollHeight);
        } else {
            chat.insertAdjacentHTML('beforeend',
                "<div class='d-flex flex-column justify-content-start align-items-start'>" +
                "<label class='label-message'>" + message.user.nom + " - " + formatDate(new Date(message.date)) + "</label>" +
                "<p class='message message-lui text-wrap'>" + message.contenu + "</p>");
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
            error: function () {
                alert("Error with the server, please contact an administrator.");
            }
        });

    }

    function changeUser(idUser) {
        localStorage.setItem("selectedUserId", idUser); // Sauvegarde l'ID sélectionné
        window.location.href = '/message/' + idUser; // Recharge la page avec l'ID sélectionné


        let badge = document.querySelector(`#notification-${idUser} .notification-badge`);
        if (badge) {
            badge.remove();c
        }
        // Supprimer l'ID de la liste des notifications en mémoire
        notifList.splice(notifList.indexOf(idUser), 1);
        // Remove from localStorage when user clicks to read
        let unreadMessages = JSON.parse(localStorage.getItem("unreadMessages")) || [];
        unreadMessages = unreadMessages.filter(msgId => msgId !== idUser);
        localStorage.setItem("unreadMessages", JSON.stringify(unreadMessages))

    }

// Appliquer la classe au chargement de la page
    document.addEventListener("DOMContentLoaded", function () {
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

document.addEventListener("DOMContentLoaded", function () {
    const app = document.querySelector(".app");
    const toggleButton = document.querySelector("#toggle-contacts");

    toggleButton.addEventListener("click", function () {
        if (app.classList.contains("show-contacts")) {
            app.classList.remove("show-contacts");
            toggleButton.textContent = "☰"; // Icône menu
        } else {
            app.classList.add("show-contacts");
            toggleButton.textContent = "✖"; // Icône de fermeture
        }
    });
});


window.onload = connect;

