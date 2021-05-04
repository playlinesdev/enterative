package br.com.chart.enterative.service.pdf;

import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import com.lowagie.text.DocumentException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 *
 * @author William Leite
 */
@Service
public class PDFReportService {

    private static final String PREFIX = "classpath://pdfreports/";
    private static final String SUFFIX = ".html";
    private static final TemplateMode TEMPLATE_MODE = TemplateMode.HTML;
    private static final String CHARACTER_ENCODING = "UTF-8";

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private EnvParameterDAO parameterDAO;

    private ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver result = new ClassLoaderTemplateResolver();

        result.setPrefix(PDFReportService.PREFIX);
        result.setSuffix(PDFReportService.SUFFIX);
        result.setTemplateMode(PDFReportService.TEMPLATE_MODE);
        result.setCharacterEncoding(PDFReportService.CHARACTER_ENCODING);

        return result;
    }

    public byte[] generateReportAsByte(String templateName, Map<String, Object> contextVariables, Locale locale) throws DocumentException, IOException {
        Context context = new Context();
        context.setVariables(contextVariables);
        context.setLocale(locale);
        String html = this.templateEngine.process(templateName, context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.getSharedContext().setReplacedElementFactory(new ImageReplacedElementFactory(renderer.getSharedContext().getReplacedElementFactory()));
        renderer.setDocumentFromString(html);
        renderer.layout();
        byte[] pdf;

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            renderer.createPDF(os);
            pdf = os.toByteArray();
        }
        return pdf;
    }

    public String generateReportAsFile(String templateName, Map<String, Object> contextVariables, Locale locale) throws DocumentException, IOException {
        byte[] pdf = this.generateReportAsByte(templateName, contextVariables, locale);

        String path = this.parameterDAO.get(ENVIRONMENT_PARAMETER.REPORT_OUTPUT_PATH);
        String fileName = String.format("%s.pdf", UUID.randomUUID().toString());
        String fullFileName = String.format("%s%s%s", path, File.separator, fileName);

        try (FileOutputStream output = new FileOutputStream(fullFileName)) {
            output.write(pdf);
        }

        return fullFileName;
    }
}
