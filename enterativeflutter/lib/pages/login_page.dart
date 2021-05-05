import 'package:enterativeflutter/changenotifiers/user_notifier.dart';
import 'package:enterativeflutter/helper/form_validator.dart';
import 'package:enterativeflutter/helper/user_config.dart';
import 'package:enterativeflutter/model/user.dart';
import 'package:enterativeflutter/webservice/enterative_services.dart';
import 'package:enterativeflutter/widgets/password_field.dart';
import 'package:enterativeflutter/widgets/widget_factory.dart';
import 'package:flushbar/flushbar_helper.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class Login extends StatefulWidget {
  @override
  LoginState createState() => LoginState();
}

class LoginState extends State<Login> {
  var loginController = TextEditingController();
  var passwordController = TextEditingController();

  final _formKey = GlobalKey<FormState>();
  var _loading = false;

  @override
  Widget build(BuildContext context) {
    if (this._loading) {
      return Scaffold(
        body: WidgetFactory.createLoading(),
      );
    }

    return FutureBuilder(
      future: UserConfig.get("login"),
      builder: (context, response) {
        if (response.connectionState != ConnectionState.done) {
          return WidgetFactory.createLoading();
        }

        this.loginController.text = response.data ?? "";

        return Form(
          key: _formKey,
          child: Scaffold(
            body: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Center(
                  child: Image(
                    image: AssetImage("assets/logo.png"),
                    height: 100,
                  ),
                ),
                Container(
                  padding: const EdgeInsets.symmetric(vertical: 15),
                  margin: const EdgeInsets.symmetric(horizontal: 15),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(30),
                    color: Theme.of(context).backgroundColor,
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      WidgetFactory.createTextField(
                        "Usuário",
                        hintText: "Preencha seu usuário",
                        prefixIcon: Icons.alternate_email,
                        controller: this.loginController,
                        validator: FormValidator.emptyValidator,
                      ),
                      PasswordField(
                        label: "Senha",
                        hintText: "Preencha sua senha",
                        controller: this.passwordController,
                        prefixIcon: Icons.lock_open,
                        keyboardType: TextInputType.text,
                        validator: FormValidator.emptyValidator,
                        autofocus: this.loginController.text.isNotEmpty,
                      ),
                      Container(
                        margin: const EdgeInsets.only(top: 15.0),
                        padding: const EdgeInsets.only(left: 15.0, right: 15.0),
                        child: Row(
                          children: <Widget>[
                            Expanded(
                              child: FlatButton(
                                padding: EdgeInsets.symmetric(vertical: 10.0),
                                shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(30.0)),
                                splashColor: Theme.of(context).primaryColor,
                                color: Theme.of(context).primaryColor,
                                child: Row(
                                  children: <Widget>[
                                    Padding(
                                      padding:
                                          const EdgeInsets.only(left: 20.0),
                                      child: Text(
                                        "Entrar",
                                        style: TextStyle(color: Colors.white),
                                      ),
                                    ),
                                    Expanded(
                                      child: Container(),
                                    ),
                                    Transform.translate(
                                      offset: Offset(-15.0, 0.0),
                                      child: Container(
                                        width: 50,
                                        decoration: BoxDecoration(
                                          borderRadius:
                                              BorderRadius.circular(28),
                                          color: Colors.white,
                                        ),
                                        child: Icon(
                                          Icons.arrow_forward,
                                          color: Theme.of(context).primaryColor,
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                                onPressed: this.onLoginPressed,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  onLoginPressed() async {
    if (this._formKey.currentState.validate()) {
      var login = this.loginController.text;
      var password = this.passwordController.text;

      setState(() {
        this._loading = true;
      });

      User user = User(login: login.trim(), password: password.trim());
      bool success = await EnterativeServices.instance.doLogin(context, user);
      if (success == true) {
        await UserConfig.set("login", login);
        Provider.of<UserNotifier>(context, listen: false).doLogin(user: user);
      } else if (success == false) {
        FlushbarHelper.createError(message: "Login and/or password incorrect!")
            .show(context);
      } else {
        this.loginController.text = "";
        this.passwordController.text = "";
      }

      setState(() {
        this._loading = false;
      });
    }
  }

  @override
  void dispose() {
    super.dispose();
    this.passwordController.dispose();
    this.loginController.dispose();
  }
}
