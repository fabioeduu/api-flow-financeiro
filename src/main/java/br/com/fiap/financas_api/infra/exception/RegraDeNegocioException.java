package br.com.fiap.financas_api.infra.exception;

public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String message) {
        super(message);
    }
}
