package br.com.chart.enterative.test;

import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.AccountType;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.repository.AccountRepository;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author William Leite
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private EnterativeUtils utils;
    
    @Autowired
    private EnterativeReflectionUtils reflectionUtils;
    
    @Test
    public void whenFindByName_thenReturnAccount() {
        // given
        Account account = new Account();
        account.setCreatedAt(new Date());
        account.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, 0L));
        account.setName("Test Account");
        account.setStatus(STATUS.ACTIVE);
        account.setType(this.reflectionUtils.asHollowLink(AccountType::new, 1L));
        this.entityManager.persist(account);
        this.entityManager.flush();
        
        // when
        Account found = this.accountRepository.findByName(account.getName());
        
        // then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo(account.getName());
    }
}
