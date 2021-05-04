package br.com.chart.enterative.web.controller.report;

import br.com.chart.enterative.vo.search.SDFReportSearchVO;
import br.com.chart.enterative.vo.search.SDFValidationSearchVO;
import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.BHNTransaction;
import br.com.chart.enterative.entity.vo.BHNTransactionVO;
import br.com.chart.enterative.enums.SDF_FILE_STATUS;
import br.com.chart.enterative.service.SDFValidationService;
import br.com.chart.enterative.service.crud.BHNTransactionCRUDService;
import br.com.chart.enterative.service.pdf.PDFReportService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SDFValidationController extends BaseWebController {

    @Autowired
    private BHNTransactionCRUDService bhnTransactionCRUDService;

    @Autowired
    private SDFValidationService sdfService;

    @Autowired
    private PDFReportService pdfReportService;

    private static final String DIFFERENCE_REPORT_TEMPLATE = "reports/SDFValidationDifference";

    @RequestMapping(path = "admin/sdfvalidation", method = RequestMethod.GET)
    public ModelAndView admin_sdfvalidation_get() {
        ModelAndView mv = this.createView("admin/sdfvalidation/search");
        mv.addObject("searchForm", new SDFValidationSearchVO());
        mv.addObject("objectList", new ArrayList<SDFValidationSearchVO>());
        mv.addObject("status_list", SDF_FILE_STATUS.ordered());
        return mv;
    }

    @RequestMapping(value = "admin/sdfvalidation", method = RequestMethod.POST)
    public ModelAndView doSearch(SDFValidationSearchVO vo) {
        ModelAndView mv = this.createView("admin/sdfvalidation/search");
        mv.addObject("searchForm", vo);
        mv.addObject("objectList", sdfService.searchSDF(vo));
        mv.addObject("status_list", SDF_FILE_STATUS.ordered());
        return mv;
    }

    @RequestMapping("admin/sdfvalidation/assemble/{id}")
    public ModelAndView assembleFile(@PathVariable("id") Long id, Pageable pageable) {
        ModelAndView mv = this.createView("admin/sdfvalidation/assemble");
        mv.addAllObjects(sdfService.assembleFile(id, pageable));
        return mv;
    }

    @RequestMapping("admin/sdfvalidation/confirm/{id}")
    public ModelAndView confirmFile(@PathVariable("id") Long id) {
        ModelAndView mv = this.createView("admin/sdfvalidation/confirm");
        mv.addObject("sdfFile", sdfService.retrieveFile(id));
        mv.addObject("sdfSummary", sdfService.summarizeFile(id));
        return mv;
    }

    @RequestMapping("admin/sdfvalidation/save/{id}")
    public ModelAndView save(@PathVariable("id") Long id) {
        sdfService.save(id);

        ModelAndView mv = this.createView("admin/sdfvalidation/done");
        mv.addObject("sdfFile", sdfService.retrieveFile(id));
        mv.addObject("sdfSummary", sdfService.summarizeFile(id));
        return mv;
    }

    @RequestMapping(path = "admin/sdfvalidation/report", method = RequestMethod.GET)
    public ModelAndView report_search_view() {
        ModelAndView mv = this.createView("admin/sdfvalidation/reportsearch");
        mv.addObject("searchForm", new SDFReportSearchVO());
        return mv;
    }

    @RequestMapping(path = "admin/sdfvalidation/report", method = RequestMethod.POST)
    public ResponseEntity<byte[]> retrieveReport(SDFReportSearchVO vo) {
        Map<String, Object> contextVariables = sdfService.assembleReportVariables(vo);
        byte[] report = null;

        try {
            report = pdfReportService.generateReportAsByte(SDFValidationController.DIFFERENCE_REPORT_TEMPLATE, contextVariables, this.locale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String fileName = "sdfvalidation_report.pdf";
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        ResponseEntity<byte[]> response = new ResponseEntity<>(report, headers, HttpStatus.OK);
        return response;
    }


    @RequestMapping("admin/sdfvalidation/report/{id}")
    public ResponseEntity<byte[]> retrieveReport(@PathVariable("id") Long id, HttpServletRequest request) {
        Map<String, Object> contextVariables = sdfService.assembleReportVariables(id);
        byte[] report = null;

        try {
            report = pdfReportService.generateReportAsByte(SDFValidationController.DIFFERENCE_REPORT_TEMPLATE, contextVariables, this.locale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String fileName = "sdfvalidation_report.pdf";
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        ResponseEntity<byte[]> response = new ResponseEntity<>(report, headers, HttpStatus.OK);
        return response;
    }

    @ResponseBody
    @RequestMapping("admin/sdfvalidation/clearTransaction/{id}")
    public String clearTransaction(@PathVariable("id") Long detailId) {
        return sdfService.clearTransaction(detailId);
    }

    @ResponseBody
    @RequestMapping("admin/sdfvalidation/saveTransaction/{sdfid}/{tid}")
    public String saveTransaction(@PathVariable("sdfid") Long sdfid, @PathVariable("tid") Long tid) {
        BHNTransaction transacao = this.bhnTransactionCRUDService.findOne(tid);
        return sdfService.saveTransaction(sdfid, transacao);
    }

    @ResponseBody
    @RequestMapping(value = "admin/sdfvalidation/transactions/day/{id}", method = RequestMethod.GET)
    public List<BHNTransactionVO> transactionsByToday(@PathVariable("id") Long id) {
        return bhnTransactionCRUDService.retrieveByDate(id);
    }

    @ResponseBody
    @RequestMapping(value = "admin/sdfvalidation/transactions/gift/{id}", method = RequestMethod.GET)
    public List<BHNTransactionVO> transactionsByGift(@PathVariable("id") Long id) {
        return bhnTransactionCRUDService.retrieveByGift(id);
    }

    @ResponseBody
    @RequestMapping(value = "admin/sdfvalidation/transactions/product/{id}", method = RequestMethod.GET)
    public List<BHNTransactionVO> transactionsByProduct(@PathVariable("id") Long id) {
        return bhnTransactionCRUDService.retrieveByProduct(id);
    }

    @ResponseBody
    @RequestMapping(value = "admin/sdfvalidation/transactions/detail/{sdfid}/{tid}", method = RequestMethod.GET)
    public BHNTransactionVO transactionDetailsById(@PathVariable("sdfid") Long sdfID, @PathVariable("tid") Long tID) {
        return bhnTransactionCRUDService.retrieveDetails(sdfID, tID);
    }
}
