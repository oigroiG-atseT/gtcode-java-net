package it.gtcode.net.ftp;

import lombok.Cleanup;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.*;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.MethodName.class)
class FTPSessionFactoryApacheTests {

    public static FakeFtpServer fakeFtpServer1;
    public static FakeFtpServer fakeFtpServer2;

    FTPConfiguration getBaseConfiguration() {
        var configuration = new FTPConfiguration();
        configuration.setServer("127.0.0.1");
        configuration.setDirectory(Path.of("/"));
        configuration.setPort(21);
        configuration.setUsername("username");
        configuration.setPassword("password");
        return configuration;
    }

    FTPConfiguration getFTPClientConfiguration() {
        var configuration = new FTPConfiguration();
        configuration.setServer("127.0.0.1");
        configuration.setDirectory(Path.of("/"));
        configuration.setPort(22);
        configuration.setUsername("username2");
        configuration.setPassword("password2");
        return configuration;
    }

    @BeforeAll
    public static void beforeAll() {
        fakeFtpServer1 = new FakeFtpServer();
        fakeFtpServer1.setServerControlPort(21);
        fakeFtpServer1.addUserAccount(new UserAccount("username", "password", "/share"));

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/share"));
        fakeFtpServer1.setFileSystem(fileSystem);

        fakeFtpServer1.start();

        fakeFtpServer2 = new FakeFtpServer();
        fakeFtpServer2.setServerControlPort(22);
        fakeFtpServer2.addUserAccount(new UserAccount("username2", "password2", "/share"));

        fakeFtpServer2.setFileSystem(fileSystem);

        fakeFtpServer2.start();
    }

    @AfterAll
    public static void afterAll() {
        fakeFtpServer1.stop();
        fakeFtpServer2.stop();
    }

    @Test
    void FTPSessionFactoryApache() {
        try {

            var configuration = this.getBaseConfiguration();

            var factory = new FTPSessionFactoryApache(configuration);

            assertThat(factory).isNotNull().hasNoNullFieldsOrProperties();

        } catch (Exception e) {
            fail("FTPSessionFactoryApache", e);
        }
    }

    @Test
    void FTPSessionFactoryApache_fail() {
        try {

            assertThrows(
                    NullPointerException.class,
                    () -> new FTPSessionFactoryApache(null)
            );

        } catch (Exception e) {
            fail("FTPSessionFactoryApache_fail", e);
        }
    }

    @Test
    void getConfiguration() {
        try {

            var expected = this.getBaseConfiguration();
            var factory = new FTPSessionFactoryApache(expected);

            FTPConfiguration actual = factory.getConfiguration();

            assertThat(actual).isEqualTo(expected);

        } catch (Exception e) {
            fail("getConfiguration", e);
        }
    }

    @Test
    void openSession() {
        try {

            var configuration = this.getBaseConfiguration();
            var factory = new FTPSessionFactoryApache(configuration);

            @Cleanup FTPSession actual = factory.openSession();

            assertThat(actual).isNotNull();

        } catch (Exception e) {
            fail("openSession", e);
        }
    }

    @Test
    void openSession_configuration() {
        try {

            var configuration = this.getBaseConfiguration();
            var factory = new FTPSessionFactoryApache(configuration);
            var clientConfiguration = this.getFTPClientConfiguration();


            @Cleanup FTPSession actual = factory.openSession(clientConfiguration);

            assertThat(actual)
                    .isNotNull()
                    .returns(clientConfiguration, FTPSession::getConfiguration);

        } catch (Exception e) {
            fail("openSession_configuration", e);
        }
    }

    @Test
    void openSession_configurationClient() {
        try {

            var configuration = this.getBaseConfiguration();
            var factory = new FTPSessionFactoryApache(configuration);
            var clientConfiguration = this.getFTPClientConfiguration();

            var client = new FTPClient();
            client.connect(clientConfiguration.getServer(), clientConfiguration.getPort());
            client.login(clientConfiguration.getUsername(), clientConfiguration.getPassword());

            @Cleanup FTPSession actual = factory.openSession(clientConfiguration, client);

            assertThat(actual)
                    .isNotNull()
                    .returns(clientConfiguration, FTPSession::getConfiguration);

        } catch (Exception e) {
            fail("openSession_configurationClient", e);
        }
    }

    @Test
    @SuppressWarnings("resource")
    void openSession_configurationClient_fail() {
        try {

            var configuration = this.getBaseConfiguration();
            var factory = new FTPSessionFactoryApache(configuration);
            var clientConfiguration = this.getFTPClientConfiguration();

            var client = "Indubbiamente un FTPClient di Apache";

            assertThrows(
                    IllegalArgumentException.class,
                    () -> factory.openSession(clientConfiguration, client)
            );

        } catch (Exception e) {
            fail("openSession_configurationClient_fail", e);
        }
    }

}
