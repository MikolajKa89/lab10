package com.example.lab10.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoSpacesValidator implements ConstraintValidator<NoSpaces, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Jeśli pole jest puste (null), to inna adnotacja (@NotNull) się tym zajmie.
        // Tutaj uznajemy to za poprawne, żeby nie dublować błędów.
        if (value == null) {
            return true;
        }

        // BŁĄD BYŁ TUTAJ:
        // Musimy zwrócić TRUE, jeśli hasło NIE ZAWIERA (!contains) spacji.
        // Wcześniej zwracałeś wynik metody contains, czyli false (bo spacji nie było), co oznaczało błąd walidacji.
        return !value.contains(" ");
    }
}