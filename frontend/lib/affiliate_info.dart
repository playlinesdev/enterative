import 'package:yaml/yaml.dart';

class AffiliateInfo {
  late String imgPath;

  AffiliateInfo._();

  static AffiliateInfo? getFromRoute(String settingsYaml, String affiliateId) {
    var doc = loadYaml(settingsYaml);
    var id = affiliateId.replaceAll('/', '');
    var info = doc['affiliates'][id];
    if (info == null) return null;
    var affiliateInfo = AffiliateInfo._();
    affiliateInfo.imgPath = info['img']!;

    return affiliateInfo;
  }
}
