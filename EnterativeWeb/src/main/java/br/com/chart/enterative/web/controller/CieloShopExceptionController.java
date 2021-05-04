package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.CieloShopExceptionVO;
import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.CieloShopExceptionCRUDService;
import br.com.chart.enterative.service.crud.ShopCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.CieloShopExceptionSearchVO;
import java.util.ArrayList;
import java.util.Comparator;
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
 * @author William Leite
 */
@Controller
public class CieloShopExceptionController extends BaseWebController {

    @Autowired
    private CieloShopExceptionCRUDService cieloShopExceptionCRUDService;
    
    @Autowired
    private ShopCRUDService shopCRUDService;

    @RequestMapping(path = "admin/cieloshopexception", method = RequestMethod.GET)
    public ModelAndView admin_cieloshopexception_get() {
        ModelAndView mv = this.createView("admin/cieloshopexception/list");
        mv.addObject("searchForm", new CieloShopExceptionSearchVO());
        mv.addObject("objectList", new ArrayList<CieloShopExceptionVO>());
        mv.addObject("addPath", "admin/cieloshopexception/add");
        return mv;
    }

    @RequestMapping(path = "admin/cieloshopexception", method = RequestMethod.POST)
    public ModelAndView admin_cieloshopexception_post(CieloShopExceptionSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/cieloshopexception/list");
        PageWrapper<CieloShopExceptionVO> exceptions = this.cieloShopExceptionCRUDService.retrieve(searchForm, pageable, "admin/cieloshopexception");
        mv.addObject("objectList", exceptions.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", exceptions);
        mv.addObject("addPath", "admin/cieloshopexception/add");
        mv.addObject("editPath", "admin/cieloshopexception/edit");
        return mv;
    }

    @RequestMapping(path = "admin/cieloshopexception/add", method = RequestMethod.GET)
    public ModelAndView admin_cieloshopexception_add() {
        return this.createFormView(this.cieloShopExceptionCRUDService.initVO());
    }

    @RequestMapping(path = "admin/cieloshopexception/edit/{id}")
    public ModelAndView admin_cieloshopexception_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.cieloShopExceptionCRUDService.findOneVO(id));
    }

    @RequestMapping(value = "admin/cieloshopexception/save", method = RequestMethod.POST)
    public ModelAndView admin_cieloshopexception_save(CieloShopExceptionVO exception) {
        ServiceResponse response;
        try {
            response = this.cieloShopExceptionCRUDService.processSave(exception, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, exception);
    }

    private ModelAndView createFormView(ServiceResponse response, CieloShopExceptionVO exception) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            exception = (CieloShopExceptionVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(exception);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(CieloShopExceptionVO vo) {
        ModelAndView mv = this.createView("admin/cieloshopexception/form");
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/cieloshopexception/save");
        mv.addObject("crudHomePath", "admin/cieloshopexception");
        mv.addObject("shop_list", this.shopCRUDService.findAllVOSorted(Comparator.comparing(ShopVO::getName)).collect(Collectors.toList()));
        return mv;
    }
}
