var callbackTimer = undefined;

function startCallbackWaiting() {
    callbackTimer = window.setTimeout(checkOrder, 1000);
}

function checkOrder() {
    var idpedido = $("#id").val();
    if (!idpedido) {
        return;
    }

    $.get("/enterative/callback/check/" + idpedido, function (data) {
        if (data === "OK") {
            callbackTimer = undefined;
            window.location.href = "/enterative/ativacao/fisico/orderdone/" + idpedido;
        }
    }).always(function () {
        callbackTimer = window.setTimeout(checkOrder, 1000);
    });
}

$(function () {
    startCallbackWaiting();
});