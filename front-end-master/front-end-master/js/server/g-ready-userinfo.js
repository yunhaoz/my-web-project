// general component: on document ready, load userid and email
var userInfo;
$(document).ready(function() {
    $.ajax({
        method: "GET",
        url: "/api/userinfo",
        async: false,
        success: function(data, textStatus, jqXHR) {
            userInfo = data;
        },
        fail: function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR.status);
        }
    });
});