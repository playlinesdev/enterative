package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.AccountTransaction;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.AccountTransactionCategoryVO;
import br.com.chart.enterative.entity.vo.AccountTransactionVO;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_STATUS;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_TYPE;
import br.com.chart.enterative.enums.REPORT_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionCategoryCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionDeadFileCRUDService;
import br.com.chart.enterative.service.pdf.PDFReportService;
import br.com.chart.enterative.service.report.AccountTransactionReportService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountTransactionSearchGroupingVO;
import br.com.chart.enterative.vo.search.AccountTransactionSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author William Leite
 */
@Controller
public class AccountTransactionController extends BaseWebController {

    @Autowired
    private AccountCRUDService accountService;

    @Autowired
    private PDFReportService pdfReportService;

    @Autowired
    private AccountTransactionCategoryCRUDService categoryService;

    @Autowired
    private AccountTransactionCRUDService transactionService;

    @Autowired
    private AccountTransactionDeadFileCRUDService deadFileService;

    @Autowired
    private AccountTransactionReportService reportService;

    @Autowired
    private AccountTransactionCategoryCRUDService accountTransactionCategoryService;

    private final String REPORT_TEMPLATE = "reports/AccountTransaction";

    private ModelAndView assembleView(ModelAndView mv) {
        mv.addObject("account_list", this.accountService.findByStatusOrderByName(STATUS.ACTIVE));
        mv.addObject("category_list", this.categoryService.findAll(Sort.by("name")));
        mv.addObject("type_list", ACCOUNT_TRANSACTION_TYPE.ordered());
        mv.addObject("status_list", ACCOUNT_TRANSACTION_STATUS.ordered());
        mv.addObject("report_type_list", REPORT_TYPE.ordered());
        return mv;
    }

    @RequestMapping(path = "account/transaction", method = RequestMethod.GET)
    public ModelAndView account_transaction_get() {
        ModelAndView mv = this.createView("account/transaction/list");
        mv.addObject("searchForm", this.transactionService.initSearchVO());
        mv.addObject("objectList", new ArrayList<AccountTransactionVO>());
        mv.addObject("addPath", "account/transaction/add");
        return this.assembleView(mv);
    }

    @RequestMapping(path = "account/transaction/report", method = RequestMethod.POST)
    public ResponseEntity<byte[]> account_transaction_report(AccountTransactionSearchVO vo, HttpServletRequest request) throws ParseException {
        Map<String, Object> contextVariables = this.reportService.assembleTransactionReportVariables(loggedUser(), vo);
        byte[] report = null;

        try {
            report = pdfReportService.generateReportAsByte(REPORT_TEMPLATE, contextVariables, this.locale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String fileName = "transactionreport.pdf";
//        headers.setContentDispositionFormData("inline", fileName);
        headers.add("content-disposition", "inline;filename=" + fileName);
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        ResponseEntity<byte[]> response = new ResponseEntity<>(report, headers, HttpStatus.OK);
        return response;
    }

    @RequestMapping(path = "account/transaction/add", method = RequestMethod.GET)
    public ModelAndView account_transaction_add() {
        return this.createFormView(this.transactionService.initVO());
    }

    @RequestMapping(path = "account/transaction/save", method = RequestMethod.POST)
    public ModelAndView account_transaction_save(AccountTransactionVO accountTransactionVO) {
        accountTransactionVO.setTransactionDate(new Date());
        ServiceResponse response = this.transactionService.processSave(accountTransactionVO, this.loggedUserId());
        return this.account_transaction_id(((AccountTransactionVO) response.get("entity")).getId());
    }

    private ModelAndView createFormView(ServiceResponse response, AccountTransactionVO transaction) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            transaction = response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(transaction);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(AccountTransactionVO vo) {
        ModelAndView mv = this.createView("account/transaction/form");
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "account/transaction/save");
        mv.addObject("crudHomePath", "account/transaction");
        mv.addObject("type_list", ACCOUNT_TRANSACTION_TYPE.ordered());
        mv.addObject("status_list", ACCOUNT_TRANSACTION_STATUS.ordered());
        mv.addObject("account_list", this.accountService.findAllVOSorted(Comparator.comparing(AccountVO::getName)).collect(Collectors.toList()));
        mv.addObject("category_list", this.accountTransactionCategoryService.findAllVOSorted(Comparator.comparing(AccountTransactionCategoryVO::getName)).collect(Collectors.toList()));
        return mv;
    }

    @RequestMapping(path = "account/transaction/{id}", method = RequestMethod.GET)
    public ModelAndView account_transaction_id(@PathVariable("id") Long id) {
        ModelAndView mv = this.createView("account/transaction/form");

        AccountTransactionVO vo;
        if (this.transactionService.exists(id)) {
            AccountTransaction entity = this.transactionService.findOne(id);
            vo = this.transactionService.converter().convert(entity);
        } else {
            vo = this.deadFileService.findOneVO(id);
        }

        mv.addObject("activeObject", vo);
        mv.addObject("crudHomePath", "account/transaction");
        return mv;
    }


    @RequestMapping(path = "admin/account/transaction/activate/{id}", method = RequestMethod.GET)
    public ModelAndView account_transaction_activate(@PathVariable("id") Long id) {
        if (this.transactionService.exists(id)) {
            AccountTransaction entity = this.transactionService.findOne(id);
            this.transactionService.activate(entity);
        } else {
            this.deadFileService.activate(id);
        }
        return this.account_transaction_id(id);
    }

    @RequestMapping(path = "admin/account/transaction/cancel/{id}", method = RequestMethod.GET)
    public ModelAndView account_transaction_cancel(@PathVariable("id") Long id) {
        if (this.transactionService.exists(id)) {
            AccountTransaction entity = this.transactionService.findOne(id);
            this.transactionService.cancel(entity);
        } else {
            this.deadFileService.cancel(id);
        }
        return this.account_transaction_id(id);
    }

    @RequestMapping(path = "account/transaction", method = RequestMethod.POST)
    public ModelAndView account_transaction(AccountTransactionSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("account/transaction/list");
        User user = loggedUser();

        List<AccountTransactionVO> transactions = this.transactionService.retrieveTransactions(user, searchForm);
        List<AccountTransactionVO> deadTransactions = this.deadFileService.retrieveTransactions(user, searchForm);

        List<AccountTransactionVO> objectList = new ArrayList<>();
        objectList.addAll(transactions);
        objectList.addAll(deadTransactions);
        objectList.sort(Comparator.comparing(AccountTransactionVO::getTransactionDate));

        if (Arrays.stream(searchForm.getGrouping()).noneMatch(AccountTransactionSearchGroupingVO::isSelected)) {
            mv.addObject("objectList", objectList);
            mv.addObject("grouping", false);
        } else {
            Map<String, Object> hm = this.transactionService.groupList(objectList, searchForm.getGrouping());
            mv.addObject("objectList", hm);
            Map<String, Object> hmTotal = this.transactionService.totalGroupList(hm);
            mv.addObject("objectListTotals", hmTotal);
            mv.addObject("grouping", true);
        }

        ServiceResponse response = this.transactionService.retrieveTotals(objectList);
        if (Objects.isNull(response.getMessage())) {
            mv.addObject("totals", response.get("totals"));
        } else {
            mv.addObject("errorMessage", response.getMessage());
        }

        mv.addObject("searchForm", searchForm);
        mv.addObject("addPath", "account/transaction/add");
//        mv.addObject("page", transactions);

        return this.assembleView(mv);
    }
}
