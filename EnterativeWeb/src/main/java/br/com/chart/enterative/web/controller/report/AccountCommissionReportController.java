package br.com.chart.enterative.web.controller.report;

import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.vo.search.AccountCommissionSearchVO;
import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.pdf.PDFReportService;
import br.com.chart.enterative.service.report.AccountCommissionReportService;
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
public class AccountCommissionReportController extends BaseWebController {

    @Autowired
    private AccountCommissionReportService reportService;

    @Autowired
    private AccountCRUDService accountService;

    @Autowired
    private PDFReportService pdfReportService;

    private static final String CLIENTCOMMISSION_REPORT_TEMPLATE = "reports/AccountCommission";

    @RequestMapping(path = "account/commission")
    public ModelAndView account_commission_get() {
        ModelAndView mv = this.createView("account/report/commission");
        mv.addObject("searchForm", new AccountCommissionSearchVO());
        mv.addObject("account_list", this.accountService.findByStatusOrderByName(STATUS.ACTIVE));
        return mv;
    }

    @RequestMapping(path = "account/commission/report", method = RequestMethod.POST)
    public ResponseEntity<byte[]> account_commission_report(AccountCommissionSearchVO vo, HttpServletRequest request) throws ParseException {
        Map<String, Object> contextVariables = reportService.assembleReportVariables(loggedUser(), vo);
        byte[] report = null;

        try {
            report = pdfReportService.generateReportAsByte(AccountCommissionReportController.CLIENTCOMMISSION_REPORT_TEMPLATE, contextVariables, this.locale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String fileName = "accountcommission.pdf";
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        ResponseEntity<byte[]> response = new ResponseEntity<>(report, headers, HttpStatus.OK);
        return response;
    }
}
