package it.gtcode.net.ftp;

import it.gtcode.net.ftp.response.FTPResponse;
import it.gtcode.net.ftp.response.FTPStreamResponse;
import lombok.Cleanup;
import lombok.EqualsAndHashCode;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;

import java.io.*;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Implementazione di default di {@link FTPSession}.
 * @since 1.1
 * @see FTPSession
 * @author Giorgio Testa
 */
@EqualsAndHashCode
public class FTPSession_ApacheFTPClient implements FTPSession {

    private final FTPConfiguration configuration;
    private final FTPClient ftpClient;
    private final Path root;
    private boolean open;

    /**
     * Costruttore.
     * @param configuration configurazione con la quale creare la sessione
     * @throws UncheckedIOException se non è stato possibile creare la sessione
     */
    public FTPSession_ApacheFTPClient(FTPConfiguration configuration) {
        this.ftpClient = this.createClientInstance(configuration);
        this.configuration = configuration;
        this.root = this.getRoot(configuration);
        this.open = true;
    }

    /**
     * Costruttore. A differenza di {@link #FTPSession_ApacheFTPClient(FTPConfiguration)} utilizza un client già inizializzato.
     * @param configuration configurazione con la quale è stato creato inizializzato il client
     * @param ftpClient client da utilizzare per comunicare con il server
     * @see #FTPSession_ApacheFTPClient(FTPConfiguration) 
     */
    public FTPSession_ApacheFTPClient(FTPConfiguration configuration, FTPClient ftpClient) {
        this.ftpClient = ftpClient;
        this.configuration = configuration;
        this.root = this.getRoot(configuration);
        this.open = true;
    }

    /**
     * Indica se questa {@link FTPSession} è aperta o meno.<br>
     * Invocare metodi su una sessione chiusa comporta il fallimento automatico degli stessi.
     * @return {@code true} e è aperta, {@code false} altrimenti
     */
    @Override
    public boolean isOpen() {
        return this.open;
    }

    /**
     * Restituisce la configurazione con la quale è stata creata la sessione.
     * @return la configurazione della sessione
     */
    @Override
    public FTPConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
     * Restituisce il percorso con il quale è stata effettuata la connessione al server.
     * @return il percorso con il quale è stata effettuata la connessione al server
     */
    @Override
    public Path getRoot() {
        return this.root;
    }

    /**
     * Fornisce un {@link FTPStreamResponse} associato alla risorsa richiesta.<br>
     * È compito dell'utilizzatore chiudere lo stream una volta terminato l'utilizzo; la risposta restituita fornisce
     * funzioni di utility per semplificare la consumazione della risorsa.
     * @param file file da richiedere al server
     * @return un riferimento alla risorsa richiesta e i relativi codici di risposta del server
     * @see FTPStreamResponse
     * @throws IllegalStateException se la sessione non può essere utilizzata
     */
    @Override
    public FTPStreamResponse download(Path file) {
        this.canExecute();
        var response = new FTPStreamResponse(this::completePendingCommand);
        try {
            this.resetPosition();
            InputStream fileInputStream = ftpClient.retrieveFileStream(file.toString());
            this.throwWhenFalse(
                    fileInputStream != null,
                    "Non è stato possibile connettersi al file"
            );
            response.asSuccess(ftpClient.getReplyCode(), ftpClient.getReplyString(), fileInputStream);
        } catch (FTPConnectionClosedException | SocketException uce) {
            this.handleFTPConnectionClosedException();
            response.asError(ftpClient.getReplyCode(), ftpClient.getReplyString(), uce);
        } catch (IOException ioe) {
            response.asError(ftpClient.getReplyCode(), ftpClient.getReplyString(), ioe);
        }
        return response;
    }

    /**
     * Carica il file indicato restituendo l'esito dell'operazione.<br>
     * A differenza di {@link #upload(Path, Path)} il file viene caricato nella directory definita nella configurazione
     * della sessione.
     * @param file file da caricare sul server
     * @throws IllegalStateException se la sessione non può essere utilizzata
     * @return l'esito della richiesta con gli eventuali messaggi di errore
     * @see #upload(Path, Path)
     */
    @Override
    public FTPResponse upload(Path file) {
        return this.upload(file, root);
    }

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
    @Override
    public FTPResponse upload(Path file, Path target) {
        this.canExecute();
        var response = new FTPResponse();
        try {
            this.resetPosition();
            var remoteFile = target.resolve(file.getFileName());
            @Cleanup InputStream inputStream = new FileInputStream(file.toFile());
            this.createDirectoryTree(target);
            this.throwWhenFalse(
                    ftpClient.storeFile(remoteFile.getFileName().toString(), inputStream),
                    "Impossibile caricare il file sul server"
            );
            response.asSuccess(ftpClient.getReplyCode(), ftpClient.getReplyString());
        } catch (FTPConnectionClosedException fce) {
            this.handleFTPConnectionClosedException();
            response.asError(ftpClient.getReplyCode(), ftpClient.getReplyString(), fce);
        } catch (FileNotFoundException ffe) {
            response.asError("Non è stato possibile trovare il file " + file.getFileName());
        } catch (IOException ioe) {
            response.asError(ftpClient.getReplyCode(), ftpClient.getReplyString(), ioe);
        }
        return response;
    }

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
    @Override
    public FTPResponse upload(Path file, InputStream fileStream) {
        return this.upload(file, fileStream, root);
    }

    /**
     * Carica il file indicato restituendo l'esito dell'operazione.
     * @param file nome del file da caricare
     * @param fileStream {@code InputStream} relativo al file da caricare sul server
     * @param target directory nella quale caricare il file
     * @throws IllegalStateException se la sessione non può essere utilizzata
     * @return l'esito della richiesta con gli eventuali messaggi di errore
     * @see #upload(Path, Path)
     */
    @Override
    public FTPResponse upload(Path file, InputStream fileStream, Path target) {
        this.canExecute();
        var response = new FTPResponse();
        try {
            this.resetPosition();
            var remoteFile = target.resolve(file.getFileName());
            this.createDirectoryTree(target);
            this.throwWhenFalse(
                    ftpClient.storeFile(remoteFile.getFileName().toString(), fileStream),
                    "Impossibile caricare il file sul server"
            );
            response.asSuccess(ftpClient.getReplyCode(), ftpClient.getReplyString());
        } catch (FTPConnectionClosedException fce) {
            this.handleFTPConnectionClosedException();
            response.asError(ftpClient.getReplyCode(), ftpClient.getReplyString(), fce);
        } catch (IOException ioe) {
            response.asError(ftpClient.getReplyCode(), ftpClient.getReplyString(), ioe);
        }
        return response;
    }

    /**
     * Elimina il file indicato restituendo l'esito della richiesta.
     * @param file path del file da eliminare
     * @return l'esito della richiesta con gli eventuali messaggi di errore
     * @throws IllegalStateException se la sessione non può essere utilizzata
     */
    @Override
    public FTPResponse delete(Path file) {
        this.canExecute();
        var response = new FTPResponse();
        try {
            this.resetPosition();
            this.throwWhenFalse(
                    ftpClient.deleteFile(file.toString()),
                    "Impossibile rimuovere il file dal server"
            );
            response.asSuccess(ftpClient.getReplyCode(), ftpClient.getReplyString());
        } catch (FTPConnectionClosedException fce) {
            this.handleFTPConnectionClosedException();
            response.asError(ftpClient.getReplyCode(), ftpClient.getReplyString(), fce);
        } catch (IOException ioe) {
            response.asError(ftpClient.getReplyCode(), ftpClient.getReplyString(), ioe);
        }
        return response;
    }

    /**
     * Esegue il comando fornito sul server.
     * @param command comando da eseguire
     * @return l'esito della richiesta con gli eventuali messaggi di errore
     * @throws IllegalStateException se la sessione non può essere utilizzata
     */
    @Override
    public FTPResponse execute(String command) {
        this.canExecute();
        var response = new FTPResponse();
        try {
            this.resetPosition();
            this.throwWhenFalse(
                    ftpClient.sendSiteCommand(command),
                    "Impossibile eseguire il comando fornito"
            );
            response.asSuccess(ftpClient.getReplyCode(), ftpClient.getReplyString());
        } catch (FTPConnectionClosedException fce) {
            this.handleFTPConnectionClosedException();
            response.asError(ftpClient.getReplyCode(), ftpClient.getReplyString(), fce);
        } catch (IOException ioe) {
            response.asError(ftpClient.getReplyCode(), ftpClient.getReplyString(), ioe);
        }
        return response;
    }

    /**
     * Tenta di chiudere la sessione, eseguendo il logout e disconnettendosi dal server.<br>
     * Nel caso in cui la sessione risulti essere già chiusa questo comando non ha effetto.
     * @throws UncheckedIOException se viene riscontrato un problema durante la disconnessione dal server
     */
    @Override
    public void close() {
        if (!open) return;
        try {
            open = false;
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    /**
     * Dato il percorso/file fornito crea il directory tree nel caso questo non esista o risulti incompleto.
     * @param target percorso dal quale ricreare l'albero
     * @throws IOException se non è stato possibile creare la nuova directory o se non è stato possibile spostarsi nella
     * directory appena creata
     */
    private void createDirectoryTree(Path target) throws IOException {
        for (Path path : target) {
            if (Files.isDirectory(path)) continue;
            if (!ftpClient.changeWorkingDirectory(path.toString())) {
                this.throwWhenFalse(
                        ftpClient.makeDirectory(path.toString()),
                        "Impossibile creare la directory"
                );
                this.changeWorkingDirectory(path);
            }
        }
    }

    /**
     * Data la configurazione fornita tenta di inizializzare una connessione verso il server FTP.
     * @param ftpConfiguration configurazione con la quale inizializzare il client
     * @return {@link FTPClient} connesso alle coordinate fornite
     * @throws UncheckedIOException se non è stato possibile trovare, connettersi o eseguire il login al server
     */
    private FTPClient createClientInstance(FTPConfiguration ftpConfiguration) {
        var client = new FTPClient();
        try {
            client.connect(ftpConfiguration.getServer(), ftpConfiguration.getPort());
            client.login(ftpConfiguration.getUsername(), ftpConfiguration.getPassword());
            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException ioe) {
            throw new UncheckedIOException(
                    String.format(
                            "Non è stato possibile connettersi al server: (%s) %s",
                            client.getReplyCode(), client.getReplyString()
                    ),
                    ioe
            );
        }
        return client;
    }

    /**
     * Data la configurazione fornita importa la root della sessione:<br>
     * <ul>
     *     <li>
     *         se non è stata fornita alcuna preferenza di directory ({@link FTPConfiguration#getDirectory()})
     *         viene restituita la directory scelta dal server FTP;
     *     </li>
     *     <li>
     *         se è stata fornita una preferenza di directory ({@link FTPConfiguration#getDirectory()})
     *         viene restituita quella directory e viene spostato il focus della sessione su di essa;
     *     </li>
     * </ul>
     * @param ftpConfiguration configurazione fornita durante la creazione della sessione
     * @return percorso definito come root della sessione
     */
    private Path getRoot(FTPConfiguration ftpConfiguration) {
        try {
            if (ftpConfiguration.getDirectory() != null) {
                this.changeWorkingDirectory(configuration.getDirectory());
                return configuration.getDirectory();
            }
            var root = Path.of("/");
            configuration.setDirectory(root);
            return root;
        } catch (IOException ioe) {
            throw new UncheckedIOException("Non è stato possibile connettersi al server", ioe);
        }
    }

    /**
     * Tenta di spostarsi nel percorso fornito.
     * @param path percorso nel quale spostarsi
     * @throws IOException se non è stato possibile spostarsi nel nuovo percorso
     */
    private void changeWorkingDirectory(Path path) throws IOException {
        this.throwWhenFalse(
                ftpClient.changeWorkingDirectory(path.toString()),
                "Impossibile spostarsi nella directory"
        );
    }

    /**
     * Solleva una {@link IOException} nel caso {@code result} sia {@code false}, completando il messaggio fornito con
     * lo stato e messaggio di risposta restituiti dal server.
     * @param result parametro indicante se sollevare l'eccezione o meno
     * @param message messaggio di errore
     * @throws IOException se {@code result} è {@code false}
     */
    private void throwWhenFalse(boolean result, String message) throws IOException {
        if (result) return;
        throw new IOException(String.format(message + ": (%s) %s", ftpClient.getReplyCode(), ftpClient.getReplyString()));
    }

    /**
     * Riporta la sessione alla root dichiarata durante la creazione della stessa.
     * @throws IOException se non è stato possibile spostarsi
     */
    private void resetPosition() throws IOException {
        this.throwWhenFalse(ftpClient.changeWorkingDirectory(root.toString()), "Impossibile spostarsi nella directory");
    }

    /**
     * Invia al server vari comandi per gestire il termine di una transazione di download.
     * @throws UncheckedIOException se non è stato possibile eseguire i comandi
     */
    private void completePendingCommand() {
        try {
            ftpClient.completePendingCommand();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Gestisce la corretta chiusura della sessione nel caso venga sollevata una {@link FTPConnectionClosedException}.
     * @throws UncheckedIOException se non è stato possibile chiudere la sessione
     */
    private void handleFTPConnectionClosedException() {
        try {
            open = false;
            ftpClient.disconnect();
        } catch (IOException ioe) {
            throw new UncheckedIOException("Non è stato possibile disconnettersi dal server", ioe);
        }
    }

    /**
     * Verifica che la sessione si trovi in condizione di essere utilizzata.
     * @throws IllegalStateException se la sessione non può essere utilizzata
     */
    private void canExecute() {
        if (!open) throw new IllegalStateException("Impossibile utilizzare la sessione, questa risulta essere chiusa");
    }

}
