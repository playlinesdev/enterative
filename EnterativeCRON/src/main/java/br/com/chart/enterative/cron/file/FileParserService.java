package br.com.chart.enterative.cron.file;

import br.com.chart.enterative.dao.SDFFileDAO;
import br.com.chart.enterative.enums.SDF_FILE_STATUS;
import br.com.chart.enterative.entity.SDFDetail;
import br.com.chart.enterative.entity.SDFFile;
import br.com.chart.enterative.entity.SDFHeader;
import br.com.chart.enterative.entity.SDFTrailer;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.converter.FileParserConverterService;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.SDFValidationService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class FileParserService extends UserAwareComponent {

    @Autowired
    private SDFFileDAO sdfFileDAO;

    @Autowired
    private FileParserConverterService fileParserConverterService;
    
    @Autowired
    private SDFValidationService sdfValidationService;

    private final String HEADER_CONSTANT = "HR";
    private final String DETAIL_CONSTANT = "DT";
    private final String TRAILER_CONSTANT = "TR";

    private static String fileExtension;
    private static String filePattern;

    public void assembleFiles() {
        List<SDFFile> files = this.sdfFileDAO.findByStatusOrderByName(SDF_FILE_STATUS.PENDING);
        files.stream().forEach(f -> {
            try {
                this.log(String.format("[%s][%s] Validando arquivo", f.getId(), f.getName()));
                sdfValidationService.assembleFile(f.getId());
                this.log(String.format("[%s][%s] Arquivo validado com sucesso", f.getId(), f.getName()));
            } catch (CRUDServiceException e) {
                this.log(String.format("[%s][%s] Ocorreu um erro ao validar o arquivo", f.getId(), f.getName()));
                e.printStackTrace();
            }
        });
    }
    
    public void parseFiles(String receivedPath, String errorPath, String processedPath, String fileExt, String filePtrn) {
        this.log("Comecando a ler arquivos");

        FileParserService.fileExtension = fileExt;
        FileParserService.filePattern = filePtrn;

        Path rp = Paths.get(receivedPath);
        Path ep = Paths.get(errorPath);
        Path pp = Paths.get(processedPath);

        final boolean isCSV = Objects.equals(fileExtension.toLowerCase(), "csv");
        this.log("isCSV [" + isCSV + "]");

        if (Files.exists(rp) && Files.isDirectory(rp)) {
            this.log("Diretorio de arquivos recebidos [OK]");

            try (Stream<Path> fileList = this.filterFileList(Files.list(rp))) {
                this.log("Iterando na lista de arquivos...");
                fileList.forEach((Path path) -> {
                    boolean error = false;
                    try {
                        this.log("Iniciando leitura do arquivo [" + path.toString() + "]");

                        List<String> lines = Files.lines(path).collect(Collectors.toList());

                        this.log("Lendo cabecalho...");
                        Optional<String> strHeader = lines.stream().filter(l -> l.startsWith(this.HEADER_CONSTANT)).findFirst();
                        SDFHeader header = this.fileParserConverterService.toSDFHeader(strHeader, isCSV);

                        this.log("Lendo detalhes...");
                        List<String> strDetails = lines.stream().filter(l -> l.startsWith(this.DETAIL_CONSTANT)).collect(Collectors.toList());
                        List<SDFDetail> details = this.fileParserConverterService.toSDFDetails(strDetails, isCSV);

                        this.log("Lendo rodape...");
                        Optional<String> strTrailer = lines.stream().filter(l -> l.startsWith(this.TRAILER_CONSTANT)).findFirst();
                        SDFTrailer trailer = this.fileParserConverterService.toSDFTrailer(strTrailer, isCSV);

                        this.log("Persistindo arquivo...");
                        SDFFile file = this.createSDFFile(path.getFileName().toString(), header, details, trailer);
                        this.sdfFileDAO.saveAndFlush(file, this.systemUserId());

                        this.log("Arquivo lido com sucesso! [" + path.toString() + "]");
                    } catch (IllegalArgumentException e) {
                        this.log("Erro ao ler o arquivo! [" + path.toString() + "][" + e.toString() + "]");
                        error = true;
                    } catch (IOException e) {
                        this.log("Erro ao abrir o arquivo para leitura! [" + path.toString() + "][" + e.toString() + "]");
                        error = true;
                    } catch (Exception e) {
                        this.log("Erro inesperado! [" + path.toString() + "][" + e.toString() + "]");
                        error = true;
                    } finally {
                        try {
                            this.log("Transferindo arquivo...");
                            Path dest;
                            if (error) {
                                dest = ep.resolve(path.getFileName());
                            } else {
                                dest = pp.resolve(path.getFileName());
                            }

                            Files.move(path, dest, StandardCopyOption.REPLACE_EXISTING);
                            this.log("Arquivo transferido com sucesso de [" + path.toString() + "] para [" + dest.toString() + "]");
                        } catch (Exception e) {
                            this.log("Erro ao transferir o arquivo! [" + path.toString() + "][" + error + "][" + e.toString() + "]");
                        }
                    }
                });
            } catch (Exception e) {
                this.log("Erro ao listar os arquivos do diretorio! [" + e.toString() + "]");
            }
        } else {
            this.log("Caminho de recebidos nao existe ou nao e um diretorio! [" + receivedPath + "]");
        }

        this.log("----------------------- END -----------------------");
    }

    private SDFFile createSDFFile(String fileName, SDFHeader header, List<SDFDetail> details, SDFTrailer trailer) {
        SDFFile result = new SDFFile();

        result.setCreatedAt(new Date());

        details.stream().forEach(d -> {
            d.setFile(result);
        });
        result.setDetails(details);

        header.setFile(result);
        result.setHeader(header);

        result.setName(fileName);
        result.setStatus(SDF_FILE_STATUS.PENDING);

        trailer.setFile(result);
        result.setTrailer(trailer);

        return result;
    }

    private Stream<Path> filterFileList(Stream<Path> fileList) {
        return fileList.filter(FileParserService::startsWithPattern).filter(FileParserService::endsWithExtension);
    }

    public static boolean endsWithExtension(Path path) {
        System.out.println(String.format("Matching [%s] with [%s]", path.getFileName().toString().toLowerCase(), fileExtension.toLowerCase()));
        return path.getFileName().toString().toLowerCase().endsWith(fileExtension.toLowerCase());
    }

    public static boolean startsWithPattern(Path path) {
        System.out.println(String.format("Matching [%s] with [%s]", path.getFileName().toString().toLowerCase(), filePattern.toLowerCase()));
        return path.getFileName().toString().toLowerCase().startsWith(filePattern.toLowerCase());
    }
}
