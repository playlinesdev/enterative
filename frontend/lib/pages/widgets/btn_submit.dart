import 'package:flutter/material.dart';
import 'package:frontend/notifiers/affiliate/form.dart';

class BtnSubmit extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var form = AffiliateForm.of(context);
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: ElevatedButton(
          child: Text('Enviar'),
          onPressed: () {
            print(form.razaoSocial);
            print(form.nomeResponsavel);
            print(form.ramoAtividade);
          },
        ),
      ),
    );
  }
}
