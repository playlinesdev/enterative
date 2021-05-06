import 'package:flutter/cupertino.dart';
import 'package:provider/provider.dart';

class NetworkNotifier with ChangeNotifier {
  NetworkStatus _status = NetworkStatus.pending;
  NetworkStatus get status => _status;

  void setNetworkStatus(NetworkStatus isReady) {
    _status = isReady;
    notifyListeners();
  }

  static NetworkNotifier of(BuildContext context, {bool listen = true}) {
    return Provider.of<NetworkNotifier>(context, listen: listen);
  }
}

enum NetworkStatus { pending, offline, ready }
