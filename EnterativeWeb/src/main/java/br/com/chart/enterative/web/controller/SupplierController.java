package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.SupplierVO;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.EnvParameterCRUDService;
import br.com.chart.enterative.service.crud.SupplierCRUDService;
import br.com.chart.enterative.service.crud.UserCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.SupplierSearchVO;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;
import org.springframework.data.domain.Pageable;

@Controller
public class SupplierController extends BaseWebController {

    @Autowired
    private SupplierCRUDService supplierCRUDService;

    @RequestMapping(path = "admin/supplier", method = RequestMethod.GET)
    public ModelAndView admin_supplier_get() {
        ModelAndView mv = this.createView("admin/supplier/list");
        mv.addObject("searchForm", new SupplierSearchVO());
        mv.addObject("objectList", new ArrayList<SupplierVO>());
        mv.addObject("addPath", "admin/supplier/add");
        return mv;
    }

    @RequestMapping(path = "admin/supplier", method = RequestMethod.POST)
    public ModelAndView admin_supplier_post(SupplierSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/supplier/list");
        PageWrapper<SupplierVO> suppliers = this.supplierCRUDService.retrieve(searchForm, pageable, "admin/supplier");
        mv.addObject("objectList", suppliers.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", suppliers);
        mv.addObject("addPath", "admin/supplier/add");
        mv.addObject("editPath", "admin/supplier/edit");
        return mv;
    }

    @RequestMapping(path = "admin/supplier/add", method = RequestMethod.GET)
    public ModelAndView admin_supplier_add(SupplierVO product) {
        return this.createFormView(this.supplierCRUDService.initVO());
    }

    @RequestMapping(path = "admin/supplier/edit/{id}")
    public ModelAndView admin_supplier_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.supplierCRUDService.findOneVO(id));
    }

    @RequestMapping(value = "admin/supplier/save", method = RequestMethod.POST)
    public ModelAndView admin_supplier_save(SupplierVO supplier) {
        ServiceResponse response;
        try {
            response = this.supplierCRUDService.processSave(supplier, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, supplier);
    }

    private ModelAndView createFormView(ServiceResponse response, SupplierVO product) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            product = (SupplierVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(product);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(SupplierVO vo) {
        ModelAndView mv = this.createView("admin/supplier/form");
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/supplier/save");
        mv.addObject("crudHomePath", "admin/supplier");
        return mv;
    }
}
