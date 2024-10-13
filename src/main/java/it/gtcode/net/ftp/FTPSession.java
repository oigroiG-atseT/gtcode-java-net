package it.gtcode.net.ftp;

import it.gtcode.net.ftp.response.FTPResponse;
import it.gtcode.net.ftp.response.FTPStreamResponse;

import java.io.Closeable;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;

/**
 * Rappresenta una sessione instaurata tra un client e un server FTP.<br>
 * Mette a disposizione vari metodi per eseguire varie operazioni di base come scambio e rimozione di file ed esecuzione di comandi.
 * @since 1.1
 * @author Giorgio Testa
 */
public interface FTPSession extends Closeable {

    /**
     * Indica se questa {@link FTPSession} è aperta o meno.<br>
     * Invocare metodi su una sessione chiusa comporta il fallimento automatico degli stessi.
     * @return {@code true} e è aperta, {@code false} altrimenti
 */
    boolean isOpen();

    /**
     * Restituisce la configurazione con la quale è stata creata la sessione.
     * @return la configurazione della sessione
     */
    FTPConfiguration getConfiguration();

    /**
     * Restituisce il percorso con il quale è stata effettuata la connessione al server.
     * @return il percorso con il quale è stata effettuata la connessione al server
     */
    Path getRoot();

    /**
     * Fornisce un {@link FTPStreamResponse} associato alla risorsa richiesta.<br>
     * È compito dell'utilizzatore chiudere lo stream una volta terminato l'utilizzo; la risposta restituita fornisce
     * funzioni di utility per semplificare la consumazione della risorsa.
     * @param file file da richiedere al server
     * @return un riferimento alla risorsa richiesta e i relativi codici di risposta del server
     * @see FTPStreamResponse
     * @throws IllegalStateException se la sessione non può essere utilizzata
     */
    FTPStreamResponse download(Path file);

    /**
     * Carica il file indicato restituendo l'esito dell'operazione.<br>
     * A differenza di {@link #upload(Path, Path)} il file viene caricato nella directory definita nella configurazione
     * della sessione.
     * @param file file da caricare sul server
     * @throws IllegalStateException se la sessione non può essere utilizzata
     * @return l'esito della richiesta con gli eventuali messaggi di errore
     * @see #upload(Path, Path)
     */
    FTPResponse upload(Path file);

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
     * Carica il file indicato restituendo l'esito dell'operazione.<br>
     * A differenza di {@link #upload(Path, InputStream, Path)} il file viene caricato nella directory definita
     * nella configurazione della sessione.
     * @param file nome del file da caricare
     * @param fileStream {@code InputStream} relativo al file da caricare sul server
     * @throws IllegalStateException se la sessione non può essere utilizzata
     * @return l'esito della richiesta con gli eventuali messaggi di errore
     * @see #upload(Path, InputStream, Path)
     */
    FTPResponse upload(Path file, InputStream fileStream);

    /**
     * Carica il file indicato restituendo l'esito dell'operazione.
     * @param file nome del file da caricare
     * @param fileStream {@code InputStream} relativo al file da caricare sul server
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
     * Esegue il comando fornito sul server.
     * @param command comando da eseguire
     * @return l'esito della richiesta con gli eventuali messaggi di errore
     * @throws IllegalStateException se la sessione non può essere utilizzata
     */
    FTPResponse execute(String command);

    /**
     * Tenta di chiudere la sessione, eseguendo il logout e disconnettendosi dal server.<br>
     * Nel caso in cui la sessione risulti essere già chiusa questo comando non ha effetto.
     * @throws UncheckedIOException se viene riscontrato un problema durante la disconnessione dal server
     */
    @Override
    void close();

}
