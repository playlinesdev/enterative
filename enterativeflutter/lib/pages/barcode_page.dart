import 'package:barcode_scan/barcode_scan.dart';
import 'package:enterativeflutter/pages/barcode_confirm_page.dart';
import 'package:enterativeflutter/widgets/appbar.dart';
import 'package:enterativeflutter/widgets/drawer.dart';
import 'package:enterativeflutter/widgets/widget_factory.dart';
import 'package:flushbar/flushbar_helper.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class BarcodePage extends StatefulWidget {
  @override
  _BarcodePageState createState() => _BarcodePageState();
}

class _BarcodePageState extends State<BarcodePage> {
  final TextEditingController _barcodeController =
      TextEditingController(text: "");

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: EnterativeAppbar.build(context),
        drawer: EnterativeDrawer(),
        floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
        floatingActionButton: Stack(
          children: <Widget>[
            Positioned(
              left: MediaQuery.of(context).size.width / 2 - (_barcodeController.text.isNotEmpty ? 55 : 25),
              bottom: 0,
              child: Row(
                children: <Widget>[
                  Container(
                    margin: _barcodeController.text.isNotEmpty ? EdgeInsets.only(right: 10) : EdgeInsets.zero,
                    child: WidgetFactory.createFloatingActionButton(
                      context,
                      tooltip: "Ler código de barras",
                      child: Icon(Icons.camera_alt),
                      onPressed: _doBarcodeScan,
                    ),
                  ),
                  _barcodeController.text.isNotEmpty
                      ? WidgetFactory.createFloatingActionButton(
                          context,
                          tooltip: "Ativar cartão",
                          child: Icon(Icons.check),
                          onPressed: _activateCard,
                        )
                      : Container(),
                ],
              ),
            ),
          ],
        ),
        body: ListView(
          children: <Widget>[
            Center(
              child: Image(
                image: AssetImage("assets/cartao_fisico.png"),
              ),
            ),
            WidgetFactory.createTextField("Código de Barras",
                enabled: false, controller: this._barcodeController),
          ],
        ),
      ),
    );
  }

  void _activateCard() async {
    Navigator.of(context).push(MaterialPageRoute(builder: (_) => BarcodeConfirmPage(barcode: _barcodeController.text)));
  }

  void _doBarcodeScan() async {
//    setState(() {
//      this._barcodeController.text = "50516440106901234567890001235540";
//    });
    try {
      String barcode = await BarcodeScanner.scan();
      setState(() => this._barcodeController.text = barcode);
    } on PlatformException catch (e) {
      if (e.code == BarcodeScanner.CameraAccessDenied) {
        FlushbarHelper.createError(
                message: "Aplicativo não tem permissão de acessar a camera!")
            .show(context);
      } else {
        FlushbarHelper.createError(message: "Erro desconhecido: $e")
            .show(context);
      }
    } on FormatException {
      FlushbarHelper.createError(message: "Leitura foi cancelada!")
          .show(context);
    } catch (e) {
      FlushbarHelper.createError(message: "Erro desconhecido: $e")
          .show(context);
    }
  }
}
