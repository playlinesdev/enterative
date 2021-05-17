import 'package:flutter/material.dart';
import 'package:frontend/notifiers/affiliate/affiliate_form.dart';
import 'package:frontend/services/enterative_network.dart';

class BtnSubmit extends StatelessWidget {
  final GlobalKey<FormState> formKey;
  BtnSubmit({required this.formKey});

  @override
  Widget build(BuildContext context) {
    var form = AffiliateForm.of(context);
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: ElevatedButton(
          child: Text('Enviar'),
          onPressed: form.isReady
              ? () {
                  uploadAffiliateInfo(context);
                }
              : null,
        ),
      ),
    );
  }

  Future<void> uploadAffiliateInfo(context) async {
    var isValid = formKey.currentState!.validate();
    if (isValid) {
      var form = AffiliateForm.of(context, listen: false);
      var formData = EnterativeNetwork.getFormData(parameters: {
        'razaoSocial': form.razaoSocial,
        'fantasia': form.fantasia,
        'tipoLoja': form.tipoLoja,
        'cnpj': form.cnpj,
        'inscricaoEstadual': form.inscricaoEstadual,
        'inscricaoMunicipal': form.inscricaoMunicipal,
        'cpf': form.cpf,
        'nomeResponsavel': form.nomeResponsavel,
        'emailResponsavel': form.emailResponsavel,
        'link': form.link,
        'ramoAtividade': form.ramoAtividade,
        'enderecoRua': form.enderecoRua,
        'enderecoBairro': form.enderecoBairro,
        'enderecoCidade': form.enderecoCidade,
        'enderecoEstado': form.enderecoEstado,
        'enderecoCep': form.enderecoCep,
        'enderecoPais': form.enderecoPais,
      }, file: form.facadePicture, fileParamName: 'facadePicture');
      print(formData);
      try {
        var response = await EnterativeNetwork.instance.netObject.post(
          '/affiliate/register',
          data: formData,
        );
        print(response);
      } catch (e) {
        print(e);
      }
    }
  }
}
