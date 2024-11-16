package it.gtcode.net.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Rappresenta un possibile stato di una {@code Response}.
 * @since 1.0
 * @author Giorgio Testa
 */
@Getter
@AllArgsConstructor
public enum Status {

    /** Rappresenta uno stato non definito */
    UNKNOWN(null),
    /** Rappresenta un esito positivo. */
    SUCCESS(null),
    /** Rappresenta un esito negativo. */
    ERROR("Errore dal server");

    private final String message;

}
