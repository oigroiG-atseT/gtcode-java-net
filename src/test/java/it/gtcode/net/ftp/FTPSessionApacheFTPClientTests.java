package it.gtcode.net.ftp;

import it.gtcode.net.ftp.response.FTPReplyCode;
import it.gtcode.net.ftp.response.FTPResponse;
import it.gtcode.net.ftp.response.FTPStreamResponse;
import it.gtcode.net.response.BasicStatus;
import lombok.Cleanup;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.*;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.MethodName.class)
class FTPSessionApacheFTPClientTests {

    public static FakeFtpServer fakeFtpServer;

    FTPConfiguration getBaseConfiguration(String connectionDirectory) {
        var configuration = new FTPConfiguration();
        configuration.setServer("localhost");
        configuration.setDirectory(connectionDirectory != null ? Path.of(connectionDirectory) : null);
        configuration.setPort(21);
        configuration.setUsername("username");
        configuration.setPassword("password");
        return configuration;
    }

    public static final Map<String, String> FILES = Map.of(
            "toDownload.txt", "toDownload-1234567890",
            "toDelete.txt", "toDelete-1234567890"
    );

    @BeforeAll
    public static void beforeAll() {
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.setServerControlPort(21);
        fakeFtpServer.addUserAccount(new UserAccount("username", "password", "/share"));

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/share"));
        fileSystem.add(new DirectoryEntry("/share/internal"));
        fileSystem.add(new DirectoryEntry("/share/internal2/sub-internal"));
        fileSystem.add(new FileEntry("/share/internal/toDownload.txt", FILES.get("toDownload.txt")));
        fileSystem.add(new FileEntry("/share/toDelete.txt", FILES.get("toDelete.txt")));
        fakeFtpServer.setFileSystem(fileSystem);

        fakeFtpServer.start();
    }

    @AfterAll
    public static void afterAll() {
        fakeFtpServer.stop();
    }

    @Test
    void FTPSession_ApacheFTPClient__configuration() {
        try {

            var configuration = this.getBaseConfiguration(null);

            @Cleanup var session = new FTPSession_ApacheFTPClient(configuration);

            assertThat(session)
                    .returns(true, FTPSession::isOpen)
                    .returns(Path.of("/"), FTPSession::getRoot);

        } catch (Exception e) {
            fail("FTPSession_ApacheFTPClient__configuration", e);
        }
    }

    @Test
    void FTPSession_ApacheFTPClient__configurationWithDirectory() {
        try {

            Path expectedRoot = Path.of("internal2/sub-internal");

            var configuration = this.getBaseConfiguration(expectedRoot.toString());

            @Cleanup var session = new FTPSession_ApacheFTPClient(configuration);

            assertThat(session)
                    .returns(true, FTPSession::isOpen)
                    .returns(expectedRoot, FTPSession::getRoot);

        } catch (Exception e) {
            fail("FTPSession_ApacheFTPClient__configurationWithDirectory", e);
        }
    }

    @Test
    void FTPSession_ApacheFTPClient__configurationFTPClient() {
        try {

            var configuration = this.getBaseConfiguration(null);

            var client = new FTPClient();
            try {
                client.connect(configuration.getServer(), configuration.getPort());
                client.login(configuration.getUsername(), configuration.getPassword());
            } catch (IOException ioe) {
                fail("FTPSession_ApacheFTPClient__configurationFTPClient", ioe);
            }

            @Cleanup var session = new FTPSession_ApacheFTPClient(configuration, client);

            assertThat(session)
                    .returns(true, FTPSession::isOpen)
                    .returns(Path.of("/"), FTPSession::getRoot);

        } catch (Exception e) {
            fail("FTPSession_ApacheFTPClient__configurationFTPClient", e);
        }
    }

    @Test
    void getConfiguration() {
        try {

            var configuration = this.getBaseConfiguration("internal");

            @Cleanup var session = new FTPSession_ApacheFTPClient(configuration);

            assertThat(session.getConfiguration()).isEqualTo(configuration);

        } catch (Exception e) {
            fail("getConfiguration", e);
        }
    }

    @Test
    void getRoot() {
        try {

            var configuration = this.getBaseConfiguration("internal");

            @Cleanup var session = new FTPSession_ApacheFTPClient(configuration);

            assertThat(session.getRoot()).isEqualTo(configuration.getDirectory());

        } catch (Exception e) {
            fail("getRoot", e);
        }
    }

    @Test
    void download() {
        try {

            var configuration = this.getBaseConfiguration(null);

            @Cleanup var session = new FTPSession_ApacheFTPClient(configuration);

            FTPStreamResponse response = session.download(Path.of("internal/toDownload.txt"));

            assertThat(response)
                    .returns(null, FTPStreamResponse::getException)
                    .returns(FTPReplyCode.Status.POSITIVE_PRELIMINARY, (item) -> item.getReplyCode().getStatus())
                    .returns(BasicStatus.SUCCESS, FTPStreamResponse::getStatus);

            response.consume(stream -> assertThat(stream).hasBinaryContent(FILES.get("toDownload.txt").getBytes()));

        } catch (Exception e) {
            fail("download", e);
        }
    }

    @Test
    void upload() {
        try {

            var configuration = this.getBaseConfiguration(null);

            @Cleanup var session = new FTPSession_ApacheFTPClient(configuration);

            FTPResponse response = session.upload(Path.of("src/test/resources/ftp/toUpload.txt"));

            assertThat(response)
                    .returns(null, FTPResponse::getException)
                    .returns(FTPReplyCode.Status.POSITIVE_COMPLETION, (item) -> item.getReplyCode().getStatus())
                    .returns(BasicStatus.SUCCESS, FTPResponse::getStatus);

            assertThat(fakeFtpServer.getFileSystem().exists("/share/toUpload.txt")).isTrue();

        } catch (Exception e) {
            fail("upload", e);
        }
    }

    @Test
    void upload_target() {
        try {

            var configuration = this.getBaseConfiguration(null);

            @Cleanup var session = new FTPSession_ApacheFTPClient(configuration);

            FTPResponse response = session.upload(
                    Path.of("src/test/resources/ftp/toUpload.txt"),
                    Path.of("internal2/sub-internal")
            );

            assertThat(response)
                    .returns(null, FTPResponse::getException)
                    .returns(FTPReplyCode.Status.POSITIVE_COMPLETION, (item) -> item.getReplyCode().getStatus())
                    .returns(BasicStatus.SUCCESS, FTPResponse::getStatus);

            assertThat(fakeFtpServer.getFileSystem().exists("/share/internal2/sub-internal/toUpload.txt")).isTrue();

        } catch (Exception e) {
            fail("upload_target", e);
        }
    }

    @Test
    void upload_stream() {
        try {

            var configuration = this.getBaseConfiguration(null);

            @Cleanup var stream = Files.newInputStream(Path.of("src/test/resources/ftp/toUpload.txt"));
            @Cleanup var session = new FTPSession_ApacheFTPClient(configuration);

            FTPResponse response = session.upload(
                    Path.of("toUploadStream.txt"),
                    stream
            );

            assertThat(response)
                    .returns(null, FTPResponse::getException)
                    .returns(FTPReplyCode.Status.POSITIVE_COMPLETION, (item) -> item.getReplyCode().getStatus())
                    .returns(BasicStatus.SUCCESS, FTPResponse::getStatus);

            assertThat(fakeFtpServer.getFileSystem().exists("/share/toUploadStream.txt")).isTrue();

        } catch (Exception e) {
            fail("upload_stream", e);
        }
    }


    @Test
    void upload_targetStream() {
        try {

            var configuration = this.getBaseConfiguration(null);

            @Cleanup var stream = Files.newInputStream(Path.of("src/test/resources/ftp/toUpload.txt"));
            @Cleanup var session = new FTPSession_ApacheFTPClient(configuration);

            FTPResponse response = session.upload(
                    Path.of("toUploadStream.txt"),
                    stream,
                    Path.of("/internal2/sub-internal")
            );

            assertThat(response)
                    .returns(null, FTPResponse::getException)
                    .returns(FTPReplyCode.Status.POSITIVE_COMPLETION, (item) -> item.getReplyCode().getStatus())
                    .returns(BasicStatus.SUCCESS, FTPResponse::getStatus);

            assertThat(fakeFtpServer.getFileSystem().exists("/share/internal2/sub-internal/toUploadStream.txt")).isTrue();

        } catch (Exception e) {
            fail("upload_targetStream", e);
        }
    }

    @Test
    void delete() {
        try {

            var configuration = this.getBaseConfiguration(null);

            @Cleanup var session = new FTPSession_ApacheFTPClient(configuration);

            FTPResponse response = session.delete(Path.of("toDelete.txt"));

            assertThat(response)
                    .returns(null, FTPResponse::getException)
                    .returns(FTPReplyCode.Status.POSITIVE_COMPLETION, (item) -> item.getReplyCode().getStatus())
                    .returns(BasicStatus.SUCCESS, FTPResponse::getStatus);

            assertThat(fakeFtpServer.getFileSystem().exists("/share/toDelete.txt")).isFalse();

        } catch (Exception e) {
            fail("delete", e);
        }
    }

    @Test
    void execute() {
        try {

            var configuration = this.getBaseConfiguration(null);

            @Cleanup var session = new FTPSession_ApacheFTPClient(configuration);

            FTPResponse response = session.execute("chmod 777 internal/toDownload.txt");

            assertThat(response)
                    .returns(null, FTPResponse::getException)
                    .returns(FTPReplyCode.Status.POSITIVE_COMPLETION, (item) -> item.getReplyCode().getStatus())
                    .returns(BasicStatus.SUCCESS, FTPResponse::getStatus);

        } catch (Exception e) {
            fail("execute", e);
        }
    }

    @Test
    void close() {
        try {

            var configuration = this.getBaseConfiguration(null);

            @Cleanup var session = new FTPSession_ApacheFTPClient(configuration);

            session.close();

            assertThat(session).returns(false, FTPSession::isOpen);

            session.close();

        } catch (Exception e) {
            fail("close", e);
        }
    }

}