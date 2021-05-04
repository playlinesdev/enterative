package br.com.chart.enterative.web.controller.report;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.pdf.PDFReportService;
import br.com.chart.enterative.service.report.SalesByAccountReportService;
import br.com.chart.enterative.service.report.SalesByPeriodReportService;
import br.com.chart.enterative.vo.search.SalesByPeriodSearchVO;
import br.com.chart.enterative.vo.search.SalesByProductSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * @author William Leite
 */
@Controller
public class SalesByPeriodReportController extends BaseWebController {

    @Autowired
    private SalesByPeriodReportService reportService;

    @Autowired
    private PDFReportService pdfReportService;

    private final String REPORT_TEMPLATE = "reports/SalesByPeriod";

    @RequestMapping(path = "admin/report/salesbyperiod", method = RequestMethod.GET)
    public ModelAndView admin_report_salesbyperiod_get() {
        ModelAndView mv = this.createView("admin/report/salesbyperiod");
        mv.addObject("searchForm", this.reportService.initSearchVO());
        return mv;
    }

    @RequestMapping(path = "admin/report/salesbyperiod", method = RequestMethod.POST)
    public ResponseEntity<byte[]> admin_report_salesbyperiod_post(SalesByPeriodSearchVO vo, HttpServletRequest request) {
        Map<String, Object> contextVariables = this.reportService.assembleReportVariables(vo);
        byte[] report = null;
        try {
            report = pdfReportService.generateReportAsByte(REPORT_TEMPLATE, contextVariables, this.locale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String fileName = "salesbyperiod.pdf";
        headers.add("content-disposition", "inline;filename=" + fileName);
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        return new ResponseEntity<>(report, headers, HttpStatus.OK);
    }
}