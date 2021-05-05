import 'package:flutter/material.dart';

class MentesQuePensamLogo extends StatelessWidget {
  final double height;
  MentesQuePensamLogo(this.height);

  @override
  Widget build(BuildContext context) {
    return Image.asset(
      'assets/img/mentes_que_pensam_logo.jpg',
      height: height,
    );
  }
}
