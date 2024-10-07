package it.gtcode.net.ftp;

import it.gtcode.net.ftp.response.FTPResponse;
import it.gtcode.net.ftp.response.FTPStreamResponse;

import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public interface FTPSession {

    boolean isOpen();

    Path getRoot();

    FTPStreamResponse download(Path file);

    /**
     * Carica il file indicato restituendo l'esito dell'operazione.<br>
     * A differenza di {@link #upload(Path, InputStream, Path)} l'{@code InputStream} viene creato e gestito direttamente
     * da questo metodo.
     * @param file file da caricare sul server
     * @param target directory nella quale caricare il file
     * @throws IllegalStateException se la sessione non può essere utilizzata
     * @return l'esito della richiesta con gli eventuali messaggi di errore
     * @see #upload(Path, InputStream, Path)
     */
    FTPResponse upload(Path file, Path target);

    /**
     * Carica il file indicato restituendo l'esito dell'operazione.
     * @param file nome del file da caricare
     * @param fileStream {@code InputStream} realtivo al file da caricare sul server
     * @param target directory nella quale caricare il file
     * @throws IllegalStateException se la sessione non può essere utilizzata
     * @return l'esito della richiesta con gli eventuali messaggi di errore
     */
    FTPResponse upload(Path file, InputStream fileStream, Path target);

    /**
     * Elimina il file indicato restituendo l'esito della richiesta.
     * @param file path del file da eliminare
     * @return l'esito della richiesta con gli eventuali messaggi di errore
     * @throws IllegalStateException se la sessione non può essere utilizzata
     */
    FTPResponse delete(Path file);

    /**
     * Tenta di chiudere la sessione, eseguendo il logout e disconnettendosi dal server.<br>
     * Nel caso in cui la sessione risulti essere già chiusa questo comando non ha effetto.
     * @throws UncheckedIOException se viene riscontrato un problema durante la disconnessione dal server
     */
    void close();

}
