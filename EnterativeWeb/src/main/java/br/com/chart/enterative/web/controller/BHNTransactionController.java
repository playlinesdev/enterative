package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.BHNTransaction;
import br.com.chart.enterative.entity.vo.BHNTransactionVO;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import br.com.chart.enterative.service.crud.BHNTransactionCRUDService;
import br.com.chart.enterative.service.crud.ResourceCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.search.BHNTransactionSearchVO;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Pedro Araujo
 */
@Controller
public class BHNTransactionController extends BaseWebController {

    @Autowired
    private BHNTransactionCRUDService transactionService;

    @Autowired
    private ResourceCRUDService resourceService;

    @RequestMapping(method = RequestMethod.GET, path = "admin/bhn/transaction")
    public ModelAndView bhn_transaction_get() {
        ModelAndView mv = this.createView("admin/bhn/transaction/list");
        mv.addObject("searchForm", new BHNTransactionSearchVO());
        mv.addObject("objectList", new ArrayList<BHNTransactionVO>());
        mv.addObject("direction_list", TRANSACTION_DIRECTION.ordered());
        mv.addObject("resource_list", this.resourceService.findAllVO().collect(Collectors.toList()));
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST, path = "admin/bhn/transaction")
    public ModelAndView bhn_transaction_post(BHNTransactionSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/bhn/transaction/list");
        PageWrapper<BHNTransactionVO> transactions = this.transactionService.retrieveTransactions(searchForm, pageable, "admin/bhn/transaction");
        mv.addObject("searchForm", new BHNTransactionSearchVO());
        mv.addObject("objectList", transactions.getContent());
        mv.addObject("direction_list", TRANSACTION_DIRECTION.ordered());
        mv.addObject("resource_list", this.resourceService.findAllVO().filter(r -> Objects.nonNull(r)).collect(Collectors.toList()));
        mv.addObject("page", transactions);
        return mv;
    }

    @RequestMapping(path = "admin/bhn/transaction/{id}", method = RequestMethod.GET)
    public ModelAndView bhn_transaction_id(@PathVariable("id") Long id) {
        ModelAndView mv = this.createView("admin/bhn/transaction/form");

        BHNTransaction entity = this.transactionService.findOne(id);
        BHNTransactionVO vo;
        if (Objects.nonNull(entity)) {
            vo = this.transactionService.converter().convert(entity);
            mv.addObject("activeObject", vo);
            mv.addObject("crudHomePath", "admin/bhn/transaction");
        }
        return mv;
    }
}
