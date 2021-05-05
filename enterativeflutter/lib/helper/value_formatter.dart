import 'package:intl/intl.dart';

class ValueFormatter {
  ValueFormatter._();

  static final _datetimeFormat = DateFormat("dd/MM/yyyy HH:mm:ss");
  static final _dateFormat = DateFormat("dd/MM/yyyy");
  static final _timeFormat = DateFormat("HH:mm:ss");
  static final _monthYearFormat = DateFormat("MMMM/yyyy");

  static String currency(double value) {
    if (value != null) return value.toStringAsFixed(2);
    return "";
  }

  static String monthYear(DateTime date) {
    return _monthYearFormat.format(date);
  }

  static String datetime(DateTime date) {
    return _datetimeFormat.format(date);
  }

  static String date(DateTime date) {
    return _dateFormat.format(date);
  }

  static String time(DateTime time) {
    return _timeFormat.format(time);
  }
}
