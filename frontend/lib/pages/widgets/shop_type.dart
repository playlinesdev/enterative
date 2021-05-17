import 'package:flutter/material.dart';
import 'package:frontend/notifiers/affiliate/affiliate_form.dart';
import 'package:frontend/widgets/radio_group.dart';

class ShopType extends StatelessWidget {
  final labels = ['Física', 'Virtual', 'Ambas'];
  @override
  Widget build(BuildContext context) {
    var form = AffiliateForm.of(context, listen: false);
    return Container(
      child: RadioGroup(
        label: 'Tipo de Loja',
        possibleLabels: labels,
        onIndexSelected: (i) {
          form.tipoLoja = labels[i as int];
        },
      ),
    );
  }
}
