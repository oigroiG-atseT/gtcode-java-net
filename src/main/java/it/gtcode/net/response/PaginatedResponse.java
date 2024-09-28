package it.gtcode.net.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Rappresenta una risposta contenente una lista di oggetti ottenuti da una richiesta che restituisce un risultato paginato.
 * @param <T> il tipo di oggetto contenuto
 * @since 1.0
 * @see ListResponse
 * @author Giorgio Testa
 */
@EqualsAndHashCode(callSuper = true)
@Getter@Setter
public class PaginatedResponse<T> extends ListResponse<T> {

    /** Il numero totale di elementi presenti nel server. */
    private int totalCount;

    /**
     * Costruttore di base. Valorizza {@code items} con un {@link java.util.ArrayList} vuoto e {@code totalCount} a {@code 0}.
     */
    public PaginatedResponse() {
        super();
        this.totalCount = 0;
    }

    /**
     * Inizializza la risposta con lo stato {@link BasicStatus#SUCCESS} ed i parametri forniti.
     * @param items gli oggetti che la risposta dovrà contenere
     * @param totalCount numero di elementi presenti nel server
     */
    public void asSuccess(List<T> items, int totalCount) {
        super.asSuccess(items);
        this.totalCount = totalCount;
    }

}
