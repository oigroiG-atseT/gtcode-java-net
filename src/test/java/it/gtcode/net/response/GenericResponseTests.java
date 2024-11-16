package it.gtcode.net.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.MethodName.class)
class GenericResponseTests {

    @Test
    void GenericResponse() {
        try {

            var expected = new GenericResponse();
            expected.setStatus(Status.UNKNOWN);
            expected.setMessage(Status.UNKNOWN.getMessage());

            Response response = new GenericResponse();

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asSuccess_message", e);
        }
    }

    @Test
    void asSuccess_message() {
        try {

            var expected = new GenericResponse();
            expected.setStatus(Status.SUCCESS);
            expected.setMessage("SUCCESSO");

            Response response = new GenericResponse();

            response.asSuccess(expected.getMessage());

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asSuccess_message", e);
        }
    }

    @Test
    void asSuccess() {
        try {

            var expected = new GenericResponse();
            expected.setStatus(Status.SUCCESS);
            expected.setMessage(null);

            Response response = new GenericResponse();

            response.asSuccess();

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asSuccess", e);
        }
    }

    @Test
    void asError_message() {
        try {

            var expected = new GenericResponse();
            expected.setStatus(Status.ERROR);
            expected.setMessage("ERRORE");

            Response response = new GenericResponse();

            response.asError(expected.getMessage());

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asError_message", e);
        }
    }

    @Test
    void asError() {
        try {

            var expected = new GenericResponse();
            expected.setStatus(Status.ERROR);
            expected.setMessage(Status.ERROR.getMessage());

            Response response = new GenericResponse();

            response.asError();

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asError", e);
        }
    }

    @Test
    void MustBeJsonSerializable() {
        try {

            var response = new GenericResponse();
            response.setStatus(Status.ERROR);
            response.setMessage(Status.ERROR.getMessage());

            var objectMapper = new ObjectMapper();
            var serialized = objectMapper.writeValueAsString(response);

            assertThat(serialized)
                    .contains("\"status\":\"ERROR\"")
                    .contains("\"message\":\"Errore dal server\"");

            var deserialized = objectMapper.readValue(serialized, new TypeReference<GenericResponse>() {});

            assertThat(deserialized).isEqualTo(response);

        } catch (Exception e) {
            fail("MustBeJsonSerializable", e);
        }
    }

}