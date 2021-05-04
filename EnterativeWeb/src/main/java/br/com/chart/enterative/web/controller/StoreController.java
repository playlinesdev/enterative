package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.ProductCategoryVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.enums.PRODUCT_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.service.crud.ProductCategoryCRUDService;
import br.com.chart.enterative.service.store.ShoppingCartService;
import br.com.chart.enterative.service.store.StoreService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.SortColumnVO;
import br.com.chart.enterative.vo.StoreStepVO;
import br.com.chart.enterative.vo.search.StoreSearchVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class StoreController extends BaseWebController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductCategoryCRUDService productCategoryCRUDService;
    
    @Autowired
    private ShoppingCartService shoppingCartService;

    @RequestMapping(path = "store/maintenance")
    public ModelAndView store_maintenance() {
        return this.createView("store/maintenance");
    }

    @RequestMapping(path = "store", method = RequestMethod.GET)
    public ModelAndView store_get() {
        ModelAndView mv = this.createView("store/index");
        Long userID = this.loggedUserId();
        mv.addObject("favorites", this.storeService.retrieveFavorites(userID));
        mv.addObject("highlights", this.storeService.retrieveHighlights(userID));
        mv.addObject("products", this.storeService.retrieveProducts(userID, PRODUCT_TYPE.VIRTUAL));
        return mv;
    }

    @RequestMapping(path = "store/search", method = RequestMethod.GET)
    public ModelAndView store_search() {
        return this.createSearchForm(new StoreSearchVO());
    }

    @RequestMapping(path = "store/search", method = RequestMethod.POST)
    public ModelAndView store_search_post(StoreSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createSearchForm(searchForm);

        PageWrapper<ProductVO> products = this.storeService.search(searchForm, this.assembleSortColumns(), pageable, "store/search", this.loggedUserId());
        mv.addObject("objectList", products.getContent());
        mv.addObject("page", products);

        return mv;
    }
    
    @RequestMapping(path = "store/step/{id}", method = RequestMethod.POST)
    public ModelAndView store_step_id_post(@PathVariable("id") Long id, StoreStepVO stepVO) throws JsonProcessingException {
        ModelAndView mv;
        if (!Objects.equals(stepVO.getCurrentStep(), stepVO.getTotalSteps())) {
            mv = this.createView("store/step/" + this.storeService.retrieveStepPath(id));

            this.storeService.incrementStep(stepVO);

            mv.addAllObjects(this.storeService.retrieveStepContext(id, stepVO));
            mv.addObject("product", this.storeService.retrieveProduct(id, this.loggedUserId()));
            mv.addObject("stepVO", stepVO);
        } else {
            this.shoppingCartService.addProduct(id, this.loggedUser(), stepVO);
            mv = this.createRedirectView("cart");
        }
        return this.validateProduct(id, mv);
    }

    @RequestMapping(path = "store/step/{id}", method = RequestMethod.GET)
    public ModelAndView store_step_id_get(@PathVariable("id") Long id) throws JsonProcessingException {
        ModelAndView mv = this.createView("store/step/" + this.storeService.retrieveStepPath(id));
        mv.addObject("product", this.storeService.retrieveProduct(id, this.loggedUserId()));
        StoreStepVO vo = new StoreStepVO(this.storeService.retrieveTotalSteps(id));
        mv.addObject("stepVO", vo);
        mv.addAllObjects(this.storeService.retrieveStepContext(id, vo));
        return this.validateProduct(id, mv);
    }

    @RequestMapping(path = "store/product/{id}", method = RequestMethod.GET)
    public ModelAndView store_product_id_get(@PathVariable("id") Long id) {
        ModelAndView mv = this.createView("store/productDetails");
        mv.addObject("product", this.storeService.retrieveProduct(id, this.loggedUserId()));
        return this.validateProduct(id, mv);
    }

    @RequestMapping(path = "store/product/favorite/{id}", method = RequestMethod.GET)
    public ModelAndView store_product_favorite_id_get(@PathVariable("id") Long id) {
        this.storeService.toggleFavorite(id, this.loggedUserId());
        ModelAndView mv = this.createRedirectView("store/product/" + id);
        return this.validateProduct(id, mv);
    }

    private ModelAndView validateProduct(Long id, ModelAndView mv) {
        ProductVO product = this.storeService.retrieveProduct(id, this.loggedUserId());
        if (product.getStatus() != STATUS.ACTIVE) {
            return this.store_get();
        }
        return mv;
    }

    private ModelAndView createSearchForm(StoreSearchVO searchForm) {
        ModelAndView mv = this.createView("store/search");
        mv.addObject("searchForm", searchForm);
        mv.addObject("category_list", this.productCategoryCRUDService.findAllVOSorted(Comparator.comparing(ProductCategoryVO::getName)).collect(Collectors.toList()));
        mv.addObject("sort_list", this.assembleSortColumns());
        return mv;
    }

    private List<SortColumnVO> assembleSortColumns() {
        List<SortColumnVO> list = new ArrayList<>();
        list.add(new SortColumnVO(1L, "displayName", "base.name.asc", Sort.Direction.ASC));
        list.add(new SortColumnVO(2L, "displayName", "base.name.desc", Sort.Direction.DESC));
        list.add(new SortColumnVO(3L, "amount", "base.amount.asc", Sort.Direction.ASC));
        list.add(new SortColumnVO(4L, "amount", "base.amount.desc", Sort.Direction.DESC));
        return list;
    }
}
