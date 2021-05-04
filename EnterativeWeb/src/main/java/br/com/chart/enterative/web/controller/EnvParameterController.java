package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.EnvParameterVO;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.EnvParameterCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EnvParameterController extends BaseWebController {

    @Autowired
    private EnvParameterCRUDService parameterService;

    @RequestMapping(path = "admin/parameter", method = RequestMethod.GET)
    public ModelAndView admin_parameter_get() {
        ModelAndView mv = this.createView("admin/parameter/list");

        mv.addObject("objectList", this.parameterService.retrieveUIList());
        mv.addObject("addPath", "admin/parameter/add");
        mv.addObject("editPath", "admin/parameter/edit");
        return mv;
    }

    @RequestMapping(path = "admin/parameter/edit/{id}")
    public ModelAndView admin_parameter_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.parameterService.findOneVO(id));
    }

    @RequestMapping(path = "admin/parameter/add/{parameter}")
    public ModelAndView parametroAddDefault(@PathVariable("parameter") ENVIRONMENT_PARAMETER parameter) {
        EnvParameterVO vo = new EnvParameterVO();
        vo.setParam(parameter);
        vo.setName(parameter.getName());
        vo.setValue(parameter.getDefaultValue().toString());
        return this.createFormView(vo);
    }

    @RequestMapping(value = "admin/parameter/save", method = RequestMethod.POST)
    public ModelAndView admin_product_save(EnvParameterVO parameter) {
        ServiceResponse response;
        try {
            response = this.parameterService.processSave(parameter, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, parameter);
    }

    private ModelAndView createFormView(ServiceResponse response, EnvParameterVO parameter) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            parameter = (EnvParameterVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(parameter);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(EnvParameterVO vo) {
        ModelAndView mv = this.createView("admin/parameter/form");
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/parameter/save");
        mv.addObject("crudHomePath", "admin/parameter");
        return mv;
    }
}
