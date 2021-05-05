import 'package:enterativeflutter/helper/optional.dart';
import 'package:enterativeflutter/helper/value_formatter.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_datetime_picker/flutter_datetime_picker.dart';

class WidgetFactory {
  WidgetFactory._();

  static const defaultMargin =
      EdgeInsets.only(top: 15.0, left: 15.0, right: 15.0);

  static FloatingActionButton createFloatingActionButton(BuildContext context,
      {void Function() onPressed, String tooltip, Widget child, String heroTag}) {
    return FloatingActionButton(
      backgroundColor: Colors.white,
      heroTag: heroTag,
      onPressed: onPressed,
      tooltip: tooltip,
      child: child,
      foregroundColor: Theme.of(context).accentColor,
      shape: CircleBorder(
        side: BorderSide(
          color: Theme.of(context).accentColor,
        ),
      ),
    );
  }

  static Container createLoading() {
    return Container(
      margin: defaultMargin,
      child: Center(
        child: CircularProgressIndicator(
          valueColor: new AlwaysStoppedAnimation<Color>(Colors.blue),
        ),
      ),
    );
  }

  static Container createNoDataFound() {
    return Container(
      child: Center(
        child: WidgetFactory.createDialogTitle("Nenhum dado foi encontrado!"),
      ),
    );
  }

  static Dismissible createDismissibleItem(BuildContext context, dynamic item,
      void Function() onDelete, void Function() onTap,
      {String trailing}) {
    Widget title;
    if (trailing != null) {
      title = Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          WidgetFactory.createStaticText("${item.description}"),
          WidgetFactory.createStaticText(trailing),
        ],
      );
    } else {
      title = WidgetFactory.createStaticText("${item.description}");
    }

    return Dismissible(
      key: Key(item.id.toString()),
      direction: DismissDirection.endToStart,
      onDismissed: (direction) {
        if (direction == DismissDirection.endToStart) {
          onDelete();
        }
      },
      background: Container(color: Colors.red),
      child: Card(
        child: InkWell(
          onTap: onTap,
          child: Padding(
            padding: const EdgeInsets.all(5.0),
            child: ListTile(
              title: title,
            ),
          ),
        ),
      ),
    );
  }

  static Container createDatePicker(BuildContext context,
      {dynamic Function(DateTime) onConfirm,
      DateTime currentTime,
      EdgeInsets margin}) {
    return Container(
      margin: Optional.of(margin).orElse(defaultMargin),
      child: RaisedButton(
        shape:
            RoundedRectangleBorder(borderRadius: BorderRadius.circular(25.0)),
        elevation: 4.0,
        onPressed: () {
          DatePicker.showDatePicker(context,
              theme: DatePickerTheme(
                containerHeight: 210.0,
              ),
              showTitleActions: true,
              onConfirm: onConfirm,
              currentTime: currentTime);
        },
        child: Container(
          alignment: Alignment.center,
          height: 50.0,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.start,
            children: <Widget>[
              Expanded(
                flex: 1,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    Icon(
                      Icons.date_range,
                      size: 18.0,
                      color: Colors.grey,
                    ),
                  ],
                ),
              ),
              Expanded(
                flex: 10,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    Text(
                      " ${ValueFormatter.date(currentTime)}",
                      style: TextStyle(
                          color: Colors.grey,
                          fontWeight: FontWeight.bold,
                          fontSize: 18.0),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
        color: Colors.black54,
      ),
    );
  }

  static Container createDatetimePicker(BuildContext context,
      {dynamic Function(DateTime) onConfirm, DateTime currentTime}) {
    return Container(
      margin: defaultMargin,
      child: RaisedButton(
          shape:
              RoundedRectangleBorder(borderRadius: BorderRadius.circular(25.0)),
          elevation: 4.0,
          onPressed: () {
            DatePicker.showDateTimePicker(context,
                theme: DatePickerTheme(
                  containerHeight: 210.0,
                ),
                showTitleActions: true,
                onConfirm: onConfirm,
                currentTime: currentTime);
          },
          child: Container(
            alignment: Alignment.center,
            height: 50.0,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.start,
              children: <Widget>[
                Expanded(
                  flex: 1,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      Icon(
                        Icons.date_range,
                        size: 18.0,
                        color: Colors.grey,
                      ),
                    ],
                  ),
                ),
                Expanded(
                  flex: 10,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      Text(
                        " ${ValueFormatter.datetime(currentTime)}",
                        style: TextStyle(
                          color: Colors.grey,
                          fontWeight: FontWeight.bold,
                          fontSize: 18.0,
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
          color: Colors.black54),
    );
  }

  static Container createDropdown(
      BuildContext context, String label, List<dynamic> items,
      {int value,
      void Function(int) onChanged,
      Color fillColor,
      Color borderColor = Colors.grey,
      EdgeInsetsGeometry margin}) {
    List<DropdownMenuItem<int>> dropItems =
        []; //[DropdownMenuItem<int>(value: 0, child: Text(""))];
    items.forEach((item) {
      dropItems.add(DropdownMenuItem<int>(
        value: item.id,
        child: Text(item.description),
      ));
    });

    return Container(
      margin: margin ?? defaultMargin,
      child: DropdownButtonFormField(
        value: value,
        items: dropItems,
        onChanged: onChanged,
        isDense: true,
        style: Theme.of(context).textTheme.body1.copyWith(fontSize: 16.0),
        decoration: new InputDecoration(
          contentPadding: EdgeInsets.symmetric(vertical: 0, horizontal: 15.0),
          labelText: label,
          filled: fillColor != null,
          fillColor: fillColor,
          enabledBorder: new OutlineInputBorder(
            borderRadius: new BorderRadius.circular(25.0),
            borderSide: new BorderSide(color: borderColor),
          ),
          border: new OutlineInputBorder(
            borderRadius: new BorderRadius.circular(25.0),
            borderSide: new BorderSide(),
          ),
        ),
      ),
    );
  }

  static Container createButton(String label,
      {void Function() onPressed, IconData prefixIcon}) {
    Widget prefix;
    if (prefixIcon != null) {
      prefix = Container(
        padding: EdgeInsets.only(right: 10),
        margin: EdgeInsets.only(right: 10),
        child: Icon(
          prefixIcon,
          color: Colors.white,
        ),
      );
    }

    return Container(
      margin: defaultMargin,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(10),
        color: Colors.white.withRed(3).withGreen(67).withBlue(230),
      ),
      child: FlatButton(
        onPressed: onPressed,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            if (prefix != null) prefix,
            if (prefix != null)
              Container(
                decoration: BoxDecoration(
                    border: Border(
                  right: BorderSide(color: Colors.white, width: 2),
                )),
                child: Text(""),
              ),
            WidgetFactory.createStaticText(label, color: Colors.white),
          ],
        ),
      ),
    );
  }

  static Text createStaticText(String text, {Color color, FontWeight fontWeight, double fontSize, TextAlign textAlign}) {
    return Text(
      text,
      textAlign: textAlign,
      style: TextStyle(
        fontSize: fontSize,
        color: color,
        fontWeight: fontWeight,
      ),
    );
  }

  static Container createDialogTitle(String text,
      {Color color, EdgeInsetsGeometry margin}) {
    return Container(
      margin: margin ?? EdgeInsets.zero,
      child: WidgetFactory.createStaticText(text, fontWeight: FontWeight.bold, fontSize: 20.0, color: color),
    );
  }

  static Container createTitle(String text, {Color color}) {
    return Container(
      margin: EdgeInsets.only(left: 15, top: 5),
      child: Text(
        text,
        style: TextStyle(
          fontSize: 30.0,
          fontWeight: FontWeight.bold,
          color: color,
        ),
      ),
    );
  }

  static Container createTextField(String label,
      {String Function(String) validator,
      TextInputType keyboardType,
      TextEditingController controller,
      String hintText,
      IconData prefixIcon,
      bool autocorrect = true,
      bool obscureText = false,
      EdgeInsetsGeometry margin,
      TextAlign textAlign = TextAlign.start,
      void Function(String) onChanged,
      void Function() onEditingComplete,
      bool enabled = true}) {
    Widget prefix;
    if (prefixIcon != null) {
      prefix = Container(
        padding: EdgeInsets.symmetric(horizontal: 10),
        margin: EdgeInsets.all(10),
        decoration: BoxDecoration(
            border: Border(right: BorderSide(width: 1, color: Colors.grey))),
        child: Icon(prefixIcon),
      );
    }

    return Container(
      margin: margin ?? defaultMargin,
      child: new TextFormField(
        enabled: enabled,
        decoration: new InputDecoration(
          prefixIcon: prefix,
          contentPadding: EdgeInsets.symmetric(vertical: 0, horizontal: 15),
          labelText: label,
          hintText: hintText,
          fillColor: Colors.black,
          errorStyle: TextStyle(color: Colors.red[500], fontSize: 14),
          border: new OutlineInputBorder(
            borderRadius: new BorderRadius.circular(25.0),
            borderSide: new BorderSide(color: Colors.black),
          ),
        ),
        validator: validator,
        keyboardType: keyboardType,
        controller: controller,
        autocorrect: autocorrect,
        obscureText: obscureText,
        textAlign: textAlign,
        onChanged: onChanged,
        onEditingComplete: onEditingComplete,
      ),
    );
  }

  static void showDeleteDialog(BuildContext context, dynamic item,
      void Function() onDecline, void Function() onConfirm) {
    showDialog(
      context: context,
      builder: (_context) {
        return AlertDialog(
          title: WidgetFactory.createDialogTitle("Mr. Money"),
          content: WidgetFactory.createStaticText(
              "Do you wish to remove ${item.description}?"),
          actions: <Widget>[
            FlatButton(
              child: WidgetFactory.createStaticText("No"),
              onPressed: onDecline,
            ),
            FlatButton(
              child: WidgetFactory.createStaticText("Yes, remove it",
                  color: Theme.of(context).accentColor),
              color: Theme.of(context).primaryColor,
              onPressed: onConfirm,
            ),
          ],
        );
      },
    );
  }

  static BottomNavigationBar createNavigationBar(
      BuildContext context,
      int selectedIndex,
      List<BottomNavigationBarItem> items,
      void Function(int) onTap) {
    return BottomNavigationBar(
        selectedItemColor: Theme.of(context).accentColor,
        showUnselectedLabels: true,
        onTap: onTap,
        currentIndex: selectedIndex,
        items: items);
  }
}
