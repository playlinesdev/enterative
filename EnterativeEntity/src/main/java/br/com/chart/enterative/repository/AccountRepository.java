package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.enums.STATUS;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author William Leite
 */
public interface AccountRepository extends UserAwareRepository<Account, Long> {

    public List<Account> findByParentId(Long id);

    @Query("SELECT s FROM Account s LEFT JOIN FETCH s.transactions WHERE s.id = :id")
    public Account findByIdWithTransactionsEager(@Param("id") Long id);

    @Query("SELECT s FROM Account s LEFT JOIN FETCH s.transactionsDeadFile WHERE s.id = :id")
    public Account findByIdWithTransactionsDeadFileEager(@Param("id") Long id);

    @Query("SELECT DISTINCT s FROM Account s LEFT JOIN FETCH s.transactionsDeadFile")
    public List<Account> findAllWithTransactionsDeadFileEager();

    @Query("SELECT DISTINCT s FROM Account s LEFT JOIN FETCH s.transactions")
    public List<Account> findAllWithTransactionsEager();

    public List<Account> findByStatusOrderByName(STATUS status);

    public Page<Account> findByNameIgnoreCaseContainingAndStatusAndTypeIdOrderByName(String name, STATUS status, Long type, Pageable pageable);

    public Page<Account> findByNameIgnoreCaseContainingAndTypeIdOrderByName(String name, Long type, Pageable pageable);

    public Page<Account> findByStatusAndTypeIdOrderByName(STATUS status, Long type, Pageable pageable);

    public Page<Account> findByStatusOrderByName(STATUS status, Pageable pageable);

    public Page<Account> findByTypeIdOrderByName(Long type, Pageable pageable);

    public Page<Account> findByNameIgnoreCaseContainingAndStatusOrderByName(String name, STATUS status, Pageable pageable);

    @Modifying
    @Query("UPDATE Account SET balanceThreshold = :balanceThreshold WHERE id = :id")
    void setBalanceThresholdForID(@Param("balanceThreshold") BigDecimal balanceThreshold, @Param("id") Long id);
}
