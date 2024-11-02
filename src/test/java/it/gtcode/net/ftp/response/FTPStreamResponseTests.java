package it.gtcode.net.ftp.response;

import it.gtcode.net.response.BasicStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.MethodName.class)
class FTPStreamResponseTests {

    @Test
    void asSuccess_codeMessageStream() {
        try {

            var expectedStatus = BasicStatus.SUCCESS;
            var expectedReplyCode = FTPReplyCode.COMMAND_OK;
            var expectedMessage = "COMMAND OK";
            var expectedStream = InputStream.nullInputStream();

            var response = new FTPStreamResponse(() -> {});

            response.asSuccess(expectedReplyCode.getCode(), expectedMessage, expectedStream);

            assertThat(response)
                    .returns(expectedStatus, FTPStreamResponse::getStatus)
                    .returns(expectedReplyCode, FTPStreamResponse::getReplyCode)
                    .returns(expectedMessage, FTPStreamResponse::getMessage)
                    .returns(null, FTPStreamResponse::getException)
                    .returns(Optional.of(expectedStream), FTPStreamResponse::getStream);

        } catch (Exception e) {
            fail("asSuccess_codeMessageStream", e);
        }
    }

    @Test
    void getStream() {
        try {

            var stream = InputStream.nullInputStream();
            var response = new FTPStreamResponse(() -> {});
            response.asSuccess(FTPReplyCode.COMMAND_OK.getCode(), "COMMAND OK", stream);

            assertThat(response.getStream()).isNotEmpty().contains(stream);

        } catch (Exception e) {
            fail("getStream", e);
        }
    }

    @Test
    void consume() {
        try {

            var expectedText = "Nugu gurunkilu bard guruntu";
            var callbackFlag = new ArrayList<String>();

            var stream = new ByteArrayInputStream(expectedText.getBytes());
            var response = new FTPStreamResponse(() -> callbackFlag.add("CALLBACK CALLED"));
            response.asSuccess(FTPReplyCode.COMMAND_OK.getCode(), "COMMAND OK", stream);

            response.consume((is) -> {
                try {
                    assertThat(new String(is.readAllBytes())).isEqualTo(expectedText);
                } catch (IOException e) {
                    fail("getStream", e);
                }
            });

            assertThat(callbackFlag).isNotEmpty().hasSize(1);

        } catch (Exception e) {
            fail("getStream", e);
        }
    }

    @Test
    void close() {
        try {

            var callbackFlag = new ArrayList<String>();

            var response = new FTPStreamResponse(() -> callbackFlag.add("CALLBACK CALLED"));
            response.asSuccess(FTPReplyCode.COMMAND_OK.getCode(), "COMMAND OK", InputStream.nullInputStream());

            response.close();

            assertThat(callbackFlag).isNotEmpty().hasSize(1);

        } catch (Exception e) {
            fail("close", e);
        }
    }

    @Test
    void close_error() {
        try {

            var callbackFlag = new ArrayList<String>();

            var response = new FTPStreamResponse(() -> callbackFlag.add("CALLBACK CALLED"));
            response.asSuccess(FTPReplyCode.COMMAND_OK.getCode(), "COMMAND OK", new InputStream() {
                @Override
                public int read() {
                    return 0;
                }

                @Override
                public void close() throws IOException {
                    throw new IOException();
                }
            });

            assertThrows(UncheckedIOException.class, response::close);

            assertThat(callbackFlag).isNotEmpty().hasSize(1);

        } catch (Exception e) {
            fail("close", e);
        }
    }

}