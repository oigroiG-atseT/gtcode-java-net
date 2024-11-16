package it.gtcode.net.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

/**
 * Rappresenta una risposta contenente un oggetto.
 * @param <T> il tipo dell'oggetto
 * @since 1.0
 * @see GenericResponse
 * @author Giorgio Testa
 */
@EqualsAndHashCode(callSuper = true)
@Getter@Setter @ToString(callSuper = true)
public class SingleResponse<T> extends GenericResponse {

    private T item;

    /**
     * Ottiene l'oggetto contenuto nella risposta.
     * @return l'oggetto contenuto wrappato in un {@linkplain Optional}
     * @since 2.0
     */
    public Optional<T> extractItem() {
        return Optional.ofNullable(item);
    }

    /**
     * Inizializza la risposta con lo stato {@link Status#SUCCESS} e l'oggetto fornito.<br>
     * <p>
     * NOTA:<br>
     * questo metodo è stato chiamato in maniera differente dagli altri {@code asSuccess} poiché se impostato a
     * {@code String} entrerebbe in conflitto con {@link GenericResponse#asSuccess(String)}
     * </p>
     * @param item l'oggetto che la risposta dovrà contenere
     */
    public void asSuccessful(T item) {
        super.asSuccess();
        this.item = item;
    }

}
