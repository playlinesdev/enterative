function loadFromStorage() {
    var activeMenu = undefined;
    if (typeof (Storage) !== "undefined") {
        activeMenu = sessionStorage.activeMenu;
    } else {
        console.log("Navegador não possui suporte para armazenamento de sessão!");
    }
    return activeMenu;
}

function saveToStorage(menu) {
    if (typeof (Storage) !== "undefined") {
        sessionStorage.activeMenu = menu;
    } else {
        console.log("Navegador não possui suporte para armazenamento de sessão!");
    }
}

function toggleSubMenuByClass(menu) {
    var submenu = menu + "_submenu";
    var icon = "#" + menu + "_icon";

    if ($(icon).html() === "keyboard_arrow_right") {
        $(icon).html("keyboard_arrow_down");
        var els = document.getElementsByClassName(submenu);
        for (var index in els) {
            els[index].style = "display: flex";
        }
        if (menu.indexOf("main") !== -1) {
            var els = $("[class*='" + menu.replace("main", "sub") + "_']");
            for (var index in els) {
                els[index].style = "display: flex";
            }
        }
    } else {
        $(icon).html("keyboard_arrow_right");
        var els = document.getElementsByClassName(submenu);
        for (var index in els) {
            els[index].style = "display: none";
        }
        if (menu.indexOf("main") !== -1) {
            var els = $("[class*='" + menu.replace("main", "sub") + "_']");
            for (var index in els) {
                els[index].style = "display: none";
            }
        }
    }
}

function toggleSubMenu(menu, canSave, canAlterStyle) {
    if (menu === "undefined" || menu === undefined)
        return;

    var submenu = menu + "_submenu";
    var icon = menu + "_icon";

    if (!document.getElementById(icon) || !document.getElementById(submenu) || !document.getElementById(menu)) {
        return;
    }

    if (document.getElementById(icon).innerHTML === "keyboard_arrow_right") {
        document.getElementById(icon).innerHTML = "keyboard_arrow_down";
        document.getElementById(submenu).style = "display: flex";
        if (canAlterStyle === "undefined" || canAlterStyle === undefined || canAlterStyle === true) {
            $("#" + menu).parent().attr("style", "border-top: var(--default-border-color) 1px solid; border-bottom: var(--default-border-color) 1px solid;");
        }
        if (canSave === "undefined" || canSave === undefined || canSave === true) {
            saveToStorage(menu);
        }
    } else {
        document.getElementById(icon).innerHTML = "keyboard_arrow_right";
        document.getElementById(submenu).style = "display: none";
        if (canAlterStyle === "undefined" || canAlterStyle === undefined || canAlterStyle === true) {
            $("#" + menu).parent().attr("style", "");
        }
        if (canSave === "undefined" || canSave === undefined || canSave === true) {
            saveToStorage(undefined);
        }
    }
}

function onLocaleFocus(locale) {
    locale.setSelectionRange(0, locale.value.length);
}

var canRedirect = false;

function onLocaleChange(locale) {
    if (canRedirect) {
        var value = $(locale).val();
        if (value === 'br') {
            value = 'pt_br';
        } else if (value === 'us') {
            value = 'en_us';
        }
        window.location.href = '/enterative/changelang/' + value;
    }
}

$(function () {
    var m = loadFromStorage();
    if (m)
        toggleSubMenu(m);
});