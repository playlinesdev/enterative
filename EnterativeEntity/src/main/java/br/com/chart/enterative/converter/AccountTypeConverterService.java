package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.AccountType;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.AccountTypeVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class AccountTypeConverterService extends ConverterService<AccountType, AccountTypeVO> {

    public AccountTypeConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public AccountType convert(AccountTypeVO vo) {
        AccountType entity = new AccountType();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCommissionable(vo.getCommissionable());
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setInitialDeposit(vo.getInitialDeposit());
        entity.setName(vo.getName());
        return entity;
    }

    @Override
    public AccountTypeVO convert(AccountType entity) {
        AccountTypeVO vo = new AccountTypeVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCommissionable(entity.getCommissionable());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setInitialDeposit(entity.getInitialDeposit());
        vo.setName(entity.getName());
        return vo;
    }

}
