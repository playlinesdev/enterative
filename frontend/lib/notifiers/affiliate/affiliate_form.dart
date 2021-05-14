import 'dart:typed_data';

import 'package:flutter/cupertino.dart';
import 'package:provider/provider.dart';

class AffiliateForm with ChangeNotifier {
  String razaoSocial = '';
  String fantasia = '';
  String tipoLoja = 'Física';
  String cnpj = '';
  String inscricaoEstadual = '';
  String inscricaoMunicipal = '';
  String cpf = '';
  String nomeResponsavel = '';
  String emailResponsavel = '';
  String link = '';
  String ramoAtividade = '';
  String enderecoRua = '';
  String enderecoBairro = '';
  String enderecoCidade = '';
  String enderecoEstado = '';
  String enderecoCep = '';
  String enderecoPais = '';
  Uint8List? facadePicture;

  static AffiliateForm of(BuildContext context, {bool listen = true}) {
    return Provider.of<AffiliateForm>(context, listen: listen);
  }
}
