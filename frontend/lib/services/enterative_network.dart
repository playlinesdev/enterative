import 'package:dio/dio.dart';
import 'package:yaml/yaml.dart';

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

  void ping() async {
    var response = await _dioObject.get('/');
    print(response);
  }

  Dio get _dioObject {
    var url = _settingsMap['api']['url'];
    print(url);
    return Dio(BaseOptions(baseUrl: url));
  }
}
