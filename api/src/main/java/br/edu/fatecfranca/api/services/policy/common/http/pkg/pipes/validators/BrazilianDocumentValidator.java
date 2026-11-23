package br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class BrazilianDocumentValidator implements ConstraintValidator<BrazilianDocument, String> {

    private static final Pattern CPF = Pattern.compile("\\d{11}");
    private static final Pattern FORMATTED_CPF = Pattern.compile("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    private static final Pattern CNPJ = Pattern.compile("[A-Z0-9]{12}\\d{2}");
    private static final Pattern FORMATTED_CNPJ =
            Pattern.compile("[A-Z0-9]{2}\\.[A-Z0-9]{3}\\.[A-Z0-9]{3}/[A-Z0-9]{4}-\\d{2}");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        String normalized = value.trim().toUpperCase();
        if (FORMATTED_CPF.matcher(normalized).matches()) {
            normalized = normalized.replaceAll("[.-]", "");
        } else if (FORMATTED_CNPJ.matcher(normalized).matches()) {
            normalized = normalized.replaceAll("[./-]", "");
        }

        if (CPF.matcher(normalized).matches()) {
            return isValidCpf(normalized);
        }
        return CNPJ.matcher(normalized).matches() && isValidCnpj(normalized);
    }

    private boolean isValidCpf(String cpf) {
        if (hasRepeatedCharacters(cpf)) {
            return false;
        }
        int firstDigit = calculateCpfDigit(cpf.substring(0, 9));
        int secondDigit = calculateCpfDigit(cpf.substring(0, 10));
        return cpf.charAt(9) - '0' == firstDigit && cpf.charAt(10) - '0' == secondDigit;
    }

    private boolean isValidCnpj(String cnpj) {
        if (hasRepeatedCharacters(cnpj)) {
            return false;
        }
        int firstDigit = calculateCnpjDigit(cnpj.substring(0, 12));
        int secondDigit = calculateCnpjDigit(cnpj.substring(0, 13));
        return cnpj.charAt(12) - '0' == firstDigit && cnpj.charAt(13) - '0' == secondDigit;
    }

    private int calculateCpfDigit(String value) {
        int sum = 0;
        for (int i = 0; i < value.length(); i++) {
            sum += (value.charAt(i) - '0') * (value.length() + 1 - i);
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    private int calculateCnpjDigit(String value) {
        int sum = 0;
        int weight = 2;
        for (int i = value.length() - 1; i >= 0; i--) {
            sum += (value.charAt(i) - '0') * weight;
            weight = weight == 9 ? 2 : weight + 1;
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    private boolean hasRepeatedCharacters(String value) {
        return value.chars().allMatch(character -> character == value.charAt(0));
    }
}
