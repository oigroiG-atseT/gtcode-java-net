package it.gtcode.net.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una risposta contenente una lista di oggetti.</br>
 * @param <T> il tipo di oggetto contenuto
 * @since 1.0
 * @see GenericResponse
 * @see PaginatedResponse
 * @author Giorgio Testa
 */
@EqualsAndHashCode(callSuper = true)
@Getter@Setter
public class ListResponse<T> extends GenericResponse {

    private List<T> items;

    /**
     * Costruttore di base. Valorizza {@code items} con un {@link ArrayList} vuoto.
     */
    public ListResponse() {
        super();
        this.items = new ArrayList<>();
    }

    /**
     * Inizializza la risposta con lo stato {@link BasicStatus#SUCCESS} e gli oggetti forniti.
     * @param items gli oggetti che la risposta dovrà contenere
     */
    public void asSuccess(List<T> items) {
        super.asSuccess();
        this.items = items;
    }

}
