package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.PartnerVO;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.PartnerCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.PartnerSearchVO;
import java.util.ArrayList;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PartnerController extends BaseWebController {

    @Autowired
    private PartnerCRUDService partnerService;

    @RequestMapping(path = "admin/partner", method = RequestMethod.GET)
    public ModelAndView admin_partner_get() {
        ModelAndView mv = this.createView("admin/partner/list");
        mv.addObject("searchForm", new PartnerSearchVO());
        mv.addObject("objectList", new ArrayList<PartnerVO>());
        mv.addObject("addPath", "admin/partner/add");
        return mv;
    }

    @RequestMapping(path = "admin/partner", method = RequestMethod.POST)
    public ModelAndView admin_partner_post(PartnerSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/partner/list");
        PageWrapper<PartnerVO> partners = this.partnerService.retrieve(searchForm, pageable, "admin/partner");
        mv.addObject("objectList", partners.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", partners);
        mv.addObject("addPath", "admin/partner/add");
        mv.addObject("editPath", "admin/partner/edit");
        return mv;
    }

    @RequestMapping(path = "admin/partner/add", method = RequestMethod.GET)
    public ModelAndView admin_partner_add() {
        return this.createFormView(this.partnerService.initVO());
    }

    @RequestMapping(path = "admin/partner/edit/{id}")
    public ModelAndView admin_partner_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.partnerService.findOneVO(id));
    }

    @RequestMapping(value = "admin/partner/save", method = RequestMethod.POST)
    public ModelAndView admin_partner_save(PartnerVO partner) {
        ServiceResponse response;
        try {
            response = this.partnerService.processSave(partner, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, partner);
    }

    private ModelAndView createFormView(ServiceResponse response, PartnerVO partner) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            partner = (PartnerVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(partner);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(PartnerVO vo) {
        ModelAndView mv = this.createView("admin/partner/form");
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/partner/save");
        mv.addObject("crudHomePath", "admin/partner");
        return mv;
    }

}
