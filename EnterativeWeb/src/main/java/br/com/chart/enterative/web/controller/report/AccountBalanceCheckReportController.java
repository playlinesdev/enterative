package br.com.chart.enterative.web.controller.report;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.service.pdf.PDFReportService;
import br.com.chart.enterative.service.report.AccountBalanceCheckReportService;
import br.com.chart.enterative.service.report.AccountBalanceReportService;
import br.com.chart.enterative.vo.report.ReportProgress;
import br.com.chart.enterative.vo.search.AccountBalanceCheckSearchVO;
import br.com.chart.enterative.vo.search.AccountBalanceSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.Duration;
import java.util.Map;

/**
 *
 * @author William Leite
 */
@Controller
public class AccountBalanceCheckReportController extends BaseWebController {

    @Autowired
    private AccountBalanceCheckReportService reportService;

    @Autowired
    private PDFReportService pdfReportService;

    private static final String ACCOUNTBALANCECHECK_REPORT_TEMPLATE = "reports/AccountBalanceCheck";

    @RequestMapping(path = "admin/report/accountbalancecheck", method = RequestMethod.GET)
    public ModelAndView account_commission_get() {
        ModelAndView mv = this.createView("admin/report/accountbalancecheck");
        mv.addObject("searchForm", new AccountBalanceCheckSearchVO());
        return mv;
    }

    @RequestMapping(path = "admin/report/accountbalancecheck", method = RequestMethod.POST)
    public ResponseEntity<byte[]> account_commission_report(AccountBalanceCheckSearchVO vo, HttpServletRequest request) throws ParseException {
        Map<String, Object> contextVariables = reportService.assembleReportVariables(loggedUser(), vo);
        byte[] report = null;

        try {
            report = pdfReportService.generateReportAsByte(AccountBalanceCheckReportController.ACCOUNTBALANCECHECK_REPORT_TEMPLATE, contextVariables, this.locale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String fileName = "accountbalancecheck.pdf";
        headers.add("content-disposition", "inline;filename=" + fileName);
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        return new ResponseEntity<>(report, headers, HttpStatus.OK);
    }

    @RequestMapping(path = "admin/report/accountbalancecheck/progress", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ReportProgress> streamProgress(@AuthenticationPrincipal User userDetails) {
        return Flux.interval(Duration.ofSeconds(1))
                .map(i -> this.reportService.getProgress(userDetails))
                .takeUntil(ReportProgress::isDone)
                .onBackpressureDrop();
    }
}
