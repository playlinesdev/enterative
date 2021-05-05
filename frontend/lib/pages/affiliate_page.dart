import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/affiliate_info.dart';
import 'package:frontend/pages/widgets/affiliated_logo.dart';
import 'package:frontend/pages/widgets/be_a_partner.dart';
import 'package:frontend/pages/widgets/btn_submit.dart';
import 'package:frontend/pages/widgets/captcha.dart';
import 'package:frontend/pages/widgets/company_address.dart';
import 'package:frontend/pages/widgets/enterative_logo.dart';
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

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<String>(
      future: rootBundle.loadString('assets/settings.yaml', cache: false),
      builder: (context, snapshot) {
        if (snapshot.connectionState != ConnectionState.done) return LinearProgressIndicator();
        var afInfo = AffiliateInfo.getFromRoute(snapshot.data!, widget.affiliateId);
        if (afInfo == null) return notFoundWidget();
        affiliateInfo = afInfo;
        return mainWidget();
      },
    );
  }

  Widget notFoundWidget() {
    return Material(child: Center(child: Text('Não encontrado')));
  }

  Widget mainWidget() {
    return Scaffold(
      body: Padding(
        padding: EdgeInsets.symmetric(
          horizontal: Tools.screenWidth(context, 20),
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
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          MentesQuePensamLogo(50),
          AffiliatedLogo(height: 130, imgPath: affiliateInfo.imgPath),
        ],
      ),
    );
  }

  Widget formWidget() {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Form(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Responsive(children: [
              Div(child: razaoSocialWidget(), colL: 4, colM: 4, colS: 4),
            ]),
            Responsive(children: [
              Div(child: fantasiaWidget(), colL: 4, colM: 4, colS: 4),
            ]),
            SizedBox(height: 20),
            Responsive(children: [
              Div(child: ShopType(), colL: 4, colM: 4, colS: 4),
            ]),
            SizedBox(height: 20),
            Responsive(children: [
              Div(child: cnpjWidget(), colL: 4, colM: 4, colS: 4),
              Div(child: inscricaoEstadualWidget(), colL: 4, colM: 4, colS: 4, offsetL: 2, offsetM: 2),
            ]),
            Responsive(children: [
              Div(child: inscricaoMunicipalWidget(), colL: 4, colM: 4, colS: 4),
            ]),
            Responsive(children: [
              Div(child: nomeResponsavel(), colL: 4, colM: 4, colS: 4),
            ]),
            Responsive(children: [
              Div(child: cpfWidget(), colL: 4, colM: 4, colS: 4),
            ]),
            Responsive(children: [
              Div(child: emailResponsavel(), colL: 4, colM: 4, colS: 4),
              Div(child: linkWidget(), colL: 4, colM: 4, colS: 4, offsetL: 2, offsetM: 2),
            ]),
            Responsive(children: [
              Div(child: ramoAtividadeWidget(), colL: 4, colM: 4, colS: 4),
            ]),
            Responsive(children: [
              Div(child: CompanyAddress(), colL: 4, colM: 4, colS: 4),
            ]),
            Responsive(children: [
              Div(child: Phones(), colL: 4, colM: 4, colS: 4),
            ]),
            Responsive(children: [
              Div(child: FacadePicture(), colL: 4, colM: 4, colS: 4),
            ]),
            Responsive(children: [
              Div(child: Captcha(), colL: 4, colM: 4, colS: 4),
            ]),
            Responsive(children: [
              Div(child: BtnSubmit(), colL: 4, colM: 4, colS: 4),
            ]),
          ],
        ),
      ),
    );
  }

  Widget razaoSocialWidget() {
    return Container(child: EnterativeInput('Razão Social'));
  }

  Widget fantasiaWidget() {
    return Container(child: EnterativeInput('Fantasia'));
  }

  Widget cnpjWidget() {
    return Container(child: EnterativeInput('Cnpj'));
  }

  Widget inscricaoEstadualWidget() {
    return Container(child: EnterativeInput('Inscrição Estadual'));
  }

  Widget inscricaoMunicipalWidget() {
    return Container(child: EnterativeInput('Inscrição Municipal'));
  }

  Widget nomeResponsavel() {
    return Container(child: EnterativeInput('Nome Completo do Responsável'));
  }

  Widget cpfWidget() {
    return Container(child: EnterativeInput('CPF'));
  }

  Widget emailResponsavel() {
    return Container(child: EnterativeInput('E-mail do responsável'));
  }

  Widget linkWidget() {
    return Container(child: EnterativeInput('Link'));
  }

  Widget ramoAtividadeWidget() {
    return Container(child: EnterativeInput('Ramo de Atividade'));
  }
}
