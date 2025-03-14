function submitData() {
    var username = $('#username').val();

    $.ajax({
        type: "POST",
        url: "/connection",
        data: { username: username},
        success: function(response) {
            window.location.href = '/message';
        }
    });
}