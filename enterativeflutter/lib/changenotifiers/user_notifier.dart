import 'package:enterativeflutter/model/user.dart';
import 'package:flutter/material.dart';

class UserNotifier extends User with ChangeNotifier {

  doLogin({String login, String password, User user}) {
    if (user == null) {
      this.login = login;
      this.password = password;
    } else {
      this.login = user.login;
      this.password = user.password;
    }

    notifyListeners();
  }

  logout() {
    this.login = null;
    this.password = null;
    notifyListeners();
  }
}