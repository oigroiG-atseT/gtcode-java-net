package it.gtcode.net.ftp.response;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.MethodName.class)
class FTPReplyCodeTests {

    @Test
    void Status_valueOfCode() {
        try {

            assertThat(FTPReplyCode.Status.valueOfCode(522)).isEqualTo(FTPReplyCode.Status.NEGATIVE_PERMANENT);
            assertThat(FTPReplyCode.Status.valueOfCode(422)).isEqualTo(FTPReplyCode.Status.NEGATIVE_TRANSIENT);
            assertThat(FTPReplyCode.Status.valueOfCode(222)).isEqualTo(FTPReplyCode.Status.POSITIVE_COMPLETION);
            assertThat(FTPReplyCode.Status.valueOfCode(322)).isEqualTo(FTPReplyCode.Status.POSITIVE_INTERMEDIATE);
            assertThat(FTPReplyCode.Status.valueOfCode(122)).isEqualTo(FTPReplyCode.Status.POSITIVE_PRELIMINARY);
            assertThat(FTPReplyCode.Status.valueOfCode(622)).isEqualTo(FTPReplyCode.Status.PROTECTED_REPLY_CODE);

        } catch (Exception e) {
            fail("Status_valueOfCode", e);
        }
    }

    @Test
    void Status_valueOfCode_unknownCode() {
        try {

            var replyCode = 0;

            assertThrows(
                    IllegalArgumentException.class,
                    () -> FTPReplyCode.Status.valueOfCode(replyCode),
                    "Il codice 0 non è tra quelli attualmente supportati"
            );

        } catch (Exception e) {
            fail("Status_valueOfCode_unknownCode", e);
        }
    }

    @Test
    void getStatus() {
        try {

            assertThat(FTPReplyCode.EXTENDED_PORT_FAILURE.getStatus()).isEqualTo(FTPReplyCode.Status.NEGATIVE_PERMANENT);
            assertThat(FTPReplyCode.UNAVAILABLE_RESOURCE.getStatus()).isEqualTo(FTPReplyCode.Status.NEGATIVE_TRANSIENT);
            assertThat(FTPReplyCode.SECURITY_DATA_EXCHANGE_SUCCESSFULLY.getStatus()).isEqualTo(FTPReplyCode.Status.POSITIVE_COMPLETION);
            assertThat(FTPReplyCode.SECURITY_MECHANISM_IS_OK.getStatus()).isEqualTo(FTPReplyCode.Status.POSITIVE_INTERMEDIATE);
            assertThat(FTPReplyCode.SERVICE_NOT_READY.getStatus()).isEqualTo(FTPReplyCode.Status.POSITIVE_PRELIMINARY);

        } catch (Exception e) {
            fail("getStatus", e);
        }
    }

    @Test
    void valueOfCode() {
        try {

            var expected = FTPReplyCode.CANNOT_OPEN_DATA_CONNECTION;

            var actual = FTPReplyCode.valueOfCode(expected.getCode());

            assertThat(actual).isEqualTo(expected);

        } catch (Exception e) {
            fail("valueOfCode", e);
        }
    }

    @Test
    void valueOfCode_unknownCode() {
        try {

            var replyCode = 0;

            assertThrows(
                    IllegalArgumentException.class,
                    () -> FTPReplyCode.valueOfCode(replyCode),
                    "Il codice 0 non è tra quelli attualmente supportati"
            );

        } catch (Exception e) {
            fail("valueOfCode_unknownCode", e);
        }
    }

}