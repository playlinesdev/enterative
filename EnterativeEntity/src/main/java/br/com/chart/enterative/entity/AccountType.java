package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "account_type")
public class AccountType extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "commissionable", columnDefinition = "tinyint")
    @Getter @Setter private Boolean commissionable;

    @Column(name = "initial_deposit")
    @NumberFormat(pattern = "###,###,###.00")
    @Getter @Setter private BigDecimal initialDeposit;
}
