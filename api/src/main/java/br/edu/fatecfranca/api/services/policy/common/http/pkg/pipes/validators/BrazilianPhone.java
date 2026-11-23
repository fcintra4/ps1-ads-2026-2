package br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BrazilianPhoneValidator.class)
public @interface BrazilianPhone {
    String message() default "must be a valid Brazilian phone number with area code";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
