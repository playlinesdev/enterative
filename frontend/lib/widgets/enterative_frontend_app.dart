import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/notifiers/affiliate/affiliate_form.dart';
import 'package:frontend/notifiers/network_notifier.dart';
import 'package:frontend/pages/affiliate_page.dart';
import 'package:frontend/services/enterative_network.dart';
import 'package:provider/provider.dart';

class EnterativeFrontendApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return FutureBuilder<String>(
      future: rootBundle.loadString('assets/settings.yaml'),
      builder: (context, snapshot) {
        if (snapshot.connectionState != ConnectionState.done) return Center(child: CircularProgressIndicator());
        return MultiProvider(
          providers: [
            ChangeNotifierProvider(create: (context) => NetworkNotifier()),
            ChangeNotifierProvider(create: (context) => AffiliateForm()),
          ],
          builder: (context, child) => MaterialApp(
            theme: ThemeData.light().copyWith(scaffoldBackgroundColor: Colors.white),
            initialRoute: AffiliatePage.routeName,
            onGenerateRoute: (settings) {
              print(settings.arguments);
            },
            onGenerateInitialRoutes: (initialRoute) {
              print(initialRoute);
              return [
                MaterialPageRoute(
                  builder: (ctx) => Consumer<NetworkNotifier>(builder: (context, value, child) {
                    EnterativeNetwork.instance.initialize(snapshot.data!);
                    if (NetworkNotifier.of(context).status == NetworkStatus.pending)
                      EnterativeNetwork.instance.ping((netStatus) {
                        NetworkNotifier.of(context, listen: false).setNetworkStatus(netStatus);
                      });
                    return AffiliatePage(initialRoute);
                  }),
                ),
              ];
            },
          ),
        );
      },
    );
  }
}
