package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.STATUS;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "account")
@Getter
@Setter
public class Account extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Enumerated
    @Column(name = "status")
    private STATUS status;

    @ManyToOne
    @JoinColumn(name = "account_type_id")
    private AccountType type;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Account parent;
    
    @Column(name = "balance_threshold")
    private BigDecimal balanceThreshold;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<AccountTransaction> transactions;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<AccountTransactionDeadFile> transactionsDeadFile;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<User> users;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Shop> shops;
}
