package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.AccountType;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.AccountTypeVO;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class AccountConverterService extends ConverterService<Account, AccountVO> {

    public AccountConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public Account convert(AccountVO vo) {
        Account entity = new Account();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        entity.setStatus(vo.getStatus());
        entity.setType(this.reflectionUtils.asHollowLink(AccountType::new, vo.getType()));
        entity.setParent(this.reflectionUtils.asHollowLink(Account::new, vo.getParent()));
        entity.setBalanceThreshold(vo.getBalanceThreshold());
        return entity;
    }

    @Override
    public AccountVO convert(Account entity) {
        AccountVO vo = new AccountVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setStatus(entity.getStatus());
        vo.setType(this.reflectionUtils.asNamedLink(AccountTypeVO::new, entity.getType()));
        vo.setParent(this.reflectionUtils.asNamedLink(AccountVO::new, entity.getParent()));
        vo.setBalanceThreshold(entity.getBalanceThreshold());
        return vo;
    }
}
