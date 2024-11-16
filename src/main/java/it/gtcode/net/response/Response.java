package it.gtcode.net.response;

/**
 * La risposta di una richiesta od operazione effettuata attraverso uno dei protocolli di rete supportati dalla libreria.<br>
 * Offre metodi di utility per semplificare la gestione delle classi implementanti quest'interfaccia.
 * @author Giorgio Testa
 * @see GenericResponse
 * @see Status
 * @since 1.0
 */
public interface Response {

    /**
     * Ottiene lo stato della risposta.
     * @return {@link Status}
     * @since 2.0
     */
    Status getStatus();

    /**
     * Ottiene l'eventuale messaggio di risposta.
     * @return {@link String}
     * @since 2.0
     */
    String getMessage();

    /**
     * Indica che questa risposta deve rappresentare un esito positivo contenente il messaggio fornito.
     * @param message messaggio di risposta
     */
    void asSuccess(String message);

    /**
     * Indica che questa risposta deve rappresentare un esito positivo.
     */
    void asSuccess();

    /**
     * Indica che questa risposta deve rappresentare un esito negativo contenente il messaggio fornito.
     * @param message messaggio di risposta
     */
    void asError(String message);

    /**
     * Indica che questa risposta deve rappresentare un esito negativo.
     */
    void asError();

}
