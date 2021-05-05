import 'package:enterativeflutter/changenotifiers/user_notifier.dart';
import 'package:enterativeflutter/helper/value_formatter.dart';
import 'package:enterativeflutter/model/card.dart';
import 'package:enterativeflutter/model/user.dart';
import 'package:enterativeflutter/pages/callback_page.dart';
import 'package:enterativeflutter/webservice/enterative_services.dart';
import 'package:enterativeflutter/widgets/appbar.dart';
import 'package:enterativeflutter/widgets/base64_image.dart';
import 'package:enterativeflutter/widgets/drawer.dart';
import 'package:enterativeflutter/widgets/widget_factory.dart';
import 'package:flushbar/flushbar_helper.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class BarcodeConfirmPage extends StatefulWidget {
  final String barcode;

  const BarcodeConfirmPage({Key key, this.barcode}) : super(key: key);

  @override
  _BarcodeConfirmPageState createState() => _BarcodeConfirmPageState();
}

class _BarcodeConfirmPageState extends State<BarcodeConfirmPage> {
  User user;
  GiftCard card;

  @override
  void didChangeDependencies() {
    this.user = Provider.of<UserNotifier>(context);
    super.didChangeDependencies();
  }

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
              left: MediaQuery.of(context).size.width / 2 - 55,
              bottom: 0,
              child: Row(
                children: <Widget>[
                  Container(
                    margin: EdgeInsets.only(right: 10),
                    child: WidgetFactory.createFloatingActionButton(
                      context,
                      tooltip: "Confirmar cartão",
                      child: Icon(Icons.check),
                      onPressed: _confirmCard,
                      heroTag: "confirmcard",
                    ),
                  ),
                  WidgetFactory.createFloatingActionButton(
                    context,
                    tooltip: "Cancelar",
                    child: Icon(Icons.cancel, color: Colors.red),
                    onPressed: _cancelCard,
                    heroTag: "cancelcard",
                  ),
                ],
              ),
            ),
          ],
        ),
        body: FutureBuilder(
          future:
              EnterativeServices.instance.card(context, user, widget.barcode),
          builder: (context, snapshot) {
            if (snapshot.connectionState != ConnectionState.done) {
              return WidgetFactory.createLoading();
            }

            card = snapshot.data;

            return Padding(
              padding: EdgeInsets.symmetric(
                horizontal: 20.0,
                vertical: 20.0,
              ),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  Card(
                    child: Padding(
                      padding: EdgeInsets.only(left: 15, right: 15, bottom: 10),
                      child: Column(
                        children: <Widget>[
                          Base64Image(
                            source: card.produtoImagem,
                            width: 160,
                            height: 200,
                          ),
                          _label("Código Digitado"),
                          _value(card.barcode),
                          _label("EAN"),
                          _value(card.ean),
                          _label("Número Cartão"),
                          _value(card.cardNo),
                          _label("Produto"),
                          _value(card.produto),
                          _label("Valor"),
                          _value("R\$ ${ValueFormatter.currency(card.valor)}"),
                          Divider(),
                          _label("Saldo"),
                          _value(
                              "R\$ ${ValueFormatter.currency(card.shopBalance)}",
                              color: card.valor > card.shopBalance
                                  ? Colors.red
                                  : Colors.green),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            );
          },
        ),
      ),
    );
  }

  Widget _label(String label) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.start,
      children: <Widget>[
        WidgetFactory.createStaticText(
          label,
          fontSize: 20,
          fontWeight: FontWeight.bold,
        ),
      ],
    );
  }

  Widget _value(String value, {Color color}) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.start,
      children: <Widget>[
        WidgetFactory.createStaticText(
          value,
          fontSize: 16,
          color: color,
        ),
      ],
    );
  }

  void _confirmCard() async {
    int orderid =
        await EnterativeServices.instance.activateCard(context, user, card);

    if (orderid == null || orderid <= 0) {
      FlushbarHelper.createError(
        message:
            "Não foi possível concluir seu carrinho.\nTente novamente mais tarde!",
      ).show(context);
    } else {
      Navigator.of(context).pushAndRemoveUntil(
          MaterialPageRoute(builder: (_) => CallbackPage(orderID: orderid)),
          (_) => false);
    }
  }

  void _cancelCard() {
    Navigator.of(context).pop();
  }
}
