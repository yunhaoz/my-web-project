var logoutServer = function() {
    $.ajax({
        type: "POST",
        url: "/api/auth/logout",
        success: function(data, textStatus, jqXHR) {
            location.href = "/"
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR.status + " ERROR");
        }
    });
}