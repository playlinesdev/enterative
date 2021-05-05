class GiftCard {
  final String cardNo;
  final String barcode;
  final String ean;
  final String produto;
  final String produtoImagem;
  final String numeroPedido;
  final String statusPedido;
  final String resposta;
  final String respostaAux;
  final double valor;
  final double shopBalance;

  GiftCard(
      {this.cardNo,
      this.barcode,
      this.ean,
      this.produto,
      this.produtoImagem,
      this.numeroPedido,
      this.statusPedido,
      this.resposta,
      this.respostaAux,
      this.valor,
      this.shopBalance});

  factory GiftCard.fromJson(Map<String, dynamic> json) => GiftCard(
        cardNo: json["cardNo"],
        barcode: json["barcode"],
        ean: json["ean"],
        produto: json["produto"],
        produtoImagem: json["produtoImagem"],
        numeroPedido: json["numeroPedido"],
        statusPedido: json["statusPedido"],
        resposta: json["resposta"],
        respostaAux: json["respostaAux"],
        valor: json["valor"],
        shopBalance: json["shopBalance"],
      );

  Map<String, dynamic> toMap() => {
    "cardNo": cardNo,
    "barcode": barcode,
    "ean": ean,
  };
}