import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class RadioGroup extends StatefulWidget {
  final String label;
  final List<String> possibleLabels;
  final int initialSelectedIndex;
  final void Function(Object?)? onIndexSelected;

  RadioGroup(
      {required this.label,
      required this.possibleLabels,
      this.initialSelectedIndex = 0,
      this.onIndexSelected});

  @override
  _RadioGroupState createState() => _RadioGroupState();
}

class _RadioGroupState extends State<RadioGroup> {
  final TextStyle labelStyle = GoogleFonts.roboto(
    fontWeight: FontWeight.bold,
    color: Color(0xff555555),
    fontSize: 15,
  );
  int value = 0;

  @override
  void initState() {
    super.initState();
    value = widget.initialSelectedIndex;
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(widget.label, style: labelStyle),
        SizedBox(height: 5),
        ...List.generate(widget.possibleLabels.length, (index) {
          String label = widget.possibleLabels[index];
          return Row(
            children: [
              Radio(
                  value: value,
                  groupValue: index,
                  onChanged: (i) {
                    if (mounted) {
                      setState(() {
                        value = index;
                      });
                      widget.onIndexSelected!(value);
                    }
                  }),
              Text(label)
            ],
          );
        })
      ],
    );
  }
}
