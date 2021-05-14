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
    netObject.get('/').catchError((error) {
      onAfterPing(NetworkStatus.offline);
    }).then((value) {
      onAfterPing(NetworkStatus.ready);
    });
  }

  static FormData getFormData(
      {Map<String, String>? parameters, Uint8List? file, String? fileParamName, String? fileName}) {
    Map<String, dynamic> map = Map.from(parameters!);
    if (file != null) {
      if (fileParamName == null) throw new Exception('If a file is provided, fileParamName is mandatory');
      map.putIfAbsent(
          fileParamName, () => MultipartFile.fromBytes(file, filename: fileName != null ? fileName : fileParamName));
    }
    return FormData.fromMap(map);
  }

  Future<void> uploadFile(String fileName, Uint8List fileBytes) async {
    var formData = FormData.fromMap({'file': MultipartFile.fromBytes(fileBytes, filename: 'enterative_logo.png')});
    try {
      var response = await netObject.post('/upload', data: formData);
      print(response.data);
    } catch (e) {
      print(e);
    }
  }

  Dio get emptyNetObject => Dio();

  Dio get netObject {
    // print(_settingsMap['environment']);
    // if (_settingsMap['environment'] == 'dev') return emptyNetObject;
    var url = _settingsMap['api']['url'];
    print(url);
    return Dio(BaseOptions(baseUrl: url));
  }
}
