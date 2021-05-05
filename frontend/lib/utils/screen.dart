import 'package:flutter/material.dart';

class Tools {
  ///Retrives a [percentage] of the widget width
  static double screenWidth(BuildContext context, double percentage) {
    return (percentage / 100) * MediaQuery.of(context).size.width;
  }

  ///Retrives a [percentage] of the widget height
  static double screenHeight(BuildContext context, double percentage) {
    return (percentage / 100) * MediaQuery.of(context).size.height;
  }
}
