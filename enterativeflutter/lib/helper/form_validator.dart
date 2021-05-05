class FormValidator {
  FormValidator._();

  static final String Function(String) emptyValidator = (String value) {
      if (value.isEmpty) {
        return 'Valor não deve ser vazio!';
      }
      return null;
  };

  static final String Function(String) greaterZeroValidator = (String value) {
    if (value.isEmpty || double.tryParse(value) == null) {
      return 'Valor não deve ser um valor vazio ou um número inválido!';
    } else if (double.parse(value) <= 0) {
      return 'Valor deve ser maior que zero!';
    }

    return null;
  };
}