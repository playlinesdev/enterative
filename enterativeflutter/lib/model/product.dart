class Product {
  final int id;
  final String displayName;
  String imagem;
  final double amount;
  final bool favorite;
  final bool highlight;
  final String name;

  Product(
      {this.id,
      this.displayName,
      this.imagem,
      this.amount,
      this.favorite,
      this.highlight,
      this.name});

  factory Product.fromJson(Map<String, dynamic> json) => Product(
        id: json["id"],
        name: json["name"],
        displayName: json["displayName"],
        imagem: json["imagem"],
        amount: json["amount"],
        favorite: json["favorite"],
        highlight: json["highlight"],
      );
}
