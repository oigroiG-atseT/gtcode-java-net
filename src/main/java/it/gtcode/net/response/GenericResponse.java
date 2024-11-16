package it.gtcode.net.response;

import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 * Implementazione di {@code Response}.<br>
 * Rappresenta una risposta generica contenente solamente l'esito e un eventuale
 * messaggio di risposta.<br>
 * Per indicare lo stato della risposta viene utilizzata la enum {@link Status}.
 * @since 1.0
 * @see Status
 * @see SingleResponse
 * @see ListResponse
 * @author Giorgio Testa
 */
@Setter
@EqualsAndHashCode
public class GenericResponse implements Response {

    private Status status;
    private String message;

    /**
     * Costruttore di base. Un oggetto appena creato ha {@code status} valorizzato a {@link Status#UNKNOWN}.
     */
    public GenericResponse() {
        this.status = Status.UNKNOWN;
        this.message = Status.UNKNOWN.getMessage();
    }

    /**
     * Ottiene lo stato della risposta.
     * @return {@link Status}
     * @since 2.0
     */
    @Override
    public Status getStatus() {
        return this.status;
    }

    /**
     * Ottiene l'eventuale messaggio di risposta.
     * @return {@link String}
     * @since 2.0
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    /**
     * Inizializza la risposta con lo stato {@link Status#SUCCESS} e il messaggio fornito.
     * @param message messaggio di risposta
     */
    @Override
    public void asSuccess(String message) {
        this.status = Status.SUCCESS;
        this.message = message;
    }

    /**
     * Inizializza la risposta con lo stato {@link Status#SUCCESS}
     */
    @Override
    public void asSuccess() {
        this.asSuccess(Status.SUCCESS.getMessage());
    }

    /**
     * Inizializza la risposta con lo stato {@link Status#ERROR} e il messaggio fornito.
     * @param message messaggio di risposta
     */
    @Override
    public void asError(String message) {
        this.status = Status.ERROR;
        this.message = message;
    }

    /**
     * Inizializza la risposta con lo stato {@link Status#ERROR} e con il relativo messaggio di errore.
     */
    @Override
    public void asError() {
        this.asError(Status.ERROR.getMessage());
    }

}
