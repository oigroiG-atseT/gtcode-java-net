package it.gtcode.net.ftp;

import lombok.EqualsAndHashCode;
import org.apache.commons.net.ftp.FTPClient;

import java.util.Objects;

/**
 * Implementazione di {@code FTPSessionFactory} con {@code apache-commons-net}.
 * @since 1.1
 * @see FTPSessionFactory
 * @author Giorgio Testa
 */
@EqualsAndHashCode
public class FTPSessionFactoryApache implements FTPSessionFactory {

    private final FTPConfiguration configuration;

    /**
     * Costruttore.
     * @param configuration configurazione da utilizzare durante la creazione delle sessioni
     * @throws NullPointerException se la configurazione fornita è {@code null}
     */
    public FTPSessionFactoryApache(FTPConfiguration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
    }

    /**
     * Restituisce la configurazione con la quale è stata creata la factory.
     * @return configurazione fornita durante la costruzione della factory
     */
    @Override
    public FTPConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
     * Crea una nuova sessione con la configurazione dichiarata alla creazione della factory.
     * @return la sessione creata
     * @throws java.io.UncheckedIOException nel caso in cui non sia stato possibile creare la sessione
     */
    @Override
    public FTPSession openSession() {
        return new FTPSession_ApacheFTPClient(configuration);
    }

    /**
     * Crea una nuova sessione con la configurazione fornita.
     * @param configuration configurazione da utilizzare al posto di quella fornita al costruttore della factory
     * @return la sessione creata
     * @throws java.io.UncheckedIOException nel caso in cui non sia stato possibile creare la sessione
     */
    @Override
    public FTPSession openSession(FTPConfiguration configuration) {
        return new FTPSession_ApacheFTPClient(configuration);
    }

    /**
     * Crea una nuova sessione con la configurazione e client FTP forniti.
     * @param configuration configurazione da utilizzare al posto di quella fornita al costruttore della factory
     * @param ftpClient {@code org.apache.commons.net.ftp.FTPClient} da utilizzare, deve essere già inizializzato e connesso.
     * @return la sessione creata
     * @throws IllegalArgumentException se il client fornito non è del tipo richiesto
     */
    @Override
    public FTPSession openSession(FTPConfiguration configuration, Object ftpClient) {
        return new FTPSession_ApacheFTPClient(configuration, this.checkClient(ftpClient));
    }

    /**
     * Verifica che l'oggetto fornito sia un istanza di {@code org.apache.commons.net.ftp.FTPClient}.
     * @param ftpClient istanza di {@code org.apache.commons.net.ftp.FTPClient}
     * @return istanza nel tipo previsto
     * @throws IllegalArgumentException se l'oggetto fornito non è del tipo previsto
     */
    private FTPClient checkClient(Object ftpClient) {
        if (ftpClient instanceof FTPClient) return (FTPClient) ftpClient;
        throw new IllegalArgumentException("Il client fornito deve essere un istanza di org.apache.commons.net.ftp.FTPClient");
    }

}
