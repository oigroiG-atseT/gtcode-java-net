package it.gtcode.net.ftp;

import it.gtcode.net.ftp.response.FTPResponse;
import it.gtcode.net.ftp.response.FTPStreamResponse;
import lombok.Cleanup;
import lombok.EqualsAndHashCode;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;

import java.io.*;
import java.nio.file.Path;

@EqualsAndHashCode
public class FTPSession_ApacheFTPClient implements FTPSession {

    private final FTPClient ftpClient;
    private final Path root;
    private boolean open;

    public FTPSession_ApacheFTPClient(FTPClient ftpClient, Path root) {
        this.ftpClient = ftpClient;
        this.root = root;
        this.open = true;
    }

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public Path getRoot() {
        return this.root;
    }

    /**
     *
     * @param file
     * @return
     * @throws IllegalStateException se la sessione non può essere utilizzata
     */
    @Override
    public FTPStreamResponse download(Path file) {
        this.canExecute();
        var response = new FTPStreamResponse(this::completePendingCommand);
        try {
            this.resetPosition();
            InputStream fileInputStream = ftpClient.retrieveFileStream(file.toString());
            response.asSuccess(ftpClient.getReplyCode(), ftpClient.getReplyString(), fileInputStream);
        } catch (FTPConnectionClosedException fce) {
            this.handleFTPConnectionClosedException();
            response.asError(ftpClient.getReplyCode(), ftpClient.getReplyString(), fce);
        } catch (IOException ioe) {
            response.asError(ftpClient.getReplyCode(), ftpClient.getReplyString(), ioe);
        }
        return response;
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
            var remoteFile = target.relativize(file.getFileName());
            @Cleanup InputStream inputStream = new FileInputStream(file.toFile());
            this.createDirectoryTree(target);
            this.throwWhenFalse(
                    ftpClient.storeFile(remoteFile.toString(), inputStream),
                    "Impossibile caricare il file sul server"
            );
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
     * Carica il file indicato restituendo l'esito dell'operazione.
     * @param file nome del file da caricare
     * @param fileStream {@code InputStream} realtivo al file da caricare sul server
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
            var remoteFile = target.relativize(file.getFileName());
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            this.createDirectoryTree(target);
            this.throwWhenFalse(
                    ftpClient.storeFile(remoteFile.toString(), fileStream),
                    "Impossibile caricare il file sul server"
            );
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
            if (path.equals(target.getFileName())) continue;
            if (!ftpClient.changeWorkingDirectory(path.toString())) {
                this.throwWhenFalse(
                        ftpClient.makeDirectory(path.toString()),
                        "Impossibile creare la directory"
                );
                this.throwWhenFalse(
                        ftpClient.changeWorkingDirectory(path.toString()),
                        "Impossibile spostarsi nella directory"
                );
            }
        }
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
