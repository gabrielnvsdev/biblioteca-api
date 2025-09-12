package com.desafio.biblioteca_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<Cpf, String>{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        String cpf = value.replaceAll("\\D", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int d1 = 0, d2 = 0;
            for (int i = 0; i < 9; i++) {
                int digito = Character.getNumericValue(cpf.charAt(i));
                d1 += (10 - i) * digito;
                d2 += (11 - i) * digito;
            }

            int resto = d1 % 11;
            int check1 = (resto < 2) ? 0 : 11 - resto;
            d2 += check1 * 2;

            resto = d2 % 11;
            int check2 = (resto < 2) ? 0 : 11 - resto;

            return check1 == Character.getNumericValue(cpf.charAt(9)) &&
                    check2 == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }
}
