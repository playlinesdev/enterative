package br.com.chart.enterative.cron.file;

import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import java.io.IOException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class FileRetrieverController {

    @Autowired
    private FileRetrieverService fileParserService;

    private final String receivedPath;
    private final String fileExtension;
    private final String remoteFileExtension;
    private final String password;
    private final Integer port;
    private final String url;
    private final String username;
    private final String canRun;

    public FileRetrieverController(EnvParameterDAO parameterDAO) {
        receivedPath = parameterDAO.get(ENVIRONMENT_PARAMETER.FILE_SDF_PATH_RECEIVED);
        fileExtension = parameterDAO.get(ENVIRONMENT_PARAMETER.FILE_SDF_EXTENSION);
        remoteFileExtension = parameterDAO.get(ENVIRONMENT_PARAMETER.FILE_SDF_REMOTE_EXTENSION);
        password = parameterDAO.get(ENVIRONMENT_PARAMETER.FILE_SDF_SFTP_PASSWORD);
        port = parameterDAO.get(ENVIRONMENT_PARAMETER.FILE_SDF_SFTP_PORT);
        url = parameterDAO.get(ENVIRONMENT_PARAMETER.FILE_SDF_SFTP_URL);
        username = parameterDAO.get(ENVIRONMENT_PARAMETER.FILE_SDF_SFTP_USERNAME);
        canRun = parameterDAO.get(ENVIRONMENT_PARAMETER.CRON_FILE);
    }

    @Scheduled(cron = "0 0 17 * * *") // Every day at 17:00 GMT+3 (14:00 MST), forever
    public void retrieveFiles() throws IOException {
        this.fileParserService.setFileExtension(fileExtension);
        this.fileParserService.setPassword(password);
        this.fileParserService.setPort(port);
        this.fileParserService.setReceivedPath(receivedPath);
        this.fileParserService.setRemoteFileExtension(remoteFileExtension);
        this.fileParserService.setUrl(url);
        this.fileParserService.setUsername(username);

        if (Objects.equals(canRun, "S")) {
            this.fileParserService.retrieveFiles();
        }
    }
}
