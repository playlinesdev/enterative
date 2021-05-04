function retrieveObjects() {
    var comboType = $("#type");
    var typeValue = comboType.val();

    var loader = $("#loader");
    var combo = $("#objectid");

    combo.hide();
    loader.show();

    var url = "/enterative/admin/fileupload/objects/" + typeValue;

    $.get(url, function (data) {
        if (data) {
            var options = [];
            for (var index in data) {
                var d = data[index];
                options.push(objectOption(d, typeValue));
            }
            combo.html(options.join(""));
        }
    }).always(function () {
        loader.hide();
        combo.show();
    });
}

function objectOption(d, type) {
    var opt = "<option value='" + d.id + "'>";
    if (type === "PRODUCT_IMAGE") {
        opt += '[' + d.id + '] ' + d.displayName;
    }
    opt += "</option>";
    return opt;
}