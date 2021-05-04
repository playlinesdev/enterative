function gotoPage(page, size) {
    var form = $('#domFormSearch');
    var oldAction = form.attr("action");

    form.attr("action", oldAction + "?page=" + page + "&size=" + size);
    form.submit();
}

function goToReport() {
    var form = $("#domFormSearch");
    var oldAction = form.attr("action");

    form.attr("action", oldAction + "/report");
    form.attr("target", "_blank");

    form.submit();

    form.attr("action", oldAction);
    form.attr("target", "_self");
}