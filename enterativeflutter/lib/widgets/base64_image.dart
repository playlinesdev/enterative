import 'dart:convert';
import 'dart:typed_data';

import 'package:flutter/material.dart';

class Base64Image extends StatelessWidget {
  final String source;
  final double width;
  final double height;

  Base64Image({Key key, this.source, this.width = 80, this.height = 100});

  @override
  Widget build(BuildContext context) {
    final Uint8List bytes = Base64Decoder()
        .convert(source.replaceFirst("data:image/png;base64, ", ""));

    return Container(
      width: width,
      height: height,
      decoration: BoxDecoration(
        image: DecorationImage(
          image: MemoryImage(bytes),
          fit: BoxFit.fill,
        ),
      ),
    );
  }
}
