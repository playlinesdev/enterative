import 'package:enterativeflutter/widgets/widget_factory.dart';
import 'package:flutter/material.dart';

class PasswordField extends StatefulWidget {
  final String label;
  final String Function(String) validator;
  final TextInputType keyboardType;
  final TextEditingController controller;
  final String hintText;
  final IconData prefixIcon;
  final bool autofocus;

  const PasswordField({
    Key key,
    this.label,
    this.validator,
    this.keyboardType,
    this.controller,
    this.hintText,
    this.prefixIcon,
    this.autofocus = false,
  }) : super(key: key);

  @override
  PasswordFieldState createState() => PasswordFieldState();
}

class PasswordFieldState extends State<PasswordField> {
  bool _obscureText = true;

  onEyePressed() {
    setState(() {
      _obscureText = !_obscureText;
    });
  }

  @override
  Widget build(BuildContext context) {
    Widget prefix;
    if (widget.prefixIcon != null) {
      prefix = Container(
        padding: EdgeInsets.symmetric(horizontal: 10),
        margin: EdgeInsets.all(10),
        decoration: BoxDecoration(border: Border(right: BorderSide(width: 1, color: Colors.grey))),
        child: Icon(widget.prefixIcon),
      );
    }

    Widget suffix = Container(
      padding: EdgeInsets.symmetric(horizontal: 10),
      margin: EdgeInsets.all(10),
      decoration: BoxDecoration(border: Border(left: BorderSide(width: 1, color: Colors.grey))),
      child: InkWell(
        child: Icon(_obscureText ? Icons.visibility : Icons.visibility_off),
        onTap: this.onEyePressed,
      ),
    );

    return Container(
      margin: WidgetFactory.defaultMargin,
      child: new TextFormField(
        autofocus: widget.autofocus,
        decoration: new InputDecoration(
          prefixIcon: prefix,
          suffixIcon: suffix,
          contentPadding: EdgeInsets.symmetric(vertical: 0, horizontal: 15),
          labelText: widget.label,
          hintText: widget.hintText,
          fillColor: Colors.white,
          errorStyle: TextStyle(color: Colors.red[500], fontSize: 14),
          border: new OutlineInputBorder(
            borderRadius: new BorderRadius.circular(25.0),
            borderSide: new BorderSide(),
          ),
        ),
        validator: widget.validator,
        keyboardType: widget.keyboardType,
        controller: widget.controller,
        autocorrect: false,
        obscureText: _obscureText,
      ),
    );
  }
}
