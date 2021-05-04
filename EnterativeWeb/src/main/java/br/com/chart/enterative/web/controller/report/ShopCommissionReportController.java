package br.com.chart.enterative.web.controller.report;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.service.crud.ProductCRUDService;
import br.com.chart.enterative.service.crud.ShopCRUDService;
import br.com.chart.enterative.service.report.ShopCommissionReportService;
import br.com.chart.enterative.vo.search.ShopCommissionReportSearchVO;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author William Leite
 */
@Controller
public class ShopCommissionReportController extends BaseWebController {

    @Autowired
    private ShopCommissionReportService shopCommissionReportService;

    @Autowired
    private ProductCRUDService productService;

    @Autowired
    private ShopCRUDService shopService;

    @RequestMapping(path = "admin/shopcommissionreport", method = RequestMethod.GET)
    public ModelAndView admin_shopcommissionreport() {
        ModelAndView mv = this.createView();
        mv.addObject("report", this.shopCommissionReportService.assemble(null));
        mv.addObject("searchForm", new ShopCommissionReportSearchVO());
        return mv;
    }

    @RequestMapping(path = "admin/shopcommissionreport", method = RequestMethod.POST)
    public ModelAndView admin_shopcommissionreport_post(ShopCommissionReportSearchVO searchForm) {
        ModelAndView mv = this.createView();
        mv.addObject("report", this.shopCommissionReportService.assemble(searchForm));
        mv.addObject("searchForm", searchForm);
        return mv;
    }

    private ModelAndView createView() {
        ModelAndView mv = this.createView("admin/report/shopcommissionreport");
        mv.addObject("product_list", this.productService.findAllVOSorted(Comparator.comparing(ProductVO::getName)).collect(Collectors.toList()));
        mv.addObject("shop_list", this.shopService.findAllVOSorted(Comparator.comparing(ShopVO::getName)).collect(Collectors.toList()));
        return mv;
    }
}
