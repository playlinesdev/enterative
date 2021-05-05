import 'package:enterativeflutter/changenotifiers/user_notifier.dart';
import 'package:enterativeflutter/model/product.dart';
import 'package:enterativeflutter/model/user.dart';
import 'package:enterativeflutter/webservice/enterative_services.dart';
import 'package:enterativeflutter/widgets/appbar.dart';
import 'package:enterativeflutter/widgets/base64_image.dart';
import 'package:enterativeflutter/widgets/drawer.dart';
import 'package:enterativeflutter/widgets/widget_factory.dart';
import 'package:flushbar/flushbar_helper.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class StorePage extends StatefulWidget {
  @override
  _StorePageState createState() => _StorePageState();
}

class _StorePageState extends State<StorePage> {
  User _user;

  @override
  Widget build(BuildContext context) {
    _user = Provider.of<UserNotifier>(context);

    return SafeArea(
      child: Scaffold(
        appBar: EnterativeAppbar.build(context),
        drawer: EnterativeDrawer(),
        body: FutureBuilder(
          future: EnterativeServices.instance.storeProducts(context, _user),
          builder: (context, snapshot) {
            if (snapshot.connectionState != ConnectionState.done) {
              return WidgetFactory.createLoading();
            } else if (snapshot.data == null || snapshot.data.length == 0) {
              return WidgetFactory.createNoDataFound();
            }

            List<Product> products = snapshot.data;
            products.sort((a, b) {
              var favA = a.favorite ?? false;
              var favB = b.favorite ?? false;
              int favCompare = favB ? 1 : favA ? -1 : 0;
              if (favCompare != 0) return favCompare;

              var highA = a.highlight ?? false;
              var highB = b.highlight ?? false;
              int highCompare = highB ? 1 : highA ? -1 : 0;
              if (highCompare != 0) return highCompare;

              int nameCompare = a.displayName.compareTo(b.displayName);
              return nameCompare;
            });

            return Container(
              child: ListView.builder(
                scrollDirection: Axis.vertical,
                key: PageStorageKey("storelist"),
                itemCount: snapshot.data.length,
                itemBuilder: (context, index) {
                  Product item = snapshot.data[index];
                  return this._createProductCard(item);
                },
              ),
            );
          },
        ),
      ),
    );
  }

  Widget _createProductCard(Product product) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(2.0),
        child: ListTile(
          leading: Base64Image(
            source: product.imagem,
          ),
          title: WidgetFactory.createStaticText(product.displayName),
          trailing: SizedBox.fromSize(
            size: Size.fromWidth(100),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: <Widget>[
                if (product.highlight)
                  Icon(
                    Icons.star,
                    color: Colors.yellow,
                  ),
                InkWell(
                  onTap: () => _productFavoriteTap(product),
                  child: Icon(
                    product.favorite == true
                        ? Icons.favorite
                        : Icons.favorite_border,
                    color: Colors.red,
                  ),
                ),
                Container(
                  margin: EdgeInsets.only(left: 10),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(25),
                    border: Border.fromBorderSide(
                      BorderSide(
                        color: Colors.blueAccent,
                      ),
                    ),
                  ),
                  child: InkWell(
                    onTap: () => _productAddCartTap(product),
                    child: Padding(
                      padding: EdgeInsets.all(5),
                      child: Icon(
                        Icons.add_shopping_cart,
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  _productFavoriteTap(Product product) async {
    bool success = await EnterativeServices.instance.toggleFavorite(context, product.id, _user);
    if (success == true) {
      setState(() {});
    } else if (success == false) {
      FlushbarHelper.createError(message: "Não foi possível mudar o favorito!").show(context);
    }
  }

  _productAddCartTap(Product product) async {
    bool success = await EnterativeServices.instance.addToCart(context, product.id, _user);
    if (success == true) {
      FlushbarHelper.createSuccess(
          message: "Product [${product.displayName}] adicionado ao carrinho!")
          .show(context);
    } else if (success == false) {
      FlushbarHelper.createError(message: "Não foi possível adicionar ao carrinho!").show(context);
    }
  }
}
