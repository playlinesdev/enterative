package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.vo.AccountTypeVO;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class AccountDAO extends UserAwareDAO<Account, Long> {

    public AccountDAO(BaseRepository<Account, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AccountRepository repository() {
        return (AccountRepository) super.repository();
    }

    public List<Account> findByParentId(Long id) {
        return this.repository().findByParentId(id);
    }

    public List<Account> findByStatusOrderByName(STATUS status) {
        return this.repository().findByStatusOrderByName(status);
    }

    public Account findByIdWithTransactionsEager(Long id) {
        return this.repository().findByIdWithTransactionsEager(id);
    }

    public Account findByIdWithTransactionsDeadFileEager(Long id) {
        return this.repository().findByIdWithTransactionsDeadFileEager(id);
    }

    public List<Account> findAllWithTransactionsDeadFileEager() {
        return this.repository().findAllWithTransactionsDeadFileEager();
    }

    public List<Account> findAllWithTransactionsEager() {
        return this.repository().findAllWithTransactionsEager();
    }

    public Page<Account> findByNameIgnoreCaseContainingAndStatusAndTypeIdOrderByName(String name, STATUS status, Long type, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndStatusAndTypeIdOrderByName(name, status, type, pageable);
    }

    public Page<Account> findByNameIgnoreCaseContainingAndTypeIdOrderByName(String name, Long type, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndTypeIdOrderByName(name, type, pageable);
    }

    public Page<Account> findByStatusAndTypeIdOrderByName(STATUS status, Long type, Pageable pageable) {
        return this.repository().findByStatusAndTypeIdOrderByName(status, type, pageable);
    }

    public Page<Account> findByStatusOrderByName(STATUS status, Pageable pageable) {
        return this.repository().findByStatusOrderByName(status, pageable);
    }

    public Page<Account> findByTypeIdOrderByName(long type, Pageable pageable) {
        return this.repository().findByTypeIdOrderByName(type, pageable);
    }

    public Page<Account> findByNameIgnoreCaseContainingAndStatusOrderByName(String name, STATUS status, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndStatusOrderByName(name, status, pageable);
    }

    public void setBalanceThresholdForID(BigDecimal balanceThreshold, Long id) {
        this.repository().setBalanceThresholdForID(balanceThreshold, id);
    }
}
