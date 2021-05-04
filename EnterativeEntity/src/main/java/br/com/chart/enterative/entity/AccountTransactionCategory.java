package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "account_transaction_category")
public class AccountTransactionCategory extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

}
