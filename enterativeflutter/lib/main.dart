import 'package:enterativeflutter/changenotifiers/user_notifier.dart';
import 'package:enterativeflutter/pages/login_page.dart';
import 'package:enterativeflutter/pages/store_page.dart';
import 'package:enterativeflutter/widgets/theme_data.dart';
import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:provider/provider.dart';

void main() => runApp(EnterativeApp());

class EnterativeApp extends StatelessWidget {
  static bool checked = false;

  @override
  Widget build(BuildContext context) {
    this.checkPermissions();

    return MultiProvider(
      providers: [
        ChangeNotifierProvider<UserNotifier>(
          create: (_) => UserNotifier(),
        ),
      ],
      child: Consumer<UserNotifier>(
        builder: (_, user, child) {
          return MaterialApp(
            title: 'Enterative',
            theme: EnterativeThemeData.build(context),
            home: user.login != null ? StorePage() : Login(),
          );
        },
      ),
    );
  }

  void checkPermissions() async {
    if (!checked) {
      checked = true;

      PermissionStatus permission = await PermissionHandler().checkPermissionStatus(PermissionGroup.camera);

      if (permission != PermissionStatus.granted) {
        await PermissionHandler().requestPermissions([PermissionGroup.camera]);
      }
    }
  }
}