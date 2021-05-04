package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseDAO;
import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.entity.AccountTransactionDeadFile;
import br.com.chart.enterative.repository.AccountTransactionDeadFileRepository;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * @author William Leite
 */
@Service
public class AccountTransactionDeadFileDAO extends BaseDAO<AccountTransactionDeadFile, Long> {

    public AccountTransactionDeadFileDAO(BaseRepository<AccountTransactionDeadFile, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AccountTransactionDeadFileRepository repository() {
        return super.repository();
    }

    public List<AccountTransactionDeadFile> findByAccount(Long id) {
        return this.repository().findByAccount(id);
    }

    public AccountTransactionDeadFile findByPurchaseOrderLineId(Long id) {
        return this.repository().findByPurchaseOrderLineId(id);
    }

    public List<AccountTransactionDeadFile> findBySaleOrderLineId(Long id) {
        return this.repository().findBySaleOrderLineId(id);
    }

    public List<AccountTransactionDeadFile> findByAccountInAndTransactionDate(List<Long> ids, Date startDate, Date endDate) {
        return this.repository().findByAccountInAndTransactionDate(ids, startDate, endDate);
    }

    public List<AccountTransactionDeadFile> findByAccountInAndTransactionDateGreaterThan(List<Long> ids, Date startDate) {
        return this.repository().findByAccountInAndTransactionDateGreaterThan(ids, startDate);
    }

    public List<AccountTransactionDeadFile> findByAccountInAndTransactionDateLessThan(List<Long> ids, Date endDate) {
        return this.repository().findByAccountInAndTransactionDateLessThan(ids, endDate);
    }

    public List<AccountTransactionDeadFile> findByAccountIn(List<Long> ids) {
        return this.repository().findByAccountIn(ids);
    }
}
