import 'dart:typed_data';

import 'package:frontend/notifiers/network_notifier.dart';
import 'package:yaml/yaml.dart';
import 'package:dio/dio.dart';

class EnterativeNetwork {
  late YamlMap _settingsMap;
  static EnterativeNetwork? _instance;
  static EnterativeNetwork get instance {
    if (_instance == null) _instance = EnterativeNetwork._();
    return _instance!;
  }

  EnterativeNetwork._();
  void initialize(String settingsString) {
    this._settingsMap = loadYaml(settingsString);
  }

  void ping(void Function(NetworkStatus) onAfterPing) async {
    _dioObject.get('/').catchError((error) {
      onAfterPing(NetworkStatus.offline);
    }).then((value) => onAfterPing(NetworkStatus.ready));
  }

  Future<void> uploadFile(String fileName, Uint8List fileBytes) async {
    var formData = FormData.fromMap({'file': MultipartFile.fromBytes(fileBytes, filename: 'enterative_logo.png')});
    try {
      var response = await _dioObject.post('/upload', data: formData);
      print(response.data);
    } catch (e) {
      print(e);
    }
  }

  Dio get _dioObject {
    var url = _settingsMap['api']['url'];
    print(url);
    return Dio(BaseOptions(baseUrl: url));
  }
}
