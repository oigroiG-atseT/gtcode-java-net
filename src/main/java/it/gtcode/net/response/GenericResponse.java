package it.gtcode.net.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Implementazione di {@code Response}.<br>
 * Rappresenta una risposta generica contenente solamente l'esito e un eventuale
 * messaggio di risposta.<br>
 * Per indicare lo stato della risposta viene utilizzata l'enum {@link BasicStatus}.
 * @since 1.0
 * @see Status
 * @see BasicStatus
 * @see SingleResponse
 * @see ListResponse
 * @author Giorgio Testa
 */
@EqualsAndHashCode
@Getter@Setter
public class GenericResponse implements Response {

    private Status status;
    private String message;

    /**
     * Costruttore di base. Un oggetto appena creato ha {@code status} valorizzato a {@link BasicStatus#UNKNOWN}.
     */
    public GenericResponse() {
        this.status = BasicStatus.UNKNOWN;
        this.message = BasicStatus.UNKNOWN.getMessage();
    }

    /**
     * Inizializza la risposta con lo stato {@link BasicStatus#SUCCESS} ed il messaggio fornito.
     * @param message messaggio di risposta
     */
    @Override
    public void asSuccess(String message) {
        this.status = BasicStatus.SUCCESS;
        this.message = message;
    }

    /**
     * Inizializza la risposta con lo stato {@link BasicStatus#SUCCESS}
     */
    @Override
    public void asSuccess() {
        this.asSuccess(BasicStatus.SUCCESS.getMessage());
    }

    /**
     * Inizializza la risposta con lo stato {@link BasicStatus#ERROR} ed il messaggio fornito.
     * @param message messaggio di risposta
     */
    @Override
    public void asError(String message) {
        this.status = BasicStatus.ERROR;
        this.message = message;
    }

    /**
     * Inizializza la risposta con lo stato {@link BasicStatus#ERROR} e con il relativo messaggio di errore.
     */
    @Override
    public void asError() {
        this.asError(BasicStatus.ERROR.getMessage());
    }

}
