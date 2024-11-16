package it.gtcode.net.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.MethodName.class)
class ListResponseTests {

    @Test
    void ListResponse() {
        try {

            var response = new ListResponse<String>();

            assertThat(response.getItems()).isEqualTo(Collections.emptyList());

        } catch (Exception e) {
            fail("ListResponse", e);
        }
    }

    @Test
    void asSuccess_list() {
        try {

            var expected = new ListResponse<String>();
            expected.setStatus(Status.SUCCESS);
            expected.setMessage(null);
            expected.setItems(List.of("A", "B", "C"));

            var response = new ListResponse<String>();

            response.asSuccess(expected.getItems());

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asSuccess_list", e);
        }
    }

    @Test
    void MustBeJsonSerializable() {
        try {

            var response = new ListResponse<String>();
            response.setStatus(Status.ERROR);
            response.setMessage(Status.ERROR.getMessage());
            response.setItems(List.of("A", "B", "C"));

            var objectMapper = new ObjectMapper();
            var serialized = objectMapper.writeValueAsString(response);

            assertThat(serialized)
                    .contains("\"status\":\"ERROR\"")
                    .contains("\"message\":\"Errore dal server\"")
                    .contains("\"items\":[");

            var deserialized = objectMapper.readValue(serialized, new TypeReference<ListResponse<String>>() {});

            assertThat(deserialized).isEqualTo(response);

        } catch (Exception e) {
            fail("MustBeJsonSerializable", e);
        }
    }

}