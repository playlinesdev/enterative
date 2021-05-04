package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.AccountTypeVO;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.FileUploadVO;
import br.com.chart.enterative.enums.FILE_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.crud.AccountTypeCRUDService;
import br.com.chart.enterative.service.crud.FileUploadCRUDService;
import br.com.chart.enterative.vo.LastSearchVO;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountSearchVO;
import br.com.chart.enterative.vo.search.FileUploadSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author William Leite
 */
@Controller
public class FileUploadController extends BaseWebController {

    @Autowired
    private FileUploadCRUDService fileUploadCRUDService;

    @RequestMapping(path = "admin/fileupload", method = RequestMethod.GET)
    public ModelAndView admin_fileupload_get() {
        LastSearchVO<FileUploadSearchVO> lastSearch = this.retrieveLastSearch("admin/fileupload", FileUploadSearchVO.class);
        if (Objects.nonNull(lastSearch)) {
            return this.admin_fileupload_post(lastSearch.getSearchForm(), PageRequest.of(lastSearch.getPageable().getPageNumber(), lastSearch.getPageable().getPageSize()));
        }
        
        ModelAndView mv = this.createView("admin/fileupload/list");
        mv.addObject("searchForm", new FileUploadSearchVO());
        mv.addObject("objectList", new ArrayList<FileUploadVO>());
        mv.addObject("type_list", FILE_TYPE.ordered());
        mv.addObject("addPath", "admin/fileupload/add");
        return mv;
    }

    @RequestMapping(path = "admin/fileupload", method = RequestMethod.POST)
    public ModelAndView admin_fileupload_post(FileUploadSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/fileupload/list");
        PageWrapper<FileUploadVO> files = this.fileUploadCRUDService.retrieveFiles(searchForm, pageable, "admin/fileupload");
        
        this.updateLastSearch("admin/fileupload", searchForm, pageable);
        
        mv.addObject("objectList", files.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", files);
        mv.addObject("addPath", "admin/fileupload/add");
        mv.addObject("editPath", "admin/fileupload/edit");
        mv.addObject("type_list", FILE_TYPE.ordered());
        return mv;
    }

    @RequestMapping(path = "admin/fileupload/add", method = RequestMethod.GET)
    public ModelAndView admin_fileupload_add(FileUploadVO fileUploadVO) {
        return this.createFormView(this.fileUploadCRUDService.initVO());
    }

    @RequestMapping(path = "admin/fileupload/remove/{id}")
    public ModelAndView admin_fileupload_remove_id(@PathVariable("id") Long id) {
        this.fileUploadCRUDService.delete(id);
        return this.admin_fileupload_get();
    }

    @RequestMapping(path = "admin/fileupload/edit/{id}")
    public ModelAndView admin_fileupload_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.fileUploadCRUDService.findOneVO(id));
    }

    @RequestMapping(value = "admin/fileupload/save", method = RequestMethod.POST)
    public ModelAndView admin_fileupload_save(FileUploadVO file) {
        ServiceResponse response;
        try {
            response = this.fileUploadCRUDService.processSave(file, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, file);
    }

    @ResponseBody
    @RequestMapping(value = "admin/fileupload/objects/{type}", method = RequestMethod.GET)
    public List<?> retrieveObjects(@PathVariable String type) {
        return this.fileUploadCRUDService.retrieveObjects(type);
    }

    private ModelAndView createFormView(ServiceResponse response, FileUploadVO file) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            file = response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(file);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(FileUploadVO vo) {
        ModelAndView mv = this.createView("admin/fileupload/form");
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/fileupload/save");
        mv.addObject("crudHomePath", "admin/fileupload");
        mv.addObject("type_list", FILE_TYPE.ordered());
        return mv;
    }
}