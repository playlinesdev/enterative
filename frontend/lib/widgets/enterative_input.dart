import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

typedef InputValidationFunction = String? Function(String?);

class EnterativeInput extends StatelessWidget {
  final String label;
  final Function(String) onChanged;
  final bool isRequired;
  // final GlobalKey<FormState>? formKey;
  final List<InputValidationFunction> validations;
  final TextStyle labelStyle = GoogleFonts.roboto(
    fontWeight: FontWeight.bold,
    color: Color(0xff555555),
    fontSize: 15,
  );

  EnterativeInput(this.label, {required this.onChanged, this.isRequired = true, this.validations = const []});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.symmetric(vertical: 5),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            child: Row(
              children: [
                Text(label, style: labelStyle),
                if (isRequired) SizedBox(width: 5),
                if (isRequired) Text('*', style: labelStyle.copyWith(color: Colors.red))
              ],
            ),
          ),
          LimitedBox(
            maxHeight: 44,
            child: TextFormField(
              onChanged: onChanged,
              validator: (value) {
                for (var validation in validations) {
                  var result = validation(value);
                  return result;
                }
                return null;
              },
              decoration: InputDecoration(
                helperText: ' ',
                contentPadding: EdgeInsets.symmetric(horizontal: 6),
                border: OutlineInputBorder(),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
