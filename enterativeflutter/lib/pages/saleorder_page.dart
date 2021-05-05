import 'dart:io';

import 'package:enterativeflutter/changenotifiers/user_notifier.dart';
import 'package:enterativeflutter/helper/value_formatter.dart';
import 'package:enterativeflutter/model/sale_order.dart';
import 'package:enterativeflutter/model/user.dart';
import 'package:enterativeflutter/webservice/enterative_services.dart';
import 'package:enterativeflutter/widgets/appbar.dart';
import 'package:enterativeflutter/widgets/base64_image.dart';
import 'package:enterativeflutter/widgets/widget_factory.dart';
import 'package:flushbar/flushbar_helper.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';

class SaleOrderPage extends StatefulWidget {
  final int orderID;

  const SaleOrderPage({Key key, this.orderID}) : super(key: key);

  @override
  _SaleOrderPageState createState() => _SaleOrderPageState();
}

class _SaleOrderPageState extends State<SaleOrderPage> {
  User user;

  @override
  void didChangeDependencies() {
    this.user = Provider.of<UserNotifier>(context);
    super.didChangeDependencies();
  }

  Widget _buildRow(String label, String value) {
    return Padding(
      padding: EdgeInsets.only(top: 10.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          WidgetFactory.createStaticText(
            label,
            fontSize: 20,
            fontWeight: FontWeight.bold,
          ),
          WidgetFactory.createStaticText(value, fontSize: 16),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: EnterativeAppbar.build(context, showCart: false),
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
                      onPressed: _printReceipt,
                      tooltip: "Imprimir Recibo",
                      child: Icon(Icons.print),
                      heroTag: "receipt",
                    ),
                  ),
                  WidgetFactory.createFloatingActionButton(
                    context,
                    tooltip: "Cancelar",
                    onPressed: () => Navigator.pushNamedAndRemoveUntil(
                        context, "/", (_) => false),
                    child: Icon(
                      Icons.cancel,
                      color: Colors.red,
                    ),
                    heroTag: "cancel",
                  ),
                ],
              ),
            ),
          ],
        ),
        body: FutureBuilder(
          future: EnterativeServices.instance
              .saleOrder(context, user, widget.orderID),
          builder: (context, snapshot) {
            if (snapshot.connectionState != ConnectionState.done) {
              return WidgetFactory.createLoading();
            }

            SaleOrder order = snapshot.data;
            return Padding(
              padding: EdgeInsets.symmetric(
                horizontal: 20.0,
              ),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  Card(
                    child: Padding(
                      padding: EdgeInsets.only(left: 15, right: 15, bottom: 10),
                      child: Column(
                        children: <Widget>[
                          _buildRow("Data",
                              "${ValueFormatter.datetime(order.createdAt)}"),
                          _buildRow("Valor Total",
                              "${ValueFormatter.currency(order.amount)}"),
                          _buildRow("Situação", "${order.translatedStatus}"),
                          _buildRow("Tipo", "${order.translatedType}"),
                        ],
                      ),
                    ),
                  ),
                  Container(
                    margin: EdgeInsets.only(
                      top: 10,
                    ),
                    child: Divider(
                      thickness: 5,
                    ),
                  ),
                  Center(
                    child: WidgetFactory.createDialogTitle("Cartões"),
                  ),
                  Container(
                    margin: EdgeInsets.only(
                      bottom: 10,
                    ),
                    child: Divider(
                      thickness: 5,
                    ),
                  ),
                  ListView.builder(
                    shrinkWrap: true,
                    itemCount: order.lines.length,
                    itemBuilder: (context, index) {
                      return _createProductCard(order.lines[index]);
                    },
                  ),
                ],
              ),
            );
          },
        ),
      ),
    );
  }

  _printReceipt() async {
    if (Platform.isAndroid) {
      List<SaleOrderReceipt> wsReceipts = await EnterativeServices.instance
          .receipt(context, user, widget.orderID);
      String cliente = "", logista = "";

      for (var rawReceipt in wsReceipts) {
        String withoutPin = "Data: ${ValueFormatter.datetime(rawReceipt.date)}\nValor: R\$ ${ValueFormatter.currency(rawReceipt.amount)}\n"
            "Situação: ${rawReceipt.status}\nTipo: ${rawReceipt.type}\nProduto: ${rawReceipt.product}";

        logista += "$withoutPin\n\n";
        cliente += "$withoutPin\nPIN: ${rawReceipt.pin}\n\n";
      }

      const printer = const MethodChannel('br.com.enterative/printer');
      await printer.invokeMethod('print', {
        "BUFFERLOGISTA": logista,
        "BUFFERCLIENTE": cliente,
        "ATENDIMENTO":
            "Enterative\nRua Santa Catarina, 1163\nBelo Horizonte - MG\nAgradecemos a preferência!",
      });
      Navigator.of(context).pushNamedAndRemoveUntil("/", (_) => false);
    } else {
      FlushbarHelper.createError(message: "Somente disponível para Android!")
          .show(context);
    }
  }

  Widget _createProductCard(SaleOrderLine line) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(2.0),
        child: ListTile(
          leading: Base64Image(
            source: line.product.imagem,
          ),
          title: WidgetFactory.createStaticText(
            line.product.name,
            textAlign: TextAlign.center,
          ),
          subtitle: Column(
            children: <Widget>[
              WidgetFactory.createStaticText(
                  ValueFormatter.currency(line.amount)),
              WidgetFactory.createStaticText(line.status),
              Divider(),
              if (line.activationStatus != null)
                WidgetFactory.createStaticText(line.activationStatus),
              if (line.response != null)
                WidgetFactory.createStaticText(line.translatedResponse,
                    color: line.response.contains('00')
                        ? Colors.green
                        : Colors.red),
              if (line.responseAux != null)
                WidgetFactory.createStaticText(line.translatedResponseAux,
                    color: line.responseAux.contains('00')
                        ? Colors.green
                        : Colors.red),
            ],
          ),
        ),
      ),
    );
  }
}
