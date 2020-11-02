package com.github.egoettelmann.sample.auth.api.core.validation;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                // Min length: 8, Max length: 30
                new LengthRule(8, 30),
                // One uppercase letter
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                // One lowercase letter
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                // One digit
                new CharacterRule(EnglishCharacterData.Digit, 1),
                // One special character
                new CharacterRule(EnglishCharacterData.Special, 1),
                // No whitespace
                new WhitespaceRule()
        ));

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                String.join(",", validator.getMessages(result))
        ).addConstraintViolation();
        return false;
    }
}
