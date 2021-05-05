import 'package:enterativeflutter/model/product.dart';
import 'package:flutter/material.dart';

class ShoppingCart {
  final int id;
  final double amount;
  final List<ShoppingCartLine> lines;

  ShoppingCart({this.id, this.amount, this.lines});

  factory ShoppingCart.fromJson(Map<String, dynamic> json) => ShoppingCart(
        id: json["id"],
        amount: json["amount"],
        lines: [
          if (json["lines"] != null)
            for (var line in json["lines"]) ShoppingCartLine.fromJson(line)
        ],
      );

  Map<String, dynamic> toMap() => {
        "id": this.id,
        "amount": this.amount,
        "lines": [for (var line in this.lines) line.toMap()],
      };
}

class ShoppingCartLine {
  final int id;
  final Product product;
  int quantity;
  final double amount;
  final double totalAmount;
  final String userEmail;
  final String userCellphone;
  TextEditingController textController;

  ShoppingCartLine(
      {this.id,
      this.product,
      this.quantity,
      this.amount,
      this.totalAmount,
      this.userEmail,
      this.userCellphone});

  factory ShoppingCartLine.fromJson(Map<String, dynamic> json) =>
      ShoppingCartLine(
        id: json["id"],
        product: Product.fromJson(json["product"]),
        quantity: (json["quantity"] as double).floor(),
        amount: json["amount"],
        totalAmount: json["totalAmount"],
        userEmail: json["userEmail"],
        userCellphone: json["userCellphone"],
      );

  Map<String, dynamic> toMap() => {
        "id": this.id,
        "quantity": this.quantity,
      };
}
