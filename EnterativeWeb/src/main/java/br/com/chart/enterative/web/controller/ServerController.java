package br.com.chart.enterative.web.controller;

import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.ServerVO;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.ProductCRUDService;
import br.com.chart.enterative.service.crud.ServerCRUDService;
import br.com.chart.enterative.vo.LastSearchVO;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ServerSearchVO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

@Controller
public class ServerController extends BaseWebController {

    @Autowired
    private ServerCRUDService serverService;

    @Autowired
    private ProductCRUDService productService;

    @RequestMapping(path = "admin/server", method = RequestMethod.GET)
    public ModelAndView admin_server_get() {
        LastSearchVO<ServerSearchVO> lastSearch = this.retrieveLastSearch("admin/server", ServerSearchVO.class);
        if (Objects.nonNull(lastSearch)) {
            return this.admin_server_post(lastSearch.getSearchForm(), PageRequest.of(lastSearch.getPageable().getPageNumber(), lastSearch.getPageable().getPageSize()));
        }
        
        ModelAndView mv = this.createView("admin/server/list");
        mv.addObject("searchForm", new ServerSearchVO());
        mv.addObject("objectList", new ArrayList<ServerVO>());
        mv.addObject("addPath", "admin/server/add");
        return mv;
    }

    @RequestMapping(path = "admin/server", method = RequestMethod.POST)
    public ModelAndView admin_server_post(ServerSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/server/list");
        PageWrapper<ServerVO> servers = this.serverService.retrieve(searchForm, pageable, "admin/server");
        
        this.updateLastSearch("admin/server", searchForm, pageable);
        
        mv.addObject("objectList", servers.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", servers);
        mv.addObject("addPath", "admin/server/add");
        mv.addObject("editPath", "admin/server/edit");
        return mv;
    }

    @RequestMapping(path = "admin/server/add", method = RequestMethod.GET)
    public ModelAndView admin_server_add(ServerVO server) {
        return this.createFormView(this.serverService.initVO());
    }

    @RequestMapping(path = "admin/server/edit/{id}")
    public ModelAndView admin_server_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.serverService.findOneVO(id));
    }

    @RequestMapping(value = "admin/server/save", method = RequestMethod.POST)
    public ModelAndView admin_server_save(ServerVO server) {
        ServiceResponse response;
        try {
            response = this.serverService.processSave(server, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, server);
    }

    private ModelAndView createFormView(ServiceResponse response, ServerVO server) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            server = (ServerVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(server);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(ServerVO vo) {
        ModelAndView mv = this.createView("admin/server/form");
        mv.addObject("status_list", STATUS.values());
        mv.addObject("product_list", this.productService.findAllVOSorted(Comparator.comparing(ProductVO::getName)).collect(Collectors.toList()));
        mv.addObject("process_list", ACTIVATION_PROCESS.ordered());
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/server/save");
        mv.addObject("crudHomePath", "admin/server");
        return mv;
    }
}
