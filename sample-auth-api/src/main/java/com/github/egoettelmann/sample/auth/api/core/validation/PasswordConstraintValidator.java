package com.github.egoettelmann.sample.auth.api.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

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
