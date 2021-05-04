package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.service.activationprocess.EpayActivationProcess;
import br.com.chart.enterative.service.pdf.PDFReportService;
import br.com.chart.enterative.service.report.EpayReportService;
import br.com.chart.enterative.vo.epay.EpayGetAttachmentResponse;
import br.com.chart.enterative.vo.search.EpaySaleReportSearchVO;
import java.text.ParseException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author William Leite
 */
@Controller
public class EpayController extends BaseWebController {
    
    @Autowired
    private EpayActivationProcess activationProcess;
    
    @Autowired
    private EpayReportService reportService;

    @Autowired
    private PDFReportService pdfReportService;
    
    private final String REPORTTEMPLATE = "reports/EpaySaleReport";

    @RequestMapping(path = "admin/epay/ticket", method = RequestMethod.GET)
    public ModelAndView admin_epay_ticket() {
        ModelAndView mv = this.createView("admin/epay/ticket");
        mv.addObject("searchForm", new EpaySaleReportSearchVO());
        return mv;
    }

    @RequestMapping(path = "admin/epay/generateticket", method = RequestMethod.GET)
    public ResponseEntity<byte[]> admin_epay_generateticket(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String fileName = "ticket.jpg";
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        
        EpayGetAttachmentResponse response = this.activationProcess.retrieveAttachment();
        ResponseEntity<byte[]> httpEntity = new ResponseEntity<>(response.getResult(), headers, HttpStatus.OK);
        return httpEntity;
    }
    
    @RequestMapping(path = "admin/epay/salereport", method = RequestMethod.POST)
    public ResponseEntity<byte[]> admin_epay_salereport(EpaySaleReportSearchVO searchForm) throws ParseException {
        Map<String, Object> contextVariables = reportService.assembleReportVariables(searchForm);
        byte[] report = null;

        try {
            report = pdfReportService.generateReportAsByte(REPORTTEMPLATE, contextVariables, this.locale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String fileName = "epaysalesreport.pdf";
//        headers.setContentDispositionFormData("inline", fileName);
        headers.add("content-disposition", "inline;filename=" + fileName);
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        ResponseEntity<byte[]> response = new ResponseEntity<>(report, headers, HttpStatus.OK);
        return response;
    }
}