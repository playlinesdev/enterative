package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.BHNActivationVO;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_TYPE;
import br.com.chart.enterative.service.crud.BHNActivationCRUDService;
import br.com.chart.enterative.vo.LastSearchVO;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.search.BHNActivationSearchVO;
import java.util.ArrayList;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author William Leite
 */
@Controller
public class BHNActivationController extends BaseWebController {

    @Autowired
    private BHNActivationCRUDService service;

    @RequestMapping(path = "admin/bhn/activation", method = RequestMethod.GET)
    public ModelAndView admin_bhn_activation_get() {
        LastSearchVO<BHNActivationSearchVO> lastSearch = this.retrieveLastSearch("admin/bhn/activation", BHNActivationSearchVO.class);
        if (Objects.nonNull(lastSearch)) {
            return this.admin_bhn_activation_post(lastSearch.getSearchForm(), PageRequest.of(lastSearch.getPageable().getPageNumber(), lastSearch.getPageable().getPageSize()));
        }

        ModelAndView mv = this.createView("admin/bhn/activation/list");
        mv.addObject("searchForm", new BHNActivationSearchVO());
        mv.addObject("objectList", new ArrayList<BHNActivationVO>());
        mv.addObject("status_list", ACTIVATION_STATUS.ordered());
        mv.addObject("type_list", ACTIVATION_TYPE.ordered());
        return mv;
    }

    
    @RequestMapping(path = "admin/bhn/activation", method = RequestMethod.POST)
    public ModelAndView admin_bhn_activation_post(BHNActivationSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/bhn/activation/list");
        PageWrapper<BHNActivationVO> activations = this.service.retrieve(searchForm, pageable, "admin/bhn/activation");

        this.updateLastSearch("admin/bhn/activation", searchForm, pageable);

        mv.addObject("objectList", activations.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", activations);
        mv.addObject("editPath", "admin/bhn/activation");
        mv.addObject("status_list", ACTIVATION_STATUS.ordered());
        mv.addObject("type_list", ACTIVATION_TYPE.ordered());
        return mv;
    }

    @RequestMapping(path = "admin/bhn/activation/code/{code}", method=RequestMethod.GET)
    public ModelAndView admin_bhn_activation_code(@PathVariable("code") String code) {
        ModelAndView mv = this.createView("admin/bhn/activation/form");
        mv.addObject("activeObject", this.service.convertForUI(this.service.findByExternalCode(code)));
        mv.addObject("crudHomePath", "admin/bhn/activation");
        return mv;
    }
    
    @RequestMapping(path = "admin/bhn/activation/{id}", method = RequestMethod.GET)
    public ModelAndView admin_bhn_activation_id(@PathVariable("id") Long id) {
        ModelAndView mv = this.createView("admin/bhn/activation/form");
        mv.addObject("activeObject", this.service.convertForUI(this.service.findOne(id)));
        mv.addObject("crudHomePath", "admin/bhn/activation");
        return mv;
    }
    
    @RequestMapping(path = "admin/bhn/activation/cancel/{id}", method = RequestMethod.GET)
    public ModelAndView admin_bhn_activation_cancel_id(@PathVariable("id") Long id) {
        this.service.cancel(id);
        return this.admin_bhn_activation_id(id);
    }
}
