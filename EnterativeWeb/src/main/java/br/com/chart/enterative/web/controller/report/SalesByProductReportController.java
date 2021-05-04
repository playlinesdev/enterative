package br.com.chart.enterative.web.controller.report;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.pdf.PDFReportService;
import br.com.chart.enterative.service.report.SalesByProductReportService;
import br.com.chart.enterative.vo.search.SalesByProductSearchVO;
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
public class SalesByProductReportController extends BaseWebController {

    @Autowired
    private AccountCRUDService accountService;

    @Autowired
    private SalesByProductReportService reportService;

    @Autowired
    private PDFReportService pdfReportService;

    private final String REPORT_TEMPLATE = "reports/SalesByProduct";

    @RequestMapping(path = "admin/report/salesbyproduct", method = RequestMethod.GET)
    public ModelAndView admin_report_salesbyproduct_get() {
        ModelAndView mv = this.createView("admin/report/salesbyproduct");
        mv.addObject("searchForm", this.reportService.initSearchVO());
        mv.addObject("account_list", this.accountService.findByStatusOrderByName(STATUS.ACTIVE));
        return mv;
    }

    @RequestMapping(path = "admin/report/salesbyproduct", method = RequestMethod.POST)
    public ResponseEntity<byte[]> admin_report_salesbyproduct_post(SalesByProductSearchVO vo, HttpServletRequest request) {
        Map<String, Object> contextVariables = this.reportService.assembleReportVariables(vo);
        byte[] report = null;
        try {
            report = pdfReportService.generateReportAsByte(REPORT_TEMPLATE, contextVariables, this.locale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String fileName = "salesbyproduct.pdf";
//        headers.setContentDispositionFormData("inline", fileName);
        headers.add("content-disposition", "inline;filename=" + fileName);
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        ResponseEntity<byte[]> response = new ResponseEntity<>(report, headers, HttpStatus.OK);
        return response;
    }
}
