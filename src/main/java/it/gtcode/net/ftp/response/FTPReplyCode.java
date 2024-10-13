package it.gtcode.net.ftp.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Predicate;

/**
 * Rappresenta vari codici di stato del protocollo FTP.<br>
 * Mette inoltre a disposizione metodi di utility nella gestione degli stati.
 * @since 1.1
 * @see Status
 * @author Giorgio Testa
 */
@AllArgsConstructor
@Getter
public enum FTPReplyCode {
    
    /**
     * 110: Il server ha riscontrato un marcatore di ripristino.
     */
    RESTART_MARKER(110),
    /**
     * 120: Il servizio è al momento non disponibile.
     */
    SERVICE_NOT_READY(120),
    /**
     * 125: La connessione dati è già aperta e il trasferimento inizierà.
     */
    DATA_CONNECTION_ALREADY_OPEN(125),
    /**
     * 150: Il file è in uno stato OK e il trasferimento inizierà.
     */
    FILE_STATUS_OK(150),
    /**
     * 200: Il comando è stato eseguito con successo.
     */
    COMMAND_OK(200),
    /**
     * 202: Il comando non è necessario o superfluo.
     */
    COMMAND_IS_SUPERFLUOUS(202),
    /**
     * 211: Stato del sistema o risposta allo stato corrente.
     */
    SYSTEM_STATUS(211),
    /**
     * 212: Stato della directory attuale.
     */
    DIRECTORY_STATUS(212),
    /**
     * 213: Stato del file attuale.
     */
    FILE_STATUS(213),
    /**
     * 214: Messaggio di aiuto, descrizione dei comandi disponibili.
     */
    HELP_MESSAGE(214),
    /**
     * 215: Tipo del sistema del server.
     */
    NAME_SYSTEM_TYPE(215),
    /**
     * 220: Il servizio è pronto per il nuovo utente.
     */
    SERVICE_READY(220),
    /**
     * 221: Il servizio sta chiudendo la connessione di controllo.
     */
    SERVICE_CLOSING_CONTROL_CONNECTION(221),
    /**
     * 225: La connessione dati è aperta; nessun trasferimento sta avvenendo.
     */
    DATA_CONNECTION_OPEN(225),
    /**
     * 226: Chiudendo la connessione dati; il trasferimento è stato completato con successo.
     */
    CLOSING_DATA_CONNECTION(226),
    /**
     * 227: Entrando in modalità passiva.
     */
    ENTERING_PASSIVE_MODE(227),
    /**
     * 229: Entrando in modalità passiva estesa.
     */
    ENTERING_EPSV_MODE(229),
    /**
     * 230: Utente autenticato correttamente.
     */
    USER_LOGGED_IN(230),
    /**
     * 234: Lo scambio dati di sicurezza è completo.
     */
    SECURITY_DATA_EXCHANGE_COMPLETE(234),
    /**
     * 235: Scambio dati di sicurezza completato con successo.
     */
    SECURITY_DATA_EXCHANGE_SUCCESSFULLY(235),
    /**
     * 250: L'azione richiesta sul file è stata completata con successo.
     */
    FILE_ACTION_OK(250),
    /**
     * 257: Il nome del percorso è stato creato.
     */
    PATHNAME_CREATED(257),
    /**
     * 331: Nome utente OK, ma è necessaria una password.
     */
    NEED_PASSWORD(331),
    /**
     * 332: È necessario un account per accedere al servizio.
     */
    NEED_ACCOUNT(332),
    /**
     * 334: Il meccanismo di sicurezza è accettabile.
     */
    SECURITY_MECHANISM_IS_OK(334),
    /**
     * 335: I dati di sicurezza forniti sono accettabili.
     */
    SECURITY_DATA_IS_ACCEPTABLE(335),
    /**
     * 350: L'azione richiesta è in sospeso, ulteriori informazioni sono necessarie.
     */
    FILE_ACTION_PENDING(350),
    /**
     * 421: Il servizio non è disponibile, chiusura della connessione di controllo.
     */
    SERVICE_NOT_AVAILABLE(421),
    /**
     * 425: Impossibile aprire la connessione dati.
     */
    CANNOT_OPEN_DATA_CONNECTION(425),
    /**
     * 426: La connessione è stata chiusa, il trasferimento è stato interrotto.
     */
    TRANSFER_ABORTED(426),
    /**
     * 431: Risorsa non disponibile o troppo occupata.
     */
    UNAVAILABLE_RESOURCE(431),
    /**
     * 450: Il file richiesto non è stato preso in considerazione.
     */
    FILE_ACTION_NOT_TAKEN(450),
    /**
     * 451: L'azione richiesta è stata interrotta a causa di un errore locale.
     */
    ACTION_ABORTED(451),
    /**
     * 452: Il server ha riscontrato un problema di spazio durante la richiesta.
     */
    INSUFFICIENT_STORAGE(452),
    /**
     * 500: Comando non riconosciuto.
     */
    UNRECOGNIZED_COMMAND(500),
    /**
     * 501: Errore di sintassi negli argomenti del comando.
     */
    SYNTAX_ERROR_IN_ARGUMENTS(501),
    /**
     * 502: Il comando non è implementato.
     */
    COMMAND_NOT_IMPLEMENTED(502),
    /**
     * 503: Sequenza di comandi errata.
     */
    BAD_COMMAND_SEQUENCE(503),
    /**
     * 504: Il comando non è implementato per il parametro specificato.
     */
    COMMAND_NOT_IMPLEMENTED_FOR_PARAMETER(504),
    /**
     * 530: Utente non autenticato, è necessario effettuare il login.
     */
    NOT_LOGGED_IN(530),
    /**
     * 532: È necessario un account per memorizzare i file.
     */
    NEED_ACCOUNT_FOR_STORING_FILES(532),
    /**
     * 533: Richiesta negata per motivi di policy.
     */
    DENIED_FOR_POLICY_REASONS(533),
    /**
     * 534: Richiesta negata per errore di verifica della sicurezza.
     */
    FAILED_SECURITY_CHECK(534),
    /**
     * 535: Il livello di protezione richiesto non è supportato.
     */
    REQUESTED_PROT_LEVEL_NOT_SUPPORTED(535),
    /**
     * 550: Il file non è disponibile o non esiste.
     */
    FILE_UNAVAILABLE(550),
    /**
     * 551: Tipo di pagina sconosciuto.
     */
    PAGE_TYPE_UNKNOWN(551),
    /**
     * 552: Spazio di archiviazione superato.
     */
    STORAGE_ALLOCATION_EXCEEDED(552),
    /**
     * 553: Nome del file non consentito.
     */
    FILE_NAME_NOT_ALLOWED(553),
    /**
     * 522: Fallimento esteso nella modalità PORT.
     */
    EXTENDED_PORT_FAILURE(522),
    /**
     * 522: Negoziazione TLS errata o crittografia dati richiesta.
     */
    BAD_TLS_NEGOTIATION_OR_DATA_ENCRYPTION_REQUIRED(522);

    /** Valore numerico del {@link FTPReplyCode} */
    private final int code;

    /**
     * Restituisce lo stato che rappresenta questo codice {@link FTPReplyCode}.
     * @return il relativo stato di questo codice di risposta
     */
    public Status getStatus() {
        return Status.valueOfCode(this.code);
    }

    /**
     * Restituisce il {@link FTPReplyCode} associato al valore numerico fornito.
     * @param replyCode codice numerico di risposta del protocollo FTP
     * @throws IllegalArgumentException se il codice fornito non è tra quelli dichiarati
     * @return il associato al valore numerico fornito
     */
    public static FTPReplyCode valueOfCode(int replyCode) {
        for (var value : values())
            if (value.code == replyCode) return value;
        throw new IllegalArgumentException(String.format("Il codice %s non è tra quelli attualmente supportati", replyCode));
    }

    /**
     * Rappresenta i possibili stati ai quali gli {@link FTPReplyCode} possono appartenere.
     * @since 1.1
     * @see FTPReplyCode
     * @author Giorgio Testa
     */
    @AllArgsConstructor
    public enum Status {

        /** Indica un errore permanente, il client non deve ripetere la richiesta. */
        NEGATIVE_PERMANENT((code) -> code >= 500 && code < 600),
        /** Indica un errore temporaneo, il client può riprovare la richiesta in un secondo momento. */
        NEGATIVE_TRANSIENT((code) -> code >= 400 && code < 500),
        /** Il comando è stato eseguito con successo e non sono richieste ulteriori azioni da parte del client. */
        POSITIVE_COMPLETION((code) -> code >= 200 && code < 300),
        /** Il comando è stato accettato ma il server attende ulteriori informazioni o comandi per completare l'operazione. */
        POSITIVE_INTERMEDIATE((code) -> code >= 300 && code < 400),
        /**  Il server ha accettato il comando e inizierà l'azione richiesta, ma la risposta finale non è ancora disponibile. */
        POSITIVE_PRELIMINARY((code) -> code >= 100 && code < 200),
        /** Indica un che è protetto o soggetto a meccanismi di sicurezza. */
        PROTECTED_REPLY_CODE((code) -> code >= 600 && code < 700);

        /** Vincolo di attribuzione dei codici */
        private final Predicate<Integer> range;

        /**
         * Restituisce lo {@link Status} associato al valore numerico fornito.
         * @param replyCode codice numerico di risposta del protocollo FTP
         * @throws IllegalArgumentException se il codice fornito non è tra quelli dichiarati
         * @return il associato al valore numerico fornito
         */
        public static Status valueOfCode(int replyCode) {
            for (var value : values())
                if (value.range.test(replyCode)) return value;
            throw new IllegalArgumentException(String.format("Il codice %s non è tra quelli attualmente supportati", replyCode));
        }

    }

}
