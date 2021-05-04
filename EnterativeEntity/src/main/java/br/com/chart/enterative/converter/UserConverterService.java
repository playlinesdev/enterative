package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.UserRoleDAO;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.Partner;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.PartnerVO;
import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.entity.vo.UserRoleVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class UserConverterService extends ConverterService<User, UserVO> {

    @Autowired
    private UserRoleConverterService userRoleConverter;

    @Autowired
    private UserRoleDAO userRoleDAO;

    public UserConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public User convert(UserVO vo) {
        User entity = new User();
        entity.setAccount(this.reflectionUtils.asHollowLink(Account::new, vo.getAccount()));
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setEmail(vo.getEmail());
        entity.setId(vo.getId());
        entity.setLocale(vo.getLocale());
        entity.setLogin(vo.getLogin());
        entity.setName(vo.getName());
        entity.setPartner(this.reflectionUtils.asHollowLink(Partner::new, vo.getPartner()));
        entity.setPassword(vo.getPassword());
        entity.setRoles(vo.getRoles().stream().map(this.userRoleConverter::convert).collect(Collectors.toList()));
        entity.setShop(this.reflectionUtils.asHollowLink(Shop::new, vo.getShop()));
        entity.setStatus(vo.getStatus());
        entity.setTerminal(vo.getTerminal());
        entity.setToken(vo.getToken());
        entity.setLastSearchJSON(vo.getLastSearchJSON());
        return entity;
    }

    @Override
    public UserVO convert(User entity) {
        UserVO vo = new UserVO();
        vo.setAccount(this.reflectionUtils.asNamedLink(AccountVO::new, entity.getAccount()));
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setEmail(entity.getEmail());
        vo.setId(entity.getId());
        vo.setLocale(entity.getLocale());
        vo.setLogin(entity.getLogin());
        vo.setName(entity.getName());
        vo.setPartner(this.reflectionUtils.asNamedLink(PartnerVO::new, entity.getPartner()));
        vo.setPassword(entity.getPassword());
        vo.setRoles(entity.getRoles().stream().map(this.userRoleConverter::convert).collect(Collectors.toList()));

        List<UserRoleVO> list = this.searchRoles(vo);
        vo.setRoles(list);

        vo.setShop(this.reflectionUtils.asNamedLink(ShopVO::new, entity.getShop()));
        vo.setStatus(entity.getStatus());
        vo.setTerminal(entity.getTerminal());
        vo.setToken(entity.getToken());
        vo.setLastSearchJSON(entity.getLastSearchJSON());
        return vo;
    }

    private List<UserRoleVO> searchRoles() {
        return this.userRoleDAO.findAll()
                .map(this.userRoleConverter::convert)
                .sorted(Comparator.comparing(UserRoleVO::getName))
                .collect(Collectors.toList());
    }

    public List<UserRoleVO> searchRoles(UserVO user) {
        List<UserRoleVO> lista = this.searchRoles();
        lista.stream()
                .filter(r -> user.getRoles().stream().noneMatch(ur -> Objects.equals(ur.getId(), r.getId())))
                .forEach(r -> r.setSelected(false));
        return lista;
    }
}
