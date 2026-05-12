package com.flowcarreiras.flowcarreiras_api.exception;

import java.util.UUID;

public class ObraNaoEncontradaException extends RuntimeException {

    public ObraNaoEncontradaException(UUID id) {
        super("Obra não encontrada: " + id);
    }

    public ObraNaoEncontradaException(String identificador) {
        super("Recurso não encontrado: " + identificador);
    }
}
