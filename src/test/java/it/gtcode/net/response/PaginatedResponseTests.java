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
class PaginatedResponseTests {

    @Test
    void PaginatedResponse() {
        try {

            var expected = new PaginatedResponse<String>();
            expected.setStatus(Status.UNKNOWN);
            expected.setMessage(Status.UNKNOWN.getMessage());
            expected.setItems(Collections.emptyList());
            expected.setTotalCount(0);

            var response = new PaginatedResponse<String>();

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("PaginatedResponse", e);
        }
    }

    @Test
    void asSuccess_paginatedList() {
        try {

            var expected = new PaginatedResponse<String>();
            expected.setStatus(Status.SUCCESS);
            expected.setMessage(Status.UNKNOWN.getMessage());
            expected.setItems(List.of("A", "B", "C"));
            expected.setTotalCount(6);

            var response = new PaginatedResponse<String>();

            response.asSuccess(expected.getItems(), expected.getTotalCount());

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asSuccess_list", e);
        }
    }

    @Test
    void MustBeJsonSerializable() {
        try {

            var response = new PaginatedResponse<String>();
            response.setStatus(Status.ERROR);
            response.setMessage(Status.ERROR.getMessage());
            response.setItems(List.of("A", "B", "C"));
            response.setTotalCount(6);

            var objectMapper = new ObjectMapper();
            var serialized = objectMapper.writeValueAsString(response);

            assertThat(serialized)
                    .contains("\"status\":\"ERROR\"")
                    .contains("\"message\":\"Errore dal server\"")
                    .contains("\"items\":[")
                    .contains("\"totalCount\":6");

            var deserialized = objectMapper.readValue(serialized, new TypeReference<PaginatedResponse<String>>() {});

            assertThat(deserialized).isEqualTo(response);

        } catch (Exception e) {
            fail("MustBeJsonSerializable", e);
        }
    }

}