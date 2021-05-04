package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.AccountTransaction;
import br.com.chart.enterative.entity.AccountTransactionCategory;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_STATUS;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_TYPE;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.repository.AccountTransactionRepository;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class AccountTransactionDAO extends UserAwareDAO<AccountTransaction, Long> {

    @Autowired
    private EnterativeUtils utils;

    public AccountTransactionDAO(BaseRepository<AccountTransaction, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AccountTransactionRepository repository() {
        return (AccountTransactionRepository) super.repository();
    }

    public AccountTransaction findBySaleOrderLineAndType(SaleOrderLine line, ACCOUNT_TRANSACTION_TYPE type) {
        return this.repository().findBySaleOrderLineAndType(line, type);
    }

    public boolean exists(final Long id) {
        return this.repository().existsById(id);
    }

    public void setStatusById(ACCOUNT_TRANSACTION_STATUS status, Long id, Date alteredAt, User alteredBy) {
        this.repository().setStatusById(status, id, alteredAt, alteredBy);
    }

    public AccountTransaction findByPurchaseOrderLineId(Long id) {
        if (Objects.nonNull(id) && id > 0) {
            return this.repository().findByPurchaseOrderLineId(id);
        }
        return null;
    }

    public List<AccountTransaction> findBySaleOrderLineId(Long id) {
        return this.repository().findBySaleOrderLineId(id);
    }

    public List<AccountTransaction> findByTransactionDateLessThanOrderByTransactionDateAsc(Date endDate) {
        endDate = this.utils.lastMoment(endDate);
        return this.repository().findByTransactionDateLessThan(endDate);
    }

    public void deleteByID(List<Long> ids) {
        this.repository().deleteByID(ids);
    }

    public void deleteByLastPosition() {
        this.repository().deleteByLastPosition();
    }

    public List<AccountTransaction> findByAccount(Long id) {
        return this.repository().findByAccount(id);
    }

    public List<AccountTransaction> findByAccountInAndTransactionDate(List<Long> ids, Date startDate, Date endDate) {
        return this.repository().findByAccountInAndTransactionDate(ids, startDate, endDate);
    }

    public List<AccountTransaction> findByAccountInAndTransactionDateGreaterThan(List<Long> ids, Date startDate) {
        return this.repository().findByAccountInAndTransactionDateGreaterThan(ids, startDate);
    }

    public List<AccountTransaction> findByAccountInAndTransactionDateLessThan(List<Long> ids, Date endDate) {
        return this.repository().findByAccountInAndTransactionDateLessThan(ids, endDate);
    }

    public List<AccountTransaction> findByAccountIn(List<Long> ids) {
        return this.repository().findByAccountIn(ids);
    }
}
