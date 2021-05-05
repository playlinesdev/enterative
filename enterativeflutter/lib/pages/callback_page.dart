import 'dart:async';

import 'package:enterativeflutter/changenotifiers/user_notifier.dart';
import 'package:enterativeflutter/helper/value_formatter.dart';
import 'package:enterativeflutter/model/callback_state.dart';
import 'package:enterativeflutter/model/user.dart';
import 'package:enterativeflutter/pages/saleorder_page.dart';
import 'package:enterativeflutter/webservice/enterative_services.dart';
import 'package:enterativeflutter/widgets/appbar.dart';
import 'package:enterativeflutter/widgets/widget_factory.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class CallbackPage extends StatefulWidget {
  final int orderID;

  const CallbackPage({Key key, this.orderID}) : super(key: key);

  @override
  _CallbackPageState createState() => _CallbackPageState();
}

class _CallbackPageState extends State<CallbackPage> {
  final StreamController<CallbackState> _controller =
      StreamController<CallbackState>();
  Timer timer;
  User user;

  @override
  void initState() {
    super.initState();
    this.timer = Timer.periodic(
        Duration(
          seconds: 5,
        ),
        (t) => _checkOrder());
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    this.user = Provider.of<UserNotifier>(context);
  }

  _checkOrder() async {
    if (this.user == null) {
      return;
    }

    bool isDone = await EnterativeServices.instance
        .callbackStatus(context, user, widget.orderID);
    if (isDone == true) {
      _controller.add(CallbackState.done());
      this.timer.cancel();
    } else {
      _controller.add(CallbackState.loading(lastUpdate: DateTime.now()));
    }
  }

  @override
  Widget build(BuildContext context) {
    return StreamBuilder<CallbackState>(
      stream: _controller.stream,
      initialData: CallbackState.loading(lastUpdate: DateTime.now()),
      builder: (context, snapshot) {
        final state = snapshot.data;
        if (state.loading) {
          return SafeArea(
            child: Scaffold(
              appBar: EnterativeAppbar.build(context, showCart: false),
              body: _buildAwaiting(state.lastUpdate),
            ),
          );
        } else {
          return SaleOrderPage(orderID: widget.orderID);
        }
      },
    );
  }

  Widget _buildAwaiting(final DateTime lastUpdate) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        WidgetFactory.createLoading(),
        Padding(
          padding: EdgeInsets.all(15.0),
          child: WidgetFactory.createStaticText(
            "Aguarde enquanto seu pedido está sendo processado.",
            fontSize: 20,
            fontWeight: FontWeight.bold,
            textAlign: TextAlign.center,
          ),
        ),
        Padding(
          padding: EdgeInsets.all(15.0),
          child: WidgetFactory.createStaticText(
            "Não feche o aplicativo e/ou esta tela enquanto estiver nesta tela.",
            fontSize: 20,
            fontWeight: FontWeight.bold,
            textAlign: TextAlign.center,
          ),
        ),
        Padding(
          padding: EdgeInsets.all(15.0),
          child: WidgetFactory.createStaticText(
            "O número do seu pedido é [${widget.orderID}]",
            fontSize: 20,
            fontWeight: FontWeight.bold,
            textAlign: TextAlign.center,
          ),
        ),
        if (lastUpdate != null)
          Padding(
            padding: EdgeInsets.all(15.0),
            child: WidgetFactory.createStaticText(
              "Última atualização: ${ValueFormatter.time(lastUpdate)}",
              fontSize: 20,
              fontWeight: FontWeight.bold,
              textAlign: TextAlign.center,
            ),
          ),
      ],
    );
  }

  @override
  void dispose() {
    this.timer?.cancel();
    super.dispose();
  }
}
