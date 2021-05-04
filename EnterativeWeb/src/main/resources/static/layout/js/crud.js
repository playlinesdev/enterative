function toggleAddLine() {
    var amount = $("#tNewLine_amount");
    var name = $("#tNewLine_name");

    amount.val("0.00");
    name.val("");

    if ($("#lineNewItem").is(":visible")) {
        $("#lineNewItem").hide();
    } else {
        $("#lineNewItem").show();
    }
}

function performActionAndDisable(element){
    element.setAttribute('style','pointer-events: none;');
}

function removeLine(link) {
    var id = link.id.replace('aRemoveLine_', '');
    $('#hname_' + id).val("REMOVE");
    document.getElementById("crud_form").submit();
}

function resetValue(target, source, idreplace) {
    if ($('#' + source.id).val()) {
        var id = source.id.replace(idreplace, '');
        $('#' + target + id).val(undefined);
    }
}

function appendAndSubmitFormAction(formID, url) {
    var form = $('#' + formID);
    var oldAction = form.attr('action');
    form.attr('action', oldAction + url);
    form.submit();
}

function appendAndSubmitFormActionWithID(formID, url, obj, idreplace) {
    var form = $('#' + formID);
    var oldAction = form.attr('action');
    var id = obj.id.replace(idreplace, '');
    if (id === undefined || id === "") {
        id = "0";
    } 
    form.attr('action', oldAction + url + id);
    form.submit();
}

$(document).ready(function () {
    $(".enterative-inputmask__date").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: "dd/mm/yyyy"
    }).inputmask('dd/mm/yyyy');

    $(".enterative-inputmask__shopcode").inputmask({
        'mask': '99999',
        'removeMaskOnSubmit': true
    });

    $(".enterative-inputmask__code4").inputmask({
        'mask': '9999',
        'removeMaskOnSubmit': true
    });
    
    $(".enterative-inputmask__code2").inputmask({
        'mask': '99',
        'removeMaskOnSubmit': true
    });

    $(".enterative-inputmask__cnpj").inputmask({
        'mask': '99.999.999/9999-99',
        'removeMaskOnSubmit': true
    });

    $(".enterative-inputmask__cpf").inputmask({
        'mask': '999.999.999-99',
        'removeMaskOnSubmit': true
    });

    $(".enterative-inputmask__cep").inputmask({
        'mask': '99999-999',
        'removeMaskOnSubmit': true
    });

    $(".enterative-inputmask__phone").inputmask({
        'mask': '(99) [9]9999-9999',
        'removeMaskOnSubmit': true
    });

    $(".enterative-inputmask__ean").inputmask({
        'mask': '999999999999-9',
        'removeMaskOnSubmit': true
    });

    $(".enterative-inputmask__terminal").inputmask({
        'mask': '999',
        'removeMaskOnSubmit': true
    });

    $(".enterative-inputmask__quantity").inputmask({
        'alias': 'numeric',
        'digits': 0,
        'unmaskAsNumber': true,
        'autoGroup': true,
        'radixPoint': ',',
        'groupSeparator': '.',
        'removeMaskOnSubmit': true,
        'min': 0
    });

    $(".enterative-inputmask__decimal").inputmask({
        'alias': 'numeric',
        'unmaskAsNumber': true,
        'autoGroup': true,
        'radixPoint': ',',
        'groupSeparator': '.',
        'removeMaskOnSubmit': true,
        'min': 0,
        'prefix': 'R$ '
    });

    $(".enterative-inputmask__percentage").inputmask({
        'alias': 'numeric',
        'unmaskAsNumber': true,
        'autoGroup': true,
        'radixPoint': ',',
        'groupSeparator': '.',
        'removeMaskOnSubmit': true,
        'min': 0,
        'max': 100,
        'suffix': ' %'
    });

    $(".enterative-inputmask__login").inputmask({
        'mask': '*{1,255}',
        'greedy': false,
        definitions: {
            '*': {
                validator: '[0-9A-Za-z]',
                casing: 'lower'
            }
        }
    });

    $(".enterative-inputmask__uf").inputmask({
        'mask': 'aa',
        definitions: {
            'a': {
                validator: '[A-Za-z]',
                casing: 'upper'
            }
        }
    });

    $(".enterative-layout-crudform__oneclick").click(function () {
        $(this).attr("disabled", true);
        $(this).closest('form').submit();
    });
});