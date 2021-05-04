function saveMobile(url) {
    var customerMobile = $("#txtCustomerMobile").val();
    var idpedido = $("#txtPedidoID").val();

    if (!customerMobile || !idpedido) {
        window.location.href = url;
        return;
    }

    $.get("/enterative/user/saleorder/customerMobile/" + idpedido + "/" + customerMobile, function (data) {
        if (data === "OK") {
            window.location.href = url;
        } else {
            alert("Ocorreu um erro ao atualizar o celular do cliente. Favor tentar novamente!");
        }
    });
}