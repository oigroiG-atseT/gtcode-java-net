package it.gtcode.net.ftp;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.MethodName.class)
class FTPConfigurationTests {

    @Test
    void FTPConfiguration() {
        try {

            var expected = new FTPConfiguration();
            expected.setServer("127.0.0.1");
            expected.setDirectory(Path.of("/"));
            expected.setPort(21);
            expected.setUsername("username");
            expected.setPassword("password");

            assertThat(expected).hasNoNullFieldsOrProperties();

        } catch (Exception e) {
            fail("FTPConfiguration");
        }
    }

}