function submitData() {
    var username = $('#username').val();
    // var hiddenData = 'internalValue'; // Set your internal data here

    $.ajax({
        type: "POST",
        url: "/connection",
        data: { username: username},
        success: function(response) {
            // Handle the response here
            // $('#result').html(response);
            window.location.href = '/message'; //one level up
        }
    });
}