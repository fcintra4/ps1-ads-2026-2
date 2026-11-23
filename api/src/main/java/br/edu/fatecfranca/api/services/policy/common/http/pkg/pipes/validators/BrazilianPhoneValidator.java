package br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BrazilianPhoneValidator implements ConstraintValidator<BrazilianPhone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        String normalized = value.trim();
        if (!normalized.matches("[+0-9() .-]+")) {
            return false;
        }

        String digits = normalized.replaceAll("\\D", "");
        if (digits.startsWith("55") && digits.length() > 11) {
            digits = digits.substring(2);
        }
        if (digits.length() != 10 && digits.length() != 11) {
            return false;
        }

        int areaCode = Integer.parseInt(digits.substring(0, 2));
        if (areaCode < 11 || areaCode > 99 || areaCode % 10 == 0) {
            return false;
        }

        return digits.length() == 10
                ? digits.charAt(2) >= '2' && digits.charAt(2) <= '5'
                : digits.charAt(2) == '9';
    }
}
