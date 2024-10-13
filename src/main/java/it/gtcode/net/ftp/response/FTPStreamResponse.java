package it.gtcode.net.ftp.response;

import it.gtcode.net.response.BasicStatus;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Rappresenta la risposta alla richiesta di una risorsa in streaming dal server.
 * @since 1.1
 * @see FTPResponse
 * @author Giorgio Testa
 */
@EqualsAndHashCode(callSuper = true)
public class FTPStreamResponse extends FTPResponse {

    private InputStream stream;
    private final Runnable serverCompleteTransactionCallback;

    public FTPStreamResponse(Runnable serverCompleteTransactionCallback) {
        this.serverCompleteTransactionCallback = serverCompleteTransactionCallback;
    }

    /**
     * Restituisce l'{@link InputStream} rappresentante la risorsa richiesta al server.<br>
     * È compito dell'utilizzatore chiamare il metodo {@link #close()} per far avvenire un corretto rilascio delle risorse
     * sia del client che del server.<br>
     * Si consiglia l'utilizzo di {@link #consume(Consumer)} in quanto ne gestisce in automatico le risorse.
     * @return lo stream wrappato in un {@link Optional}
     * @see #close()
     * @see #consume(Consumer)
     */
    public Optional<InputStream> getStream() {
        return Optional.ofNullable(stream);
    }

    /**
     * Gestisce in automatico il rilascio delle risorse una volta che è stata consumata la funzione fornita.
     * @param consumer funzione utilizzante lo stream
     * @throws NullPointerException se {@code consumer} è {@code null}
     * @throws java.util.NoSuchElementException se lo stream è {@code null}
     */
    public void consume(Consumer<InputStream> consumer) {
        Objects.requireNonNull(consumer);
        var inputStream = this.getStream().orElseThrow();
        try {
            consumer.accept(inputStream);
        } finally {
            this.close();
        }
    }

    /**
     * Chiude l'{@link InputStream} contenuto, terminando la transazione con il server per questa risorsa.
     * @throws UncheckedIOException se non è stato possibile chiudere lo stream o
     * è stato riscontrato un problema durante la terminazione della transazione
     */
    public void close() {
        try {
            this.getStream().ifPresent(stream -> {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } finally {
            serverCompleteTransactionCallback.run();
        }
    }

    /**
     * Inizializza la risposta con lo stato {@link BasicStatus#SUCCESS} e gli oggetti forniti.
     * @param replyCode codice di risposta dal server
     * @param message messaggio testuale di risposta dal server
     * @param stream stream della risorsa richiesta
     */
    public void asSuccess(int replyCode, String message, InputStream stream) {
        super.asSuccess(replyCode, message);
        this.stream = stream;
    }

}
