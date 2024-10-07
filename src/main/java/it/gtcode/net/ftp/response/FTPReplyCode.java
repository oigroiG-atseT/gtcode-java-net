package it.gtcode.net.ftp.response;

import lombok.AllArgsConstructor;

/**
 * Rappresenta vari codici di stato del protocollo FTP.<br>
 * Mette inoltre a disposizione metodi di utility nella gestione degli stati.
 * @since 1.1
 * @see Status
 * @author Giorgio Testa
 */
@AllArgsConstructor
public enum FTPReplyCode {

    /**
     * 425 - Impossibile aprire la connessione dati.
     * Il server non è riuscito ad aprire una connessione dati per il trasferimento.
     */
    CANNOT_OPEN_DATA_CONNECTION(425),
    /**
     * 426 - Trasferimento abortito.
     * Il trasferimento dati è stato interrotto prima di essere completato.
     */
    TRANSFER_ABORTED(426),
    /**
     * 450 - Azione sul file non eseguita.
     * Il server non è riuscito a eseguire l'azione richiesta sul file.
     */
    FILE_ACTION_NOT_TAKEN(450),
    /**
     * 451 - Azione abortita.
     * L'azione richiesta è stata abortita a causa di un errore imprevisto sul server.
     */
    ACTION_ABORTED(451),
    /**
     * 452 - Memoria insufficiente.
     * Il server non dispone di spazio sufficiente per eseguire l'operazione richiesta.
     */
    INSUFFICIENT_STORAGE(452),
    /**
     * 500 - Comando non riconosciuto.
     * Il server non riconosce il comando inviato.
     */
    UNRECOGNIZED_COMMAND(500),
    /**
     * 501 - Errore di sintassi negli argomenti.
     * Il comando inviato contiene argomenti con errori di sintassi.
     */
    SYNTAX_ERROR_IN_ARGUMENTS(501),
    /**
     * 502 - Comando non implementato.
     * Il server non supporta il comando richiesto.
     */
    COMMAND_NOT_IMPLEMENTED(502),
    /**
     * 503 - Sequenza di comandi errata.
     * I comandi sono stati inviati in un ordine non valido.
     */
    BAD_COMMAND_SEQUENCE(503),
    /**
     * 504 - Comando non implementato per il parametro.
     * Il comando è valido, ma non può essere eseguito con i parametri specificati.
     */
    COMMAND_NOT_IMPLEMENTED_FOR_PARAMETER(504),
    /**
     * 530 - Non loggato.
     * L'utente non è autenticato.
     */
    NOT_LOGGED_IN(530),
    /**
     * 532 - Necessario account per salvare file.
     * È richiesto un account per poter caricare o salvare file.
     */
    NEED_ACCOUNT_FOR_STORING_FILES(532),
    /**
     * 550 - File non disponibile.
     * Il file richiesto non è disponibile o non può essere accessibile.
     */
    FILE_UNAVAILABLE(550),
    /**
     * 551 - Tipo di pagina sconosciuto.
     * Il tipo di pagina del file non è riconosciuto dal server.
     */
    PAGE_TYPE_UNKNOWN(551),
    /**
     * 552 - Allocazione di memoria superata.
     * L'allocazione di memoria richiesta per il file è stata superata.
     */
    STORAGE_ALLOCATION_EXCEEDED(552),
    /**
     * 553 - Nome del file non consentito.
     * Il nome del file fornito non è valido o non è consentito.
     */
    FILE_NAME_NOT_ALLOWED(553),
    /**
     * 234 - Scambio di dati di sicurezza completato.
     * Lo scambio di dati di sicurezza è stato completato con successo.
     */
    SECURITY_DATA_EXCHANGE_COMPLETE(234),
    /**
     * 235 - Scambio di dati di sicurezza avvenuto con successo.
     * Lo scambio di dati di sicurezza è stato eseguito correttamente.
     */
    SECURITY_DATA_EXCHANGE_SUCCESSFULLY(235),
    /**
     * 334 - Meccanismo di sicurezza accettato.
     * Il meccanismo di sicurezza proposto è stato accettato dal server.
     */
    SECURITY_MECHANISM_IS_OK(334),
    /**
     * 335 - Dati di sicurezza accettabili.
     * I dati di sicurezza forniti sono stati accettati dal server.
     */
    SECURITY_DATA_IS_ACCEPTABLE(335),
    /**
     * 431 - Risorsa non disponibile.
     * La risorsa richiesta non è attualmente disponibile.
     */
    UNAVAILABLE_RESOURCE(431),
    /**
     * 522 - Fallita la negoziazione TLS o crittografia dati richiesta.
     * La negoziazione TLS è fallita, oppure è richiesta la crittografia dei dati.
     */
    BAD_TLS_NEGOTIATION_OR_DATA_ENCRYPTION_REQUIRED(522),
    /**
     * 533 - Negato per motivi di politica.
     * L'azione richiesta è stata rifiutata per ragioni politiche o di sicurezza.
     */
    DENIED_FOR_POLICY_REASONS(533),
    /**
     * 534 - Richiesta negata.
     * La richiesta è stata negata per motivi non specificati.
     */
    REQUEST_DENIED(534),
    /**
     * 535 - Verifica di sicurezza fallita.
     * La verifica dei dati di sicurezza è fallita.
     */
    FAILED_SECURITY_CHECK(535),
    /**
     * 536 - Livello di protezione richiesto non supportato.
     * Il livello di protezione richiesto non è supportato dal server.
     */
    REQUESTED_PROT_LEVEL_NOT_SUPPORTED(536),
    /**
     * 522 - Fallimento nell'impostare la modalità di porta estesa.
     * La modalità di porta estesa (EPSV) non è stata configurata correttamente.
     */
    EXTENDED_PORT_FAILURE(522);

    private final int code;

    /**
     * Restituisce lo stato che rappresenta questo codice {@link FTPReplyCode}.
     * @return il relativo stato di questo codice di risposta
     */
    public Status getStatus() {
        if (this.code >= 500 && this.code < 600) return Status.NEGATIVE_PERMANENT;
        else if (this.code >= 400 && this.code < 500) return Status.NEGATIVE_TRANSIENT;
        else if (this.code >= 200 && this.code < 300) return Status.POSITIVE_COMPLETION;
        else if (this.code >= 300 && this.code < 400) return Status.POSITIVE_INTERMEDIATE;
        else if (this.code >= 100 && this.code < 200) return Status.POSITIVE_PRELIMINARY;
        else if (this.code >= 600 && this.code < 700) return Status.PROTECTED_REPLY_CODE;
        return null;
    }

    /**
     * Restituisce il {@link FTPReplyCode} associato al valore numerico fornito.
     * @param replyCode codice numerico di risposta del protocollo FTP
     * @throws IllegalArgumentException se il codice fornito non è tra quelli dichiarati
     * @return il codice di risposta associato al valore numerico fornito
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
    public enum Status {

        /** Indica un errore permanente, il client non deve ripetere la richiesta. */
        NEGATIVE_PERMANENT,
        /** Indica un errore temporaneo, il client può riprovare la richiesta in un secondo momento. */
        NEGATIVE_TRANSIENT,
        /** Il comando è stato eseguito con successo e non sono richieste ulteriori azioni da parte del client. */
        POSITIVE_COMPLETION,
        /** Il comando è stato accettato ma il server attende ulteriori informazioni o comandi per completare l'operazione. */
        POSITIVE_INTERMEDIATE,
        /**  Il server ha accettato il comando e inizierà l'azione richiesta, ma la risposta finale non è ancora disponibile. */
        POSITIVE_PRELIMINARY,
        /** Indica un codice di risposta che è protetto o soggetto a meccanismi di sicurezza. */
        PROTECTED_REPLY_CODE;
    }

}
