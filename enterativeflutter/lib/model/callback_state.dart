import 'package:flutter/material.dart';

class CallbackState {
  final bool loading;
  final DateTime lastUpdate;

  CallbackState.loading({this.loading = true, @required this.lastUpdate});

  CallbackState.done({this.loading = false, this.lastUpdate});
}