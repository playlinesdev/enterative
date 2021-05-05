import 'package:flutter/material.dart';

class EnterativeThemeData {
  EnterativeThemeData._();

  static ThemeData build(BuildContext context) {
    var primarySwatch = Colors.blue;
    var accentColor = Colors.blueAccent;

    return ThemeData.light().copyWith(
      textTheme: Theme.of(context).textTheme.apply(bodyColor: primarySwatch, displayColor: primarySwatch),
      accentColor: accentColor,
      toggleableActiveColor: accentColor,
      backgroundColor: Colors.black12,
    );
  }
}
