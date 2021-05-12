import 'package:flutter/material.dart';
import 'package:frontend/widgets/enterative_frontend_app.dart';
import 'package:url_strategy/url_strategy.dart';

void main() async {
  setPathUrlStrategy();
  runApp(EnterativeFrontendApp());
}
