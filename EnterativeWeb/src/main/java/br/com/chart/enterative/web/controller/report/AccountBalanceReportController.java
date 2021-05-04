package br.com.chart.enterative.web.controller.report;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.service.pdf.PDFReportService;
import br.com.chart.enterative.service.report.AccountBalanceReportService;
import br.com.chart.enterative.vo.search.AccountBalanceSearchVO;
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
public class AccountBalanceReportController extends BaseWebController {

    @Autowired
    private AccountBalanceReportService reportService;

    @Autowired
    private PDFReportService pdfReportService;

    private static final String ACCOUNTBALANCE_REPORT_TEMPLATE = "reports/AccountBalance";

    @RequestMapping(path = "admin/report/accountbalance", method = RequestMethod.GET)
    public ModelAndView account_commission_get() {
        ModelAndView mv = this.createView("admin/report/accountbalance");
        mv.addObject("searchForm", new AccountBalanceSearchVO());
        return mv;
    }

    @RequestMapping(path = "admin/report/accountbalance", method = RequestMethod.POST)
    public ResponseEntity<byte[]> account_commission_report(AccountBalanceSearchVO vo, HttpServletRequest request) throws ParseException {
        Map<String, Object> contextVariables = reportService.assembleReportVariables(loggedUser(), vo);
        byte[] report = null;

        try {
            report = pdfReportService.generateReportAsByte(AccountBalanceReportController.ACCOUNTBALANCE_REPORT_TEMPLATE, contextVariables, this.locale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String fileName = "accountbalance.pdf";
//        headers.setContentDispositionFormData("inline", fileName);
        headers.add("content-disposition", "inline;filename=" + fileName);
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        ResponseEntity<byte[]> response = new ResponseEntity<>(report, headers, HttpStatus.OK);
        return response;
    }
}
