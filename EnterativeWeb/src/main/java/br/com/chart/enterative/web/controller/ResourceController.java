package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.ResourceVO;
import br.com.chart.enterative.entity.vo.ServerVO;
import br.com.chart.enterative.enums.RESOURCE_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.ResourceCRUDService;
import br.com.chart.enterative.service.crud.ServerCRUDService;
import br.com.chart.enterative.vo.LastSearchVO;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ResourceSearchVO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResourceController extends BaseWebController {

    @Autowired
    private ResourceCRUDService resourceService;

    @Autowired
    private ServerCRUDService serverService;

    @RequestMapping(path = "admin/resource", method = RequestMethod.GET)
    public ModelAndView admin_resource_get() {
        LastSearchVO<ResourceSearchVO> lastSearch = this.retrieveLastSearch("admin/resource", ResourceSearchVO.class);
        if (Objects.nonNull(lastSearch)) {
            return this.admin_resource_post(lastSearch.getSearchForm(), PageRequest.of(lastSearch.getPageable().getPageNumber(), lastSearch.getPageable().getPageSize()));
        }
        
        ModelAndView mv = this.createView("admin/resource/list");
        mv.addObject("searchForm", new ResourceSearchVO());
        mv.addObject("objectList", new ArrayList<ResourceVO>());
        mv.addObject("addPath", "admin/resource/add");
        mv.addObject("server_list", this.serverService.findAllVOSorted(Comparator.comparing(ServerVO::getName)).collect(Collectors.toList()));
        return mv;
    }

    @RequestMapping(path = "admin/resource", method = RequestMethod.POST)
    public ModelAndView admin_resource_post(ResourceSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/resource/list");
        PageWrapper<ResourceVO> products = this.resourceService.retrieve(searchForm, pageable, "admin/resource");
        
        this.updateLastSearch("admin/resource", searchForm, pageable);
        
        mv.addObject("objectList", products.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", products);
        mv.addObject("addPath", "admin/resource/add");
        mv.addObject("editPath", "admin/resource/edit");
        mv.addObject("server_list", this.serverService.findAllVOSorted(Comparator.comparing(ServerVO::getName)).collect(Collectors.toList()));
        return mv;
    }

    @RequestMapping(path = "admin/resource/add", method = RequestMethod.GET)
    public ModelAndView admin_resource_add(ResourceVO resource) {
        return this.createFormView(this.resourceService.initVO());
    }

    @RequestMapping(path = "admin/resource/edit/{id}")
    public ModelAndView admin_resource_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.resourceService.findOneVO(id));
    }

    @RequestMapping(value = "admin/resource/save", method = RequestMethod.POST)
    public ModelAndView admin_resource_save(ResourceVO resource) {
        ServiceResponse response;
        try {
            response = this.resourceService.processSave(resource, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, resource);
    }

    private ModelAndView createFormView(ServiceResponse response, ResourceVO resource) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            resource = (ResourceVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(resource);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(ResourceVO vo) {
        ModelAndView mv = this.createView("admin/resource/form");
        mv.addObject("status_list", STATUS.values());
        mv.addObject("server_list", this.serverService.findAllVOSorted(Comparator.comparing(ServerVO::getName)).collect(Collectors.toList()));
        mv.addObject("type_list", RESOURCE_TYPE.ordered());
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/resource/save");
        mv.addObject("crudHomePath", "admin/resource");
        return mv;
    }
}
