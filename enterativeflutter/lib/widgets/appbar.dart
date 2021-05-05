import 'package:enterativeflutter/changenotifiers/user_notifier.dart';
import 'package:enterativeflutter/model/user.dart';
import 'package:enterativeflutter/pages/cart_page.dart';
import 'package:enterativeflutter/webservice/enterative_services.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class EnterativeAppbar {
  static AppBar build(BuildContext context, {bool showCart = true}) {
    User user = Provider.of<UserNotifier>(context);

    return AppBar(
      actions: <Widget>[
        if (showCart)
          Container(
            margin: EdgeInsets.only(right: 15),
            child: InkWell(
              onTap: () => {
                Navigator.of(context)
                    .push(MaterialPageRoute(builder: (_) => ShoppingCartPage()))
              },
              child: Stack(
                children: <Widget>[
                  Container(
                    margin: EdgeInsets.only(top: 15),
                    child: Icon(
                      Icons.shopping_cart,
                    ),
                  ),
                  FutureBuilder(
                    future:
                        EnterativeServices.instance.cartCount(context, user),
                    builder: (context, snapshot) {
                      if (snapshot.connectionState != ConnectionState.done) {
                        return Container();
                      }

                      var count = snapshot.data;
                      return count > 0
                          ? Positioned(
                              right: 0,
                              top: 7,
                              child: Container(
                                padding: EdgeInsets.all(2),
                                decoration: BoxDecoration(
                                  color: Colors.red,
                                  borderRadius: BorderRadius.circular(6),
                                ),
                                constraints: BoxConstraints(
                                  minWidth: 14,
                                  minHeight: 14,
                                ),
                                child: Text(
                                  '$count',
                                  style: TextStyle(
                                    color: Colors.white,
                                    fontSize: 10,
                                  ),
                                  textAlign: TextAlign.center,
                                ),
                              ),
                            )
                          : Container();
                    },
                  ),
                ],
              ),
            ),
          ),
      ],
    );
  }
}
