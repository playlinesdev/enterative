import 'package:flutter/material.dart';
import 'package:frontend/services/enterative_network.dart';

class BtnSubmit extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
      child: Text('Teste'),
      onPressed: () {
        EnterativeNetwork.instance.ping();
      },
    );
  }
}
