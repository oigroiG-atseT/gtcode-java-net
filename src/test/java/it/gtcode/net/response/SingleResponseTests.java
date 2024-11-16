package it.gtcode.net.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
class SingleResponseTests {

    @Test
    void SingleResponse() {
        try {

            var expected = new SingleResponse<String>();
            expected.setStatus(Status.UNKNOWN);
            expected.setMessage(Status.UNKNOWN.getMessage());
            expected.setItem(null);

            var response = new SingleResponse<String>();

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("SingleResponse", e);
        }
    }

    @Test
    void getItemSafely() {
        try {

            var response = new SingleResponse<String>();
            response.setStatus(Status.SUCCESS);
            response.setMessage(null);
            response.setItem("ITEM");

            assertThat(response.extractItem())
                    .isNotEmpty()
                    .contains("ITEM");

        } catch (Exception e) {
            fail("getItem", e);
        }
    }

    @Test
    void asSuccess_item() {
        try {

            var expected = new SingleResponse<String>();
            expected.setStatus(Status.SUCCESS);
            expected.setMessage(null);
            expected.setItem("ITEM");

            var response = new SingleResponse<String>();

            response.asSuccessful("ITEM");

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asSuccess_item", e);
        }
    }

    @Test
    void MustBeJsonSerializable() {
        try {

            var response = new SingleResponse<Map<String, String>>();
            response.setStatus(Status.ERROR);
            response.setMessage(Status.ERROR.getMessage());
            response.setItem(Map.of("KEY1", "VALUE1"));

            var objectMapper = new ObjectMapper();
            var serialized = objectMapper.writeValueAsString(response);

            assertThat(serialized)
                    .contains("\"status\":\"ERROR\"")
                    .contains("\"message\":\"Errore dal server\"")
                    .contains("\"item\":{");

            var deserialized = objectMapper.readValue(serialized, new TypeReference<SingleResponse<Map<String, String>>>() {});

            assertThat(deserialized).isEqualTo(response);

        } catch (Exception e) {
            fail("MustBeJsonSerializable", e);
        }
    }

}