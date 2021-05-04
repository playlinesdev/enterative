package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.AccountTransactionVO;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.service.pdf.PDFReportService;
import br.com.chart.enterative.service.report.AccountTransactionReportService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountTransactionSearchVO;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccountExcerptController extends BaseWebController {

    @Autowired
    private AccountTransactionCRUDService accountTransactionService;

    @Autowired
    private AccountCRUDService accountService;

    @Autowired
    private AccountTransactionReportService reportService;

    @Autowired
    private PDFReportService pdfReportService;

    private final String REPORT_TEMPLATE = "reports/AccountExcerpt";

    @RequestMapping(path = "account/excerpt/report", method = RequestMethod.POST)
    public ResponseEntity<byte[]> account_excerpt_report(AccountTransactionSearchVO vo, HttpServletRequest request) throws ParseException {
        Map<String, Object> contextVariables = this.reportService.assembleExcerptReportVariables(loggedUser(), vo);
        byte[] report = null;

        try {
            report = pdfReportService.generateReportAsByte(REPORT_TEMPLATE, contextVariables, this.locale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String fileName = "accountexcerpt.pdf";
//        headers.setContentDispositionFormData("inline", fileName);
        headers.add("content-disposition", "inline;filename=" + fileName);
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        ResponseEntity<byte[]> response = new ResponseEntity<>(report, headers, HttpStatus.OK);
        return response;
    }

    @RequestMapping(path = "account/excerpt", method = RequestMethod.GET)
    public ModelAndView account_excerpt_get() {
        ModelAndView mv = this.createView("account/excerpt");
        mv.addObject("searchForm", new AccountTransactionSearchVO());
        mv.addObject("objectList", new ArrayList<AccountTransactionVO>());
        mv.addObject("account_list", this.accountService.findByStatusOrderByName(STATUS.ACTIVE));
        return mv;
    }

    @RequestMapping(path = "account/excerpt", method = RequestMethod.POST)
    public ModelAndView account_excerpt(AccountTransactionSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("account/excerpt");
        User user = loggedUser();

        PageWrapper<AccountTransactionVO> transactions = this.accountTransactionService.retrieveTransactions(user, searchForm, pageable, "account/excerpt");

        List<AccountTransactionVO> objectList = new ArrayList<>();
        objectList.addAll(transactions.getContent());

        Long accountID = this.retrieveAccountID(user, searchForm.getAccount());
        if (transactions.isFirstPage() && Objects.nonNull(accountID)) {
            objectList.add(0, this.accountTransactionService.retrieveLastPositionVO(accountID, searchForm, false));
        }

        mv.addObject("objectList", objectList);

        ServiceResponse response = this.accountTransactionService.retrieveTotals(objectList);
        if (Objects.isNull(response.getMessage())) {
            mv.addObject("totals", response.get("totals"));
        } else {
            mv.addObject("errorMessage", response.getMessage());
        }

        mv.addObject("searchForm", searchForm);
        mv.addObject("account_list", this.accountService.findByStatusOrderByName(STATUS.ACTIVE));
        mv.addObject("page", transactions);

        return mv;
    }
}
