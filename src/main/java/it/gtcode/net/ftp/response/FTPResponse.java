package it.gtcode.net.ftp.response;

import it.gtcode.net.response.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * Implementazione di {@code Response} per il protocollo FTP.<br>
 * Rappresenta una risposta generica contenente solamente l'esito, il codice di risposta,
 * un eventuale messaggio e {@link IOException}.<br>
 * @since 1.1
 * @see GenericResponse
 * @see FTPReplyCode
 * @see IOException
 * @author Giorgio Testa
 */
@EqualsAndHashCode(callSuper = true)
@Getter@Setter
public class FTPResponse extends GenericResponse {

    /** Codice di risposta ottenuto dal server */
    private FTPReplyCode replyCode;
    /**
     * Eventuale eccezione riscontrata durante il tentativo di comunicazione con il server.<br>
     * Potrebbe essere {@code null} anche se stato della risposta è un errore.
     */
    private IOException exception;

    /**
     * Inizializza la risposta con lo stato {@link BasicStatus#SUCCESS} e gli oggetti forniti.
     * @param replyCode codice di risposta dal server
     * @param message messaggio testuale di risposta dal server
     */
    public void asSuccess(int replyCode, String message) {
        super.asSuccess(message);
        this.replyCode = FTPReplyCode.valueOfCode(replyCode);
    }

    /**
     * Inizializza la risposta con lo stato {@link BasicStatus#ERROR} e gli oggetti forniti.
     * @param replyCode codice di risposta dal server
     * @param message messaggio testuale di risposta dal server
     * @param exception eccezione riscontrata
     */
    public void asError(int replyCode, String message, IOException exception) {
        this.asError(replyCode, message);
        this.exception = exception;
    }

    /**
     * Inizializza la risposta con lo stato {@link BasicStatus#ERROR} e gli oggetti forniti.
     * @param replyCode codice di risposta dal server
     * @param message messaggio testuale di risposta dal server
     */
    public void asError(int replyCode, String message) {
        super.asError(message);
        this.replyCode = FTPReplyCode.valueOfCode(replyCode);
    }

}
