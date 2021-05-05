import 'dart:convert';
import 'dart:io';

import 'package:enterativeflutter/helper/user_config.dart';
import 'package:enterativeflutter/model/card.dart';
import 'package:enterativeflutter/model/product.dart';
import 'package:enterativeflutter/model/sale_order.dart';
import 'package:enterativeflutter/model/shopping_cart.dart';
import 'package:enterativeflutter/model/user.dart';
import 'package:flushbar/flushbar_helper.dart';
import 'package:flutter/material.dart';

class EnterativeServices {
  EnterativeServices._();

  static final EnterativeServices instance = EnterativeServices._();

  static HttpClient _client = new HttpClient()
    ..badCertificateCallback = (_certificateCheck);

  static String _host = "192.168.0.104";

  static bool _certificateCheck(X509Certificate cert, String host, int port) =>
      host == _host;

  final String _baseURL = "https://$_host:8123/flutter";

  String _createBasicAuth(User user) {
    return "Basic " +
        base64Encode(utf8.encode("${user.login}:${user.password}"));
  }

  _createHeader(HttpClientRequest request, User user) {
    request.headers.add("authorization", this._createBasicAuth(user));
    request.headers.add("Accept-Language", "pt-BR");
    request.headers.add("Content-Type", "application/json");
  }

  Future<WSResponse> _post(BuildContext context,
      {@required String url, @required User user, dynamic body}) async {
    try {
      HttpClientRequest request = await _client.postUrl(Uri.parse(url));
      this._createHeader(request, user);
      if (body != null) {
        request.add(utf8.encode(json.encode(body)));
      }
      HttpClientResponse response = await request.close();
      String raw = await response.transform(utf8.decoder).join();
      return WSResponse.fromJSON(json.decode(raw));
    } on SocketException {
      FlushbarHelper.createError(
        message: "Não foi possível estabelecer uma conexão com o servidor!",
      ).show(context);
      return null;
    }
  }

  Future<WSResponse> _put(BuildContext context,
      {@required String url, @required User user}) async {
    try {
      HttpClientRequest request = await _client.putUrl(Uri.parse(url));
      this._createHeader(request, user);
      HttpClientResponse response = await request.close();
      String raw = await response.transform(utf8.decoder).join();
      return WSResponse.fromJSON(json.decode(raw));
    } on SocketException {
      FlushbarHelper.createError(
        message: "Não foi possível estabelecer uma conexão com o servidor!",
      ).show(context);
      return null;
    }
  }

  Future<WSResponse> _get(BuildContext context,
      {@required String url, @required User user}) async {
    try {
      HttpClientRequest request = await _client.getUrl(Uri.parse(url));
      this._createHeader(request, user);
      HttpClientResponse response = await request.close();
      String raw = await response.transform(utf8.decoder).join();
      return WSResponse.fromJSON(json.decode(raw));
    } on SocketException {
      FlushbarHelper.createError(
        message: "Não foi possível estabelecer uma conexão com o servidor!",
      ).show(context);
      return null;
    }
  }

  Future<bool> doLogin(BuildContext context, User user) async {
    var url = "$_baseURL/login";
    var response = await this._get(context, url: url, user: user);

    if (response != null && response.response != null) {
      return response.response == true;
    } else {
      return null;
    }
  }

  Future<GiftCard> card(BuildContext context, User user, String barcode) async {
    var url = "$_baseURL/card/$barcode";
    var response = await this._post(context, url: url, user: user);
    if (response != null && response.response != null) {
      return GiftCard.fromJson(response.response);
    } else {
      return null;
    }
  }

  Future<List<Product>> storeProducts(BuildContext context, User user) async {
    bool fromCache = false;
    String lastProductCache = await UserConfig.get("lastProductCache");
    if (lastProductCache != null && lastProductCache.isNotEmpty) {
      DateTime lastProductCacheDate = DateTime.parse(lastProductCache);
      if (lastProductCacheDate.difference(DateTime.now()).inDays <= 7) {
        fromCache = true;
      }
    }

    if (!fromCache) {
      await UserConfig.set("lastProductCache", DateTime.now().toString());
    }

    var url = "$_baseURL/store/products" + (fromCache ? "/cached" : "");
    var response = await this._get(context, url: url, user: user);
    if (response != null) {
      List<Product> result = [];
      for (Map<String, dynamic> map in response.response) {
        Product product = Product.fromJson(map);
        if (fromCache) {
          product.imagem = await UserConfig.get("product-${product.id}");
        } else {
          await UserConfig.set("product-${product.id}", product.imagem);
        }
        result.add(product);
      }
      return result;
    } else {
      return null;
    }
  }

  Future<bool> toggleFavorite(BuildContext context, int id, User user) async {
    var url = "$_baseURL/store/favorites/$id";
    var response = await this._put(context, url: url, user: user);

    if (response != null && response.response != null) {
      return response.response == true;
    } else {
      return null;
    }
  }

  Future<bool> addToCart(BuildContext context, int id, User user) async {
    var url = "$_baseURL/cart/add/$id";
    var response = await this._post(context, url: url, user: user);

    if (response != null && response.response != null) {
      return response.response == true;
    } else {
      return null;
    }
  }

  Future<int> cartCount(BuildContext context, User user) async {
    var url = "$_baseURL/cart/count";
    var response = await this._get(context, url: url, user: user);

    if (response != null) {
      return response.response;
    } else {
      return 0;
    }
  }

  Future<int> concludeCart(BuildContext context, User user) async {
    var url = "$_baseURL/cart/conclude";
    var response = await this._post(context, url: url, user: user);

    if (response != null) {
      return response.response;
    } else {
      return null;
    }
  }

  Future<int> activateCard(BuildContext context, User user, GiftCard card) async {
    var url = "$_baseURL/card/activate";
    var response = await this._post(context, url: url, user: user, body: card.toMap());

    if (response != null) {
      return response.response;
    } else {
      return null;
    }
  }

  Future<ShoppingCart> cart(BuildContext context, User user) async {
    var url = "$_baseURL/cart";
    var response = await this._get(context, url: url, user: user);

    if (response != null) {
      return ShoppingCart.fromJson(response.response);
    } else {
      return null;
    }
  }

  Future<ShoppingCart> saveCart(
      BuildContext context, User user, ShoppingCart cart) async {
    var url = "$_baseURL/cart";
    var response =
        await this._post(context, url: url, user: user, body: cart.toMap());

    if (response != null) {
      return ShoppingCart.fromJson(response.response);
    } else {
      return null;
    }
  }

  Future<bool> callbackStatus(
      BuildContext context, User user, int orderID) async {
    var url = "$_baseURL/saleorder/callbackstatus/$orderID";
    var response = await this._get(context, url: url, user: user);
    if (response != null) {
      return response.response;
    } else {
      return null;
    }
  }

  Future<SaleOrder> saleOrder(
      BuildContext context, User user, int orderID) async {
    var url = "$_baseURL/saleorder/$orderID";
    var response = await this._get(context, url: url, user: user);
    if (response != null) {
      return SaleOrder.fromJson(response.response);
    } else {
      return null;
    }
  }

  Future<List<SaleOrderReceipt>> receipt(
      BuildContext context, User user, int orderID) async {
    var url = "$_baseURL/saleorder/receipt/$orderID";
    var response = await this._get(context, url: url, user: user);
    if (response != null) {
      return [
        for (var json in response.response) SaleOrderReceipt.fromJson(json)
      ];
    } else {
      return null;
    }
  }
}

class WSResponse {
  final String message;
  final dynamic response;
  final String signature;

  WSResponse({this.message, this.response, this.signature});

  factory WSResponse.fromJSON(Map<String, dynamic> json) => WSResponse(
        message: json["message"],
        response: json["response"],
        signature: json["signature"],
      );
}
