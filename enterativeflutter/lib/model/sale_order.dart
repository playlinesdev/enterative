import 'package:enterativeflutter/model/product.dart';

class SaleOrder {
  final int id;
  final DateTime createdAt;
  final double amount;
  final String translatedStatus;
  final String translatedType;
  final String emailStatus;

  final List<SaleOrderLine> lines;

  SaleOrder(
      {this.id,
      this.createdAt,
      this.amount,
      this.translatedStatus,
      this.translatedType,
      this.emailStatus,
      this.lines});

  factory SaleOrder.fromJson(Map<String, dynamic> json) => SaleOrder(
        id: json["id"],
        createdAt: DateTime.parse(json["createdAt"]),
        amount: json["amount"],
        translatedStatus: json["translatedStatus"],
        translatedType: json["translatedType"],
        emailStatus: json["emailStatus"],
        lines: [
          if (json["lines"] != null)
            for (var line in json["lines"]) SaleOrderLine.fromJson(line)
        ],
      );
}

class SaleOrderLine {
  final int id;
  final Product product;
  final String status;
  final String externalCode;
  final String response;
  final String translatedResponse;
  final String responseAux;
  final String translatedResponseAux;
  final String activationStatus;
  final String callbackStatus;
  final double amount;

  SaleOrderLine(
      {this.translatedResponse,
      this.translatedResponseAux,
      this.id,
      this.product,
      this.status,
      this.externalCode,
      this.response,
      this.responseAux,
      this.activationStatus,
      this.callbackStatus,
      this.amount});

  factory SaleOrderLine.fromJson(Map<String, dynamic> json) => SaleOrderLine(
        id: json["id"],
        product: Product.fromJson(json["product"]),
        status: json["translatedStatus"],
        externalCode: json["externalCode"],
        response: json["response"],
        translatedResponse: json["translatedResponse"],
        responseAux: json["responseAux"],
        translatedResponseAux: json["translatedResponseAux"],
        activationStatus: json["translatedActivationStatus"],
        callbackStatus: json["callbackStatus"],
        amount: json["amount"],
      );
}

class SaleOrderReceipt {
  final DateTime date;
  final double amount;
  final String status;
  final String type;
  final String product;
  final String pin;

  SaleOrderReceipt(
      {this.date, this.amount, this.status, this.type, this.product, this.pin});

  factory SaleOrderReceipt.fromJson(Map<String, dynamic> json) =>
      SaleOrderReceipt(
        date: DateTime.parse(json["date"]),
        amount: json["amount"],
        status: json["status"],
        type: json["type"],
        product: json["product"],
        pin: json["pin"],
      );
}
