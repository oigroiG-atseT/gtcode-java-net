package it.gtcode.net.response;

import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.Optional;

/**
 * Rappresenta una risposta contenente un oggetto.
 * @param <T> il tipo dell'oggetto
 * @since 1.0
 * @see GenericResponse
 * @author Giorgio Testa
 */
@EqualsAndHashCode(callSuper = true)
@Setter
public class SingleResponse<T> extends GenericResponse {

    private T item;

    /**
     * Ottiene l'oggetto contenuto nella risposta.
     * @return l'oggetto contenuto wrappato in un {@linkplain Optional}
     */
    public Optional<T> getItem() {
        return Optional.ofNullable(item);
    }

    /**
     * Inizializza la risposta con lo stato {@link BasicStatus#SUCCESS} e l'oggetto fornito.
     * @param item l'oggetto che la risposta dovrà contenere
     * @apiNote questo metodo è stato chiamato in maniera differente dagli altri {@code asSuccess} poiché se impostato a
     * {@code String} entrerebbe in conflitto con {@link GenericResponse#asSuccess(String)}
     */
    public void asSuccessful(T item) {
        super.asSuccess();
        this.item = item;
    }

}
