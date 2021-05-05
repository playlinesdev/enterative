import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class BeAPartner extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      alignment: Alignment.centerLeft,
      height: 100,
      child: Text(
        'Seja um Parceiro',
        style: GoogleFonts.aBeeZee(
          fontWeight: FontWeight.bold,
          color: Color(0xff555555),
          fontSize: 28,
        ),
      ),
    );
  }
}
