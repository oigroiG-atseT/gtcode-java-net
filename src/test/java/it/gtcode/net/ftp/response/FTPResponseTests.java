package it.gtcode.net.ftp.response;

import it.gtcode.net.response.Status;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.MethodName.class)
class FTPResponseTests {

    @Test
    void asSuccess_codeMessage() {
        try {

            var expected = new FTPResponse();
            expected.setStatus(Status.SUCCESS);
            expected.setReplyCode(FTPReplyCode.SECURITY_DATA_EXCHANGE_COMPLETE);
            expected.setMessage("SECURITY DATA EXCHANGE COMPLETED");

            var response = new FTPResponse();

            response.asSuccess(expected.getReplyCode().getCode(), expected.getMessage());

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asSuccess_codeMessage", e);
        }
    }

    @Test
    void asError_codeMessage() {
        try {

            var expected = new FTPResponse();
            expected.setStatus(Status.ERROR);
            expected.setReplyCode(FTPReplyCode.UNAVAILABLE_RESOURCE);
            expected.setMessage("UNAVAILABLE RESOURCE");

            var response = new FTPResponse();

            response.asError(expected.getReplyCode().getCode(), expected.getMessage());

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asError_codeMessage", e);
        }
    }

    @Test
    void asError_codeMessageException() {
        try {

            var expected = new FTPResponse();
            expected.setStatus(Status.ERROR);
            expected.setReplyCode(FTPReplyCode.UNAVAILABLE_RESOURCE);
            expected.setMessage("UNAVAILABLE RESOURCE");
            expected.setException(new IOException("asError_codeMessageException"));

            var response = new FTPResponse();

            response.asError(expected.getReplyCode().getCode(), expected.getMessage(), expected.getException());

            assertThat(response).isEqualTo(expected);

        } catch (Exception e) {
            fail("asError_codeMessageException", e);
        }
    }

}