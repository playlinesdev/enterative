import 'package:enterativeflutter/changenotifiers/user_notifier.dart';
import 'package:enterativeflutter/pages/barcode_page.dart';
import 'package:enterativeflutter/pages/cart_page.dart';
import 'package:enterativeflutter/widgets/widget_factory.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class EnterativeDrawer extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        children: <Widget>[
          DrawerHeader(
            child: Image(image: AssetImage("assets/logo.png")),
          ),
          ListTile(
            title: WidgetFactory.createStaticText("Loja"),
            leading: Icon(Icons.store),
            onTap: () =>
                Navigator.of(context)
                    .pushNamedAndRemoveUntil("/", (_) => false),
          ),
          ListTile(
            title: WidgetFactory.createStaticText("Carrinho de Compras"),
            leading: Icon(Icons.shopping_cart),
            onTap: () =>
                Navigator.of(context)
                    .push(
                    MaterialPageRoute(builder: (_) => ShoppingCartPage())),
          ),
          Divider(),
          ListTile(
            title: WidgetFactory.createStaticText("Ativar Cartão"),
            leading: Icon(Icons.card_giftcard),
            onTap: () =>
                Navigator.of(context).push(
                    MaterialPageRoute(builder: (_) => BarcodePage())),
          ),
          Divider(),
          ListTile(
            title: WidgetFactory.createStaticText("Sair"),
            leading: Icon(Icons.exit_to_app),
            onTap: () {
              Provider.of<UserNotifier>(context, listen: false).logout();
              Navigator.of(context).pushNamedAndRemoveUntil("/", (_) => false);
            },
          )
        ],
      ),
    );
  }
}
