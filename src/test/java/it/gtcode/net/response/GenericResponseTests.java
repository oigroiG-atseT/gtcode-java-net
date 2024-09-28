package it.gtcode.net.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class GenericResponseTests {

    @Test
    void GenericResponse() {
        try {

            var expected = new GenericResponse();
            expected.setStatus(BasicStatus.UNKNOWN);
            expected.setMessage(BasicStatus.UNKNOWN.getMessage());

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
            expected.setStatus(BasicStatus.SUCCESS);
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
            expected.setStatus(BasicStatus.SUCCESS);
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
            expected.setStatus(BasicStatus.ERROR);
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
            expected.setStatus(BasicStatus.ERROR);
            expected.setMessage(BasicStatus.ERROR.getMessage());

            Response response = new GenericResponse();

            response.asError();

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asError", e);
        }
    }

}