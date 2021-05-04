package br.com.chart.enterative.cron.file;

import br.com.chart.enterative.dao.SDFFileDAO;
import br.com.chart.enterative.service.base.UserAwareComponent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Setter;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class FileRetrieverService extends UserAwareComponent {

    @Autowired
    private SDFFileDAO fileDAO;
    
    @Setter private String receivedPath;
    @Setter private String fileExtension;
    @Setter private String remoteFileExtension;
    @Setter private String username;
    @Setter private String password;
    @Setter private String url;
    @Setter private Integer port;

    public void retrieveFiles() throws IOException {
        this.log("<retrieveFiles>");
        FTPSClient ftpClient = new FTPSClient(false);
        try {
            ftpClient.setAutodetectUTF8(true);
            this.log("ftp: %s", ftpClient.getReplyString());

            ftpClient.setControlEncoding("UTF-8");
            this.log("ftp: %s", ftpClient.getReplyString());

            ftpClient.connect(this.url, this.port);
            this.log("ftp: %s", ftpClient.getReplyString());

            ftpClient.login(this.username, this.password);
            this.log("ftp: %s", ftpClient.getReplyString());

            // Set protection buffer size
            ftpClient.execPBSZ(0);
            // Set data channel protection to private
            ftpClient.execPROT("P");
            // Enter local passive mode
            ftpClient.enterLocalPassiveMode();
            this.log("ftp: %s", ftpClient.getReplyString());

            // After connection attempt, you should check the reply code to verify
            // success.
            int reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                this.log("<retrieveFiles:denied>");
                return;
            }

//            boolean success = ftpClient.changeWorkingDirectory("./inbox/recon");
//            this.log("ftp: %s", ftpClient.getReplyString());
//            if (!success) {
//                ftpClient.disconnect();
//                this.log("<retrieveFiles:changeWorkingDirectory:false>");
//                return;
//            }
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            this.log("ftp: %s", ftpClient.getReplyString());

            FTPFile[] files = ftpClient.listFiles("/inbox/recon");
            this.log("ftp: %s", ftpClient.getReplyString());

            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                this.log("<retrieveFiles:denied>");
                return;
            }

            if (Objects.isNull(files) || files.length == 0) {
                this.log("<retrieveFiles:listFiles:empty>");
                return;
            }

            String remoteExtension = String.format(".%s", this.remoteFileExtension);
            String localExtension = String.format(".%s", this.fileExtension);

            Arrays.stream(files)
                    .map(f -> f.getName().replace(remoteExtension, ""))
                    .filter(f -> !this.fileDAO.existsByName(String.format("%s%s", f, localExtension)))
                    .forEach((String f) -> {
                        try {
                            this.log("<retrieveFiles:stream[%s]>", f);
                            InputStream is = ftpClient.retrieveFileStream(String.format("/inbox/recon/%s%s", f, remoteExtension));
                            Path targetPath = Paths.get(String.format("%s/%s%s", this.receivedPath, f, localExtension));
                            this.log("<retrieveFiles:stream[%s]", targetPath.toAbsolutePath().toString());
                            java.nio.file.Files.copy(is, targetPath, StandardCopyOption.REPLACE_EXISTING);
                            ftpClient.completePendingCommand();
                            this.log("</retrieveFiles:stream>");
                        } catch (IOException ex) {
                            Logger.getLogger(FileRetrieverService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            this.log("ftp: %s", ftpClient.getReplyString());
        } finally {
            if (Objects.nonNull(ftpClient) && ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        }
        this.log("</retrieveFiles>");
    }
}
