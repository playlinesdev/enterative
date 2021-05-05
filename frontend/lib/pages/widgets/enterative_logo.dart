import 'package:flutter/material.dart';

class EnterativeLogoWidget extends StatelessWidget {
  final double height;
  EnterativeLogoWidget(this.height);

  @override
  Widget build(BuildContext context) {
    return Image.asset(
      'assets/img/enterative_logo.png',
      height: height,
    );
  }
}
