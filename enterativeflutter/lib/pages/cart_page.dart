import 'package:enterativeflutter/changenotifiers/user_notifier.dart';
import 'package:enterativeflutter/helper/value_formatter.dart';
import 'package:enterativeflutter/model/shopping_cart.dart';
import 'package:enterativeflutter/model/user.dart';
import 'package:enterativeflutter/pages/callback_page.dart';
import 'package:enterativeflutter/webservice/enterative_services.dart';
import 'package:enterativeflutter/widgets/appbar.dart';
import 'package:enterativeflutter/widgets/base64_image.dart';
import 'package:enterativeflutter/widgets/drawer.dart';
import 'package:enterativeflutter/widgets/widget_factory.dart';
import 'package:flushbar/flushbar_helper.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class ShoppingCartPage extends StatefulWidget {
  @override
  _ShoppingCartPageState createState() => _ShoppingCartPageState();
}

class _ShoppingCartPageState extends State<ShoppingCartPage> {
  User _user;
  ShoppingCart _cart;
  bool _loading = true;
  bool _concludeLoading = false;

  @override
  void initState() {
    super.initState();
    this._cart = null;
  }

  @override
  Widget build(BuildContext context) {
    _user = Provider.of<UserNotifier>(context);

    return SafeArea(
      child: Scaffold(
        appBar: EnterativeAppbar.build(context),
        drawer: EnterativeDrawer(),
        floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
        floatingActionButton:
            _loading == true || _cart == null || _cart.amount == null || _cart.amount == 0
                ? null
                : WidgetFactory.createFloatingActionButton(
                    context,
                    onPressed: _concludeLoading ? () => {} : _concludeAction,
                    tooltip: "Concluir",
                    child: _concludeLoading
                        ? CircularProgressIndicator(
                            valueColor:
                                new AlwaysStoppedAnimation<Color>(Colors.blue),
                          )
                        : Icon(Icons.check),
                  ),
        body: FutureBuilder(
          future: _retrieveCart(),
          builder: (context, snapshot) {
            if (snapshot.connectionState != ConnectionState.done) {
              return WidgetFactory.createLoading();
            } else if (snapshot.data == null || snapshot.data.amount == null || snapshot.data.amount == 0) {
              return WidgetFactory.createNoDataFound();
            }

            _cart = snapshot.data;

            return Stack(
              children: <Widget>[
                Container(
                  child: ListView.builder(
                    scrollDirection: Axis.vertical,
                    key: PageStorageKey("cartlist"),
                    itemCount: _cart.lines.length,
                    itemBuilder: (context, index) {
                      ShoppingCartLine item = _cart.lines[index];
                      return this._createProductCard(item);
                    },
                  ),
                ),
                Positioned(
                  bottom: 0,
                  left: 0,
                  right: 0,
                  child: this._buildTotal(_cart.amount),
                ),
              ],
            );
          },
        ),
      ),
    );
  }

  _concludeAction() async {
    setState(() {
      this._concludeLoading = true;
    });

    int orderid =
        await EnterativeServices.instance.concludeCart(context, _user);

    setState(() {
      this._concludeLoading = false;
    });

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

  Future<ShoppingCart> _retrieveCart() async {
    if (_cart == null) {
      _cart = await EnterativeServices.instance.cart(context, _user);
    }
    setState(() {
      this._loading = false;
    });
    return _cart;
  }

  Widget _buildTotal(double total) {
    return Container(
      decoration: BoxDecoration(
        color: Theme.of(context).accentColor,
        border: Border(
          bottom: BorderSide(
            color: Colors.white,
          ),
        ),
      ),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: <Widget>[
            WidgetFactory.createDialogTitle(
              "Total",
              color: Colors.white,
            ),
            WidgetFactory.createDialogTitle(
              "R\$ ${ValueFormatter.currency(total)}",
              color: Colors.white,
            ),
          ],
        ),
      ),
    );
  }

  Widget _createProductCard(ShoppingCartLine line) {
    line.textController = TextEditingController();
    line.textController.text = line.quantity.toString();

    return Dismissible(
      key: Key(line.id.toString()),
      direction: DismissDirection.endToStart,
      onDismissed: (direction) async {
        line.textController.text = "0";
        await _saveCart();
        FlushbarHelper.createSuccess(
                message: "Produto [${line.product.name}] removido!")
            .show(context);
      },
      background: Container(color: Colors.red),
      child: Card(
        child: Padding(
          padding: const EdgeInsets.all(2.0),
          child: ListTile(
            leading: Base64Image(
              source: line.product.imagem,
            ),
            title: WidgetFactory.createStaticText(line.product.displayName),
            trailing: Container(
              alignment: Alignment.center,
              width: 50,
              child: WidgetFactory.createTextField(
                "",
                keyboardType: TextInputType.number,
                margin: EdgeInsets.zero,
                controller: line.textController,
                textAlign: TextAlign.center,
                onEditingComplete: () => _saveCart(),
              ),
            ),
          ),
        ),
      ),
    );
  }

  _saveCart() async {
    for (var line in _cart.lines) {
      line.quantity = int.parse(line.textController.text);
    }

    _cart = await EnterativeServices.instance.saveCart(context, _user, _cart);
    setState(() {});
  }
}
