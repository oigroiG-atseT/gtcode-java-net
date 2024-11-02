package it.gtcode.net.ftp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

/**
 * Rappresenta la configurazione con la quale instaurare una connessione a un server FTP.
 * @since 1.1
 * @author Giorgio Testa
 */
@EqualsAndHashCode
@Getter@Setter
public class FTPConfiguration {

    /** Indirizzo del server FTP. */
    private String server;
    /** Directory nella quale posizionarsi. Verrà utilizzata come root nelle sessioni. */
    private Path directory;
    /** Porta del server FTP alla quale connettersi. */
    private int port;

    /** Username con il quale effettuare l'accesso. */
    private String username;
    /** Password con la quale effettuare l'accesso. */
    private String password;

}
