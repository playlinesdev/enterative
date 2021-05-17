import 'dart:typed_data';

import 'package:flutter/cupertino.dart';
import 'package:provider/provider.dart';

class AffiliateForm with ChangeNotifier {
  String _razaoSocial = '';
  String _fantasia = '';
  String _tipoLoja = 'Física';
  String _cnpj = '';
  String _inscricaoEstadual = '';
  String _inscricaoMunicipal = '';
  String _cpf = '';
  String _nomeResponsavel = '';
  String _emailResponsavel = '';
  String _link = '';
  String _ramoAtividade = '';
  String _enderecoRua = '';
  String _enderecoBairro = '';
  String _enderecoCidade = '';
  String _enderecoEstado = '';
  String _enderecoCep = '';
  String _enderecoPais = '';

  String get razaoSocial => _razaoSocial;
  String get fantasia => _fantasia;
  String get tipoLoja => _tipoLoja;
  String get cnpj => _cnpj;
  String get inscricaoEstadual => _inscricaoEstadual;
  String get inscricaoMunicipal => _inscricaoMunicipal;
  String get cpf => _cpf;
  String get nomeResponsavel => _nomeResponsavel;
  String get emailResponsavel => _emailResponsavel;
  String get link => _link;
  String get ramoAtividade => _ramoAtividade;
  String get enderecoRua => _enderecoRua;
  String get enderecoBairro => _enderecoBairro;
  String get enderecoCidade => _enderecoCidade;
  String get enderecoEstado => _enderecoEstado;
  String get enderecoCep => _enderecoCep;
  String get enderecoPais => _enderecoPais;

  set razaoSocial(String newValue) {
    _razaoSocial = newValue;
    notifyListeners();
  }

  set fantasia(String newValue) {
    _fantasia = newValue;
    notifyListeners();
  }

  set cnpj(String newValue) {
    _cnpj = newValue;
    notifyListeners();
  }

  set inscricaoEstadual(String newValue) {
    _inscricaoEstadual = newValue;
    notifyListeners();
  }

  set inscricaoMunicipal(String newValue) {
    _inscricaoMunicipal = newValue;
    notifyListeners();
  }

  set cpf(String newValue) {
    _cpf = newValue;
    notifyListeners();
  }

  set nomeResponsavel(String newValue) {
    _nomeResponsavel = newValue;
    notifyListeners();
  }

  set emailResponsavel(String newValue) {
    _emailResponsavel = newValue;
    notifyListeners();
  }

  set link(String newValue) {
    _link = newValue;
    notifyListeners();
  }

  set ramoAtividade(String newValue) {
    _ramoAtividade = newValue;
    notifyListeners();
  }

  set enderecoRua(String newValue) {
    _enderecoRua = newValue;
    notifyListeners();
  }

  set enderecoBairro(String newValue) {
    _enderecoBairro = newValue;
    notifyListeners();
  }

  set enderecoCidade(String newValue) {
    _enderecoCidade = newValue;
    notifyListeners();
  }

  set enderecoEstado(String newValue) {
    _enderecoEstado = newValue;
    notifyListeners();
  }

  set enderecoPais(String newValue) {
    _enderecoPais = newValue;
    notifyListeners();
  }

  set enderecoCep(String newValue) {
    _enderecoCep = newValue;
    notifyListeners();
  }

  set tipoLoja(String newValue) {
    _tipoLoja = newValue;
    notifyListeners();
  }

  Uint8List? facadePicture;

  get isReady =>
      razaoSocial.isNotEmpty &&
      fantasia.isNotEmpty &&
      cnpj.isNotEmpty &&
      cpf.isNotEmpty &&
      nomeResponsavel.isNotEmpty &&
      link.isNotEmpty &&
      ramoAtividade.isNotEmpty &&
      enderecoRua.isNotEmpty &&
      enderecoBairro.isNotEmpty &&
      enderecoCidade.isNotEmpty &&
      enderecoEstado.isNotEmpty &&
      enderecoCep.isNotEmpty &&
      enderecoPais.isNotEmpty;

  static AffiliateForm of(BuildContext context, {bool listen = true}) {
    return Provider.of<AffiliateForm>(context, listen: listen);
  }
}
