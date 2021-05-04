function toggleReport(detail) {
    var id = detail.split("_")[1];
    var link = "aDetails_" + id;
    var icon = $("#iDetails_" + id);
    var tr = $("#trDetails_" + id);
    var trActivation = $("#trActivation_" + id);
    var sActivation = $("#sActivation_" + id);

    if (icon.html() === "expand_more") {
        icon.html("expand_less");
        tr.show();
        trActivation.show();
        setDefaultTransaction(detail);
    } else {
        icon.html("expand_more");
        tr.hide();
        trActivation.hide();
        sActivation.html("");
    }
}

function toggleActivation(detail) {
    var id = detail.split("_")[1];
    var link = "aActivationDetails_" + id;
    var icon = $("#iActivationDetails_" + id);
    var tr = $("#trActivationDetails_" + id);
    var sActivation = $("#sActivation_" + id);

    if (icon.html() === "expand_more") {
        if (sActivation.val()) {
            icon.html("expand_less");
            tr.show();
            fillActivationLine(sActivation.val(), detail);
        }
    } else {
        icon.html("expand_more");
        tr.hide();
        tr.html("");
    }
}

function activationChange(detail) {
    var id = detail.split("_")[1];
    var sActivation = $("#sActivation_" + id);
    var icon = $("#iActivationDetails_" + id);
    var hTransactionId = $("#hTransactionId_" + id);

    if (icon.html() === "expand_less") {
        fillActivationLine(sActivation.val(), detail);
    }

    var save = $("#aSaveDetails_" + id);
    if (hTransactionId.val() !== sActivation.val()) {
        save.show();
    } else {
        save.hide();
    }
}

function clearDetail(detail) {
    var id = detail.split("_")[1];

    $.get("/enterative/admin/sdfvalidation/clearTransaction/" + id, function (data) {
        if (data === "OK") {
            var hTransactionId = $("#hTransactionId_" + id);
            hTransactionId.val(undefined);

            var transactionIcon = $("#iActivationDetails_" + id);
            if (transactionIcon.html() === "expand_less") {
                toggleActivation(detail);
            }

            var detailIcon = $("#iDetails_" + id);
            if (detailIcon.html() === "expand_less") {
                toggleReport(detail);
            }

            var tdStatus = $("#tdStatusDetails_" + id);
            var html = "<i id='iNotFound_" + id + "' class='material-icons mdl-color-text--red'>error</i>";
            html += "<span id='sNotFound_" + id + "' class='mdl-tooltip mdl-tooltip--large' for='iNotFound_" + id + "'>Não encontrada</span>"
            tdStatus.html(html);

            componentHandler.upgradeElement(document.getElementById("sNotFound_" + id));
        } else {
            var error = $("#transactionError_" + id);
            var html = "<i class='material-icons mdl-color-text--red'>error</i> ";
            html += data;
            html += "<a href='#' onclick='destroyError(" + id + ")'><i class='material-icons'>close</i></a>";
            error.html(html);
            error.show();
        }
    });
}

function saveDetail(detail) {
    var id = detail.split("_")[1];
    var sTransaction = $("#sActivation_" + id);

    $.get("/enterative/admin/sdfvalidation/saveTransaction/" + id + "/" + sTransaction.val(), function (data) {
        if (data === "OK") {
            var hTransactionId = $("#hTransactionId_" + id);
            hTransactionId.val(sTransaction.val());

            var transactionIcon = $("#iActivationDetails_" + id);
            if (transactionIcon.html() === "expand_less") {
                toggleActivation(detail);
            }

            var detailIcon = $("#iDetails_" + id);
            if (detailIcon.html() === "expand_less") {
                toggleReport(detail);
            }

            var tdStatus = $("#tdStatusDetails_" + id);
            var html = "<i id='iFoundWrong_" + id + "' class='material-icons mdl-color-text--amber'>done</i>";
            html += "<span id='sFoundWrong_" + id + "' class='mdl-tooltip mdl-tooltip--large' for='iFoundWrong_" + id + "'>Encontrada<br/>manualmente</span>"
            tdStatus.html(html);

            componentHandler.upgradeElement(document.getElementById("sFoundWrong_" + id));
        } else {
            var error = $("#transactionError_" + id);
            var html = "<i class='material-icons mdl-color-text--red'>error</i> ";
            html += data;
            html += "<a href='#' onclick='destroyError(" + id + ")'><i class='material-icons'>close</i></a>";
            error.html(html);
            error.show();
        }
    });
}

function destroyError(id) {
    var error = $("#transactionError_" + id);
    error.html("");
    error.hide();
}

function fillActivationLine(activationID, detail) {
    var id = detail.split("_")[1];
    var tr = $("#trActivationDetails_" + id);
    tr.html("<td colspan='14'><div id='activationLineLoader' class='mdl-spinner mdl-js-spinner is-active enterative-layout__center--block'></div></td>");
    componentHandler.upgradeElement(document.getElementById("activationLineLoader"));

    $.get("/enterative/admin/sdfvalidation/transactions/detail/" + id + "/" + activationID, function (data) {
        if (data) {
            var html = "<td colspan='14'>";
            html += "<div class='mdl-grid'>";

            html += __title("Date");
            html += __data(data.localTransactionDate, data.discrepancies.indexOf("localTransactionDate"));

            html += __title("Time");
            html += __data(data.localTransactionTime, data.discrepancies.indexOf("localTransactionTime"));

            html += __title("System Trace Audit");
            html += __data(data.systemTraceAuditNumber, data.discrepancies.indexOf("systemTraceAuditNumber"));

            html += __title("Gift Card");
            html += __data(data.primaryAccountNumber, data.discrepancies.indexOf("primaryAccountNumber"));

            html += __title("Amount");
            var amount = ("R$ " + parseFloat(data.transactionAmount / 100).toFixed(2)).replace(".", ",");
            html += __data(amount, data.discrepancies.indexOf("transactionAmount"));
            
            html += __title("Activation Account Number");
            html += __data(data.activationAccountNumber, data.discrepancies.indexOf("activationAccountNumber"));

            html += "</div>";
            html += "</td>";

            tr.html(html);
        }
    });
}

function __title(title) {
    return "<div class='mdl-cell mdl-cell--2-col-desktop enterative-cell__bold'>" + title + "</div>";
}

function __data(data, discrepancyIndex) {
    var html = "<div class='mdl-cell mdl-cell--2-col-desktop ";
    if (discrepancyIndex === -1) {
        html += "mdl-color-text--green'>";
    } else {
        html += "mdl-color-text--red'>";
    }
    html += data + "</div>";

    return html;
}

function setDefaultTransaction(detail) {
    var id = detail.split("_")[1];

    var hTransactionId = $("#hTransactionId_" + id);
    var clear = $("#aClearDetails_" + id);
    if (!hTransactionId.val()) {
        clear.hide();
        return;
    } else {
        clear.show();
    }

    var loader = $("#loader_" + id);
    var combo = $("#sActivation_" + id);
    var transactionId = hTransactionId.val();

    loader.show();
    combo.hide();

    $.get("/enterative/admin/sdfvalidation/transactions/detail/" + id + "/" + transactionId, function (data) {
        if (data) {
            combo.html(assembleTransactionOption(data));
            combo.val(data.id);
        }
    }).always(function () {
        loader.hide();
        combo.show();
    });
}

function assembleTransactionOption(d) {
    var opt = "<option value='" + d.id + "'>";
    var amount = ("R$ " + (parseFloat(d.transactionAmount) / 100).toFixed(2)).replace(".", ",");
    opt += [d.localTransactionDate, d.localTransactionTime, d.systemTraceAuditNumber, d.primaryAccountNumber, amount].join(" | ");
    opt += "</option>";
    return opt;
}

function retrieveActivations(detail, type) {
    var id = detail.split("_")[1];
    var combo = $("#sActivation_" + id);
    var loader = $("#loader_" + id);

    var url = "/enterative/admin/sdfvalidation/transactions/";

    if (type === 0) {
        // Day
        url += "day/" + id;
    } else if (type === 1) {
        // Gift
        url += "gift/" + id;
    } else if (type === 2) {
        // Product
        url += "product/" + id;
    } else {
        return;
    }

    var icon = $("#iActivationDetails_" + id);
    if (icon.html() === "expand_less") {
        toggleActivation(detail);
    }

    loader.show();
    combo.hide();

    $.get(url, function (data) {
        if (data) {
            var options = [];
            for (var index in data) {
                var d = data[index];
                options.push(assembleTransactionOption(d));
            }
            combo.html(options.join(""));
            if (options.length > 0) {
                combo.attr('selectedIndex', 0);
                activationChange(detail);
            }
        }
    }).always(function () {
        loader.hide();
        combo.show();
    });
}