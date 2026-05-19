package com.flowcarreiras.flowcarreiras_api.exception;

public class AcessoNegadoException extends RuntimeException {

    public AcessoNegadoException() {
        super("Você não tem permissão para realizar esta ação");
    }
    public AcessoNegadoException(String message) {
        super(message);
    }
}
