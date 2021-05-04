var timer = -1;
var timerMax = 3;
var timerFunction = undefined;

function hideScan() {
    $('#cartaoForm').show();
    $('#livestream_scanner').hide();
    $('#interative').hide();
}

function incrementTimer() {
    timer++;
    if (timer === timerMax) {
        timerFunction = undefined;
        $('#scanner_error').html('<div class="alert alert-info"><strong><i class="material-icons">update</i></strong>Carregando leitor de código de barras!</div>');
        window.location.href = "/enterative/ativacao/scanner";
    } else {
        $('#scanner_error').html('<div class="alert alert-info"><strong><i class="material-icons">timer</i></strong>Carregando leitor de código de barras! Aguarde <strong>' + (timerMax - timer) + '</strong> segundos...</div>');
        timerFunction = window.setTimeout(incrementTimer, 1000);
    }
}

function showScan() {
    $('#cartaoForm').hide();
    $('#livestream_loader').show();

    $('#livestream_scanner').show();
    $('#interactive').hide();

    timerFunction = window.setTimeout(incrementTimer, 1000);
}

//var liveStreamConfig = {
//    inputStream: {
//        type: "LiveStream",
//        constraints: {
//            width: {min: 640},
//            height: {min: 480},
//            aspectRadio: {min: 1, max: 100},
//            facingMode: "environment" // or "user" for the front camera
//        },
//        area: {
//            top: "40%",
//            bottom: "40%"
//        }
//    },
//    locator: {
//        patchSize: "medium",
//        halfSample: true
//    },
//    numOfWorkers: 4,
//    decoder: {
//        readers: ["code_128_reader"],
//        multiple: false
//    },
//    locate: false,
//    frequency: 10
//};

//    Quagga.init(
//            liveStreamConfig,
//            function (err) {
//                if (err) {
//                    var name = err.name;
//                    var msg = err.message;
//                    if (name === "NotFoundError") {
//                        name = "Erro";
//                        msg = "Camera não encontrada! Favor verificar permissões e/ou configurações de hardware.";
//                    }
//                    $('#scanner_error').html('<div class="alert alert-danger"><strong><i class="material-icons">warning</i> ' + name + '</strong>: ' + msg + '</div>');
//                    Quagga.stop();
//                    return;
//                }
//                Quagga.start();                
//                canContinue = false;
//            }
//    );


//function drawViewport(ctx, box, canvas) {
//    var minY = 1000, maxY = 0;
//    var maxX = 0;
//    for (var x = 1; x < box.length; x++) {
//        if (box[x][0] > maxX)
//            maxX = box[x][0];
//        if (box[x][1] > maxY)
//            maxY = box[x][1];
//        if (box[x][1] < minY)
//            minY = box[x][1];
//    }
//
//    ctx.globalAlpha = 0.5;
//    ctx.fillStyle = "#337ab7";
//    ctx.fillRect(0, 0, maxX, minY);
//    ctx.fillRect(0, maxY, maxX, parseInt(canvas.getAttribute("height")) - (maxY));
//
//    ctx.globalAlpha = 1;
//    ctx.fillStyle = "#000000";
//    var lineY = ((maxY - minY) / 2) + minY;
//    ctx.beginPath();
//    ctx.moveTo(0, lineY);
//    ctx.lineTo(maxX, lineY);
//    ctx.stroke();
//
//    if (!$('#interative').is(":visible")) {
//        $('#interative').show();
//        $('#livestream_loader').hide();
//    }
//}

// Make sure, QuaggaJS draws frames an lines around possible 
// barcodes on the live stream
//Quagga.onProcessed(function (result) {
//    var drawingCtx = Quagga.canvas.ctx.overlay,
//            drawingCanvas = Quagga.canvas.dom.overlay;
//
//    if (result) {
//        if (result.boxes) {
//            drawingCtx.clearRect(0, 0, parseInt(drawingCanvas.getAttribute("width")), parseInt(drawingCanvas.getAttribute("height")));
//            result.boxes.filter(function (box) {
//                return box !== result.box;
//            }).forEach(function (box) {
//                drawViewport(drawingCtx, box, drawingCanvas);
//                //Quagga.ImageDebug.drawPath(box, {x: 0, y: 1}, drawingCtx, {color: "#337ab7", lineWidth: 2});
//            });
//        }
//
////        if (result.box) {
////            Quagga.ImageDebug.drawPath(result.box, {x: 0, y: 1}, drawingCtx, {color: "#00F", lineWidth: 2});
////        }
//
//        if (result.codeResult && result.codeResult.code) {
//            Quagga.ImageDebug.drawPath(result.line, {x: 'x', y: 'y'}, drawingCtx, {color: 'red', lineWidth: 3});
//        }
//    }
//});

// Once a barcode had been read successfully, stop quagga and 
// close the modal after a second to let the user notice where 
// the barcode had actually been found.
//Quagga.onDetected(function (result) {
//    if (result.codeResult.code) {
//        $('#cardNo').val(result.codeResult.code);
//        Quagga.stop();
//        setTimeout(function () {
//            hideScan();
//        }, 1000);
//    }
//});