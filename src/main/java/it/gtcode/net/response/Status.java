package it.gtcode.net.response;

/**
 * Rappresenta un possibile stato di una {@code Response}.
 * @since 1.0
 * @see Response
 * @see BasicStatus
 * @author Giorgio Testa
 */
public interface Status {

    /**
     * Ottiene il messaggio di default dichiarato nello stato.
     * @return il messaggio dichiarato nello stato
     */
    String getMessage();

}
