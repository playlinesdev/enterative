package br.com.chart.enterative.cron.file;

import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class FileParserController {

    @Autowired
    private FileParserService fileParserService;

    private final String errorPath;
    private final String processedPath;
    private final String receivedPath;
    private final String filePattern;
    private final String fileExtension;
    private final String canRun;

    public FileParserController(EnvParameterDAO parameterDAO) {
        errorPath = parameterDAO.get(ENVIRONMENT_PARAMETER.FILE_SDF_PATH_ERROR);
        processedPath = parameterDAO.get(ENVIRONMENT_PARAMETER.FILE_SDF_PATH_PROCESSED);
        receivedPath = parameterDAO.get(ENVIRONMENT_PARAMETER.FILE_SDF_PATH_RECEIVED);
        filePattern = parameterDAO.get(ENVIRONMENT_PARAMETER.FILE_SDF_NAME_PATTERN);
        fileExtension = parameterDAO.get(ENVIRONMENT_PARAMETER.FILE_SDF_EXTENSION);
        canRun = parameterDAO.get(ENVIRONMENT_PARAMETER.CRON_FILE);
    }

    @Scheduled(cron = "0 0 0/1 1/1 * *") // Every hour forever
    public void parseFiles() {
        if (Objects.equals(canRun, "S")) {
            this.fileParserService.parseFiles(receivedPath, errorPath, processedPath, fileExtension, filePattern);
        }
    }
    
    @Scheduled(cron = "0 0 0/1 1/1 * *") // Every hour forever
    public void assembleFiles() {
        if (Objects.equals(canRun, "S")) {
            this.fileParserService.assembleFiles();
        }
    }
}
