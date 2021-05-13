import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/affiliate_info.dart';
import 'package:frontend/notifiers/affiliate/affiliate_form.dart';
import 'package:frontend/pages/not_found_affiliate_page.dart';
import 'package:frontend/pages/widgets/affiliated_logo.dart';
import 'package:frontend/pages/widgets/be_a_partner.dart';
import 'package:frontend/pages/widgets/btn_submit.dart';
import 'package:frontend/pages/widgets/captcha.dart';
import 'package:frontend/pages/widgets/company_address.dart';
import 'package:frontend/pages/widgets/facade_pitcture.dart';
import 'package:frontend/pages/widgets/mentes_que_pensam_logo.dart';
import 'package:frontend/pages/widgets/phones.dart';
import 'package:frontend/pages/widgets/shop_type.dart';
import 'package:frontend/widgets/enterative_input.dart';
import 'package:frontend/utils/screen.dart';
import 'package:responsive_ui/responsive_ui.dart';

class AffiliatePage extends StatefulWidget {
  static String routeName = '/affiliate';
  final String affiliateId;

  AffiliatePage(this.affiliateId);

  @override
  _AffiliatePageState createState() => _AffiliatePageState();
}

class _AffiliatePageState extends State<AffiliatePage> {
  late AffiliateInfo affiliateInfo;
  final _formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<String>(
      future: rootBundle.loadString('assets/settings.yaml', cache: false),
      builder: (context, snapshot) {
        if (snapshot.connectionState != ConnectionState.done)
          return Column(
            children: [LinearProgressIndicator()],
          );
        var afInfo = AffiliateInfo.getFromRoute(snapshot.data!, widget.affiliateId);
        if (afInfo == null) return NotFoundAffiliatePage();
        affiliateInfo = afInfo;
        return mainWidget();
      },
    );
  }

  Widget mainWidget() {
    return Scaffold(
      body: Padding(
        padding: EdgeInsets.symmetric(
          horizontal: Tools.screenWidth(context, 10),
          vertical: Tools.screenHeight(context, 5),
        ),
        child: ListView.separated(
          itemBuilder: (ctx, i) => makeWidgetByOrder(i) ?? Container(),
          separatorBuilder: (ctx, i) => Divider(height: 2),
          itemCount: 4,
        ),
      ),
    );
  }

  Widget? makeWidgetByOrder(int order) {
    Map<int, Widget> widgetsByOrder = {
      0: logosWidget(),
      1: BeAPartner(),
      2: formWidget(),
    };
    return widgetsByOrder[order];
  }

  Widget logosWidget() {
    return Container(
      height: 150,
      alignment: Alignment.center,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          MentesQuePensamLogo(45),
          Spacer(),
          AffiliatedLogo(height: 130, imgPath: affiliateInfo.imgPath),
          Spacer(),
        ],
      ),
    );
  }

  Widget formWidget() {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Form(
        key: _formKey,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Responsive(children: [
              Div(child: razaoSocialWidget(), colL: 4, colM: 4, colS: 4),
            ]),
            Responsive(children: [Div(child: fantasiaWidget(), colL: 4, colM: 4, colS: 4)]),
            SizedBox(height: 20),
            Responsive(children: [Div(child: ShopType(), colL: 4, colM: 4, colS: 4)]),
            SizedBox(height: 20),
            Responsive(children: [
              Div(child: cnpjWidget(), colL: 4, colM: 4, colS: 4),
              Div(child: inscricaoEstadualWidget(), colL: 4, colM: 4, colS: 4, offsetL: 2, offsetM: 2)
            ]),
            Responsive(children: [Div(child: inscricaoMunicipalWidget(), colL: 4, colM: 4, colS: 4)]),
            Responsive(children: [Div(child: nomeResponsavel(), colL: 4, colM: 4, colS: 4)]),
            Responsive(children: [Div(child: cpfWidget(), colL: 4, colM: 4, colS: 4)]),
            Responsive(children: [
              Div(child: emailResponsavel(), colL: 4, colM: 4, colS: 4),
              Div(child: linkWidget(), colL: 4, colM: 4, colS: 4, offsetL: 2, offsetM: 2),
            ]),
            Responsive(children: [Div(child: ramoAtividadeWidget(), colL: 4, colM: 4, colS: 4)]),
            Responsive(children: [Div(child: CompanyAddress(), colL: 4, colM: 4, colS: 4)]),
            Responsive(children: [Div(child: Phones(), colL: 4, colM: 4, colS: 4)]),
            Responsive(children: [Div(child: FacadePicture(), colL: 4, colM: 4, colS: 4)]),
            Responsive(children: [Div(child: Captcha(), colL: 4, colM: 4, colS: 4)]),
            Responsive(children: [
              Div(
                child: BtnSubmit(formKey: _formKey),
                colL: 4,
                colM: 4,
                colS: 4,
                offsetL: 4,
                offsetM: 4,
              )
            ]),
          ],
        ),
      ),
    );
  }

  InputValidationFunction nonNullabelValidation = (typed) => typed!.isEmpty ? 'Campo obrigatório' : null;

  Widget razaoSocialWidget() {
    return Container(
        child: EnterativeInput('Razão Social',
            validations: [nonNullabelValidation],
            onChanged: (value) => AffiliateForm.of(context, listen: false).razaoSocial = value));
  }

  Widget fantasiaWidget() {
    return Container(
        child: EnterativeInput('Fantasia',
            validations: [nonNullabelValidation],
            onChanged: (value) => AffiliateForm.of(context, listen: false).fantasia = value));
  }

  Widget cnpjWidget() {
    return Container(
        child: EnterativeInput('Cnpj',
            validations: [nonNullabelValidation],
            onChanged: (value) => AffiliateForm.of(context, listen: false).cnpj = value));
  }

  Widget inscricaoEstadualWidget() {
    return Container(
        child: EnterativeInput('Inscrição Estadual',
            validations: [nonNullabelValidation],
            onChanged: (value) => AffiliateForm.of(context, listen: false).inscricaoEstadual = value));
  }

  Widget inscricaoMunicipalWidget() {
    return Container(
        child: EnterativeInput('Inscrição Municipal',
            validations: [nonNullabelValidation],
            onChanged: (value) => AffiliateForm.of(context, listen: false).inscricaoMunicipal = value));
  }

  Widget nomeResponsavel() {
    return Container(
        child: EnterativeInput('Nome Completo do Responsável',
            validations: [nonNullabelValidation],
            onChanged: (value) => AffiliateForm.of(context, listen: false).nomeResponsavel = value));
  }

  Widget cpfWidget() {
    return Container(
        child: EnterativeInput('CPF',
            validations: [nonNullabelValidation],
            onChanged: (value) => AffiliateForm.of(context, listen: false).cpf = value));
  }

  Widget emailResponsavel() {
    return Container(
        child: EnterativeInput('E-mail do responsável',
            validations: [nonNullabelValidation],
            onChanged: (value) => AffiliateForm.of(context, listen: false).emailResponsavel = value));
  }

  Widget linkWidget() {
    return Container(
        child: EnterativeInput('Link',
            validations: [nonNullabelValidation],
            onChanged: (value) => AffiliateForm.of(context, listen: false).link = value));
  }

  Widget ramoAtividadeWidget() {
    return Container(
        child: EnterativeInput('Ramo de Atividade',
            validations: [nonNullabelValidation],
            onChanged: (value) => AffiliateForm.of(context, listen: false).ramoAtividade = value));
  }
}
