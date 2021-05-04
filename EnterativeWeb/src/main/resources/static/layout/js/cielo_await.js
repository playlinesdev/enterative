var paymentTimer = undefined;

function startPaymentWaiting() {
    paymentTimer = window.setTimeout(checkPayment, 2000);
}

function checkPayment() {
    location.reload(true);
}

$(function () {
    startPaymentWaiting();
});