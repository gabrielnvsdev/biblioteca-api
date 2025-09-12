package com.desafio.biblioteca_api.service;

import com.desafio.biblioteca_api.validation.CpfValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CpfValidatorTest {


    private final CpfValidator validator = new CpfValidator();

    @Test
    void deveAceitarCpfValido() {
        assertTrue(validator.isValid("39053344705", null));
    }

    @Test
    void deveRejeitarCpfComDigitosIguais() {
        assertFalse(validator.isValid("11111111111", null));
    }

    @Test
    void deveRejeitarCpfComMenosDe11Digitos() {
        assertFalse(validator.isValid("12345", null));
    }

    @Test
    void deveRejeitarCpfComDigitosVerificadoresInvalidos() {
        assertFalse(validator.isValid("39053344700", null));
    }
}

