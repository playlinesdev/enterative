package br.com.chart.enterative.controller;

import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.vo.LastSearchPageable;
import br.com.chart.enterative.vo.LastSearchVO;
import br.com.chart.enterative.vo.base.NamedVO;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.web.servlet.ModelAndView;

public class BaseWebController extends UserAwareComponent {

    @Autowired
    private EnterativeUtils utils;

    protected static final String STATE_EDIT = "edit";
    protected static final String STATE_ADD = "add";

    @SuppressWarnings("unchecked")
    protected <S extends NamedVO> LastSearchVO<S> retrieveLastSearch(String listName, Class<S> clazz) {
        try {
            User user = this.loggedUser();
            if (Objects.nonNull(user.getLastSearchJSON()) && !user.getLastSearchJSON().isEmpty()) {
                LastSearchVO<S> lastSearch = this.utils.fromJSON(user.getLastSearchJSON(), LastSearchVO.class, clazz);
                if (Objects.equals(lastSearch.getListName(), listName)) {
                    return lastSearch;
                }
            }
        } catch (Exception e) {
            this.log("Não foi possível recuperar a última busca!");
        }
        return null;
    }

    protected <S extends NamedVO> void updateLastSearch(String listName, S searchForm, Pageable pageable) {
        try {
            LastSearchVO<S> lastSearch = new LastSearchVO<>();
            lastSearch.setListName(listName);
            lastSearch.setPageable(LastSearchPageable.of(pageable.getPageNumber(), pageable.getPageSize()));
            lastSearch.setSearchForm(searchForm);

            String json = this.utils.toJSON(lastSearch);

            User user = this.loggedUser();
            user.setLastSearchJSON(json);
            this.userDAO.saveAndFlush(user, this.systemUserId());
        } catch (Exception e) {
            this.log("Não foi possível salvar a última busca!");
        }
    }

    protected String assemblePath(String home, String target) {
        return String.format("%s%s", home, target);
    }

    protected String assemblePath(String home, String target, boolean startingSlash) {
        if (Objects.nonNull(target)) {
            return this.assemblePath(startingSlash ? "/" : "", this.assemblePath(home, target));
        } else {
            return null;
        }
    }

    protected ModelAndView assembleListModelAndView(String homePath, String path, List<?> objects, String addPath, String editPath, String removePath) {
        ModelAndView mv = this.createView(this.assemblePath(homePath, path));
        mv.addObject("objectList", objects);
        mv.addObject("addPath", this.assemblePath(homePath, addPath, true));
        mv.addObject("editPath", this.assemblePath(homePath, editPath, true));
        mv.addObject("removePath", this.assemblePath(homePath, removePath, true));
        return mv;
    }

    protected ModelAndView assembleFormModelAndView(String homePath, String path, Object activeObject, String saveActionPath, String pageState) {
        ModelAndView mv = this.createView(this.assemblePath(homePath, path));
        mv.addObject("activeObject", activeObject);
        mv.addObject("saveActionPath", this.assemblePath(homePath, saveActionPath, true));
        mv.addObject("crudHomePath", this.assemblePath("/", homePath));
        mv.addObject("pageState", pageState);
        return mv;
    }

    public ModelAndView createRedirectView(String viewName) {
        return this.createView(String.format("redirect:/%s", viewName));
    }

    public ModelAndView createView(String viewName) {
        ModelAndView mv = new ModelAndView(viewName);
        mv.addObject("maintenance", EnterativeUtils.MAINTENANCE);
        return mv;
    }
}
