class Optional {
  final dynamic value;

  Optional._({this.value});

  static Optional of(dynamic value) {
    return Optional._(value: value);
  }

  dynamic orElse(dynamic defaultValue) {
    return value == null ? defaultValue : value;
  }
}