import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/pages/affiliate_page.dart';
import 'package:frontend/services/enterative_network.dart';

class EnterativeFrontendApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return FutureBuilder<String>(
      future: rootBundle.loadString('assets/settings.yaml'),
      builder: (context, snapshot) {
        if (snapshot.connectionState != ConnectionState.done) return Center(child: CircularProgressIndicator());
        EnterativeNetwork.instance.initialize(snapshot.data!);
        EnterativeNetwork.instance.ping();

        return MaterialApp(
          theme: ThemeData.light().copyWith(scaffoldBackgroundColor: Colors.white),
          initialRoute: AffiliatePage.routeName,
          onGenerateRoute: (settings) {
            print(settings.arguments);
          },
          onGenerateInitialRoutes: (initialRoute) {
            print(initialRoute);
            return [
              MaterialPageRoute(builder: (ctx) => AffiliatePage(initialRoute)),
            ];
          },
        );
      },
    );
  }
}
