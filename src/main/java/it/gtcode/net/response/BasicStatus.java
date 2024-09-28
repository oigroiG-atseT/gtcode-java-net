package it.gtcode.net.response;

import lombok.AllArgsConstructor;

/**
 * Implementazione di base di {@link Status}.</br>
 * @since 1.0
 * @author Giorgio Testa
 */
@AllArgsConstructor
enum BasicStatus implements Status {

    /** Rappresenta uno stato non definito */
    UNKNOWN(null),
    /** Rappresenta un esito positivo. */
    SUCCESS(null),
    /** Rappresenta un esito negativo. */
    ERROR("Errore dal server");

    private final String message;

    @Override
    public String getMessage() { return this.message; }

}
