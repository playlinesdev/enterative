import 'package:flutter/material.dart';
import 'package:frontend/notifiers/affiliate/affiliate_form.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:responsive_ui/responsive_ui.dart';

class CompanyAddress extends StatelessWidget {
  final TextStyle labelStyle = GoogleFonts.roboto(
    fontWeight: FontWeight.bold,
    color: Color(0xff555555),
    fontSize: 15,
  );
  final TextStyle miniLabelStyle = GoogleFonts.roboto(fontSize: 13);
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(vertical: 20),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              Text('Endereço da Empresa', style: labelStyle),
              SizedBox(width: 5),
              Text('*', style: labelStyle.copyWith(color: Colors.red))
            ],
          ),
          SizedBox(height: 10),
          fieldsWidgets(context),
        ],
      ),
    );
  }

  Widget fieldsWidgets(context) {
    var form = AffiliateForm.of(context, listen: false);
    return Responsive(children: [
      Responsive(children: [
        Div(child: customInput('Endereço', onChange: (typed) => form.enderecoRua = typed)),
      ]),
      Responsive(children: [
        Div(child: customInput('Bairro', onChange: (typed) => form.enderecoBairro = typed)),
      ]),
      Responsive(children: [
        Div(child: customInput('Cidade', onChange: (typed) => form.enderecoCidade = typed), colL: 4, colM: 4),
        Div(
            child: customInput('Estado', onChange: (typed) => form.enderecoEstado = typed),
            colL: 4,
            colM: 4,
            offsetL: 1,
            offsetM: 1),
      ]),
      Responsive(children: [
        Div(child: customInput('CEP/Código Postal', onChange: (typed) => form.enderecoCep = typed), colL: 4, colM: 4),
        Div(
            child: customInput('País', onChange: (typed) => form.enderecoPais = typed),
            colL: 4,
            colM: 4,
            offsetL: 1,
            offsetM: 1),
      ]),
    ]);
  }

  Widget customInput(String label, {required onChange}) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          height: 28,
          child: TextFormField(
            onChanged: onChange,
            decoration: InputDecoration(
              border: OutlineInputBorder(),
            ),
          ),
        ),
        SizedBox(height: 5),
        Text(label, style: miniLabelStyle),
      ],
    );
  }
}
