package it.gtcode.net.ftp;

/**
 * Interfaccia per le factory dedicate alla creazione di sessioni FTP.
 * @since 1.1
 * @see FTPSession
 * @see FTPConfiguration
 * @author Giorgio Testa
 */
public interface FTPSessionFactory {

    /**
     * Restituisce la configurazione con la quale è stata creata la factory.
     * @return configurazione fornita durante la costruzione della factory
     */
    FTPConfiguration getConfiguration();

    /**
     * Crea una nuova sessione con la configurazione dichiarata alla creazione della factory.
     * @return la sessione creata
     * @throws java.io.UncheckedIOException nel caso in cui non sia stato possibile creare la sessione
     */
    FTPSession openSession();

    /**
     * Crea una nuova sessione con la configurazione fornita.
     * @param configuration configurazione da utilizzare al posto di quella fornita al costruttore della factory
     * @return la sessione creata
     * @throws java.io.UncheckedIOException nel caso in cui non sia stato possibile creare la sessione
     */
    FTPSession openSession(FTPConfiguration configuration);

    /**
     * Crea una nuova sessione con la configurazione e client FTP forniti.
     * @param configuration configurazione da utilizzare al posto di quella fornita al costruttore della factory
     * @param ftpClient client FTP da utilizzare, deve essere già inizializzato e connesso
     * @return la sessione creata
     * @throws IllegalArgumentException se il tipo del client fornito non rispecchia quanto richiesto
     * dall'implementazione della factory
     */
    FTPSession openSession(FTPConfiguration configuration, Object ftpClient);

}
