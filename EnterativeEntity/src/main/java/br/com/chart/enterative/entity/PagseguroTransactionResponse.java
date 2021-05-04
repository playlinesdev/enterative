package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "pagseguro_transaction_response")
public class PagseguroTransactionResponse extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private Long id;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdAt;
    
    @Column(name = "reference")
    @Getter @Setter private String reference;
    
    @Column(name = "payment_method_type")
    @Getter @Setter private String paymentMethodType;
    
    @Column(name = "payment_method_code")
    @Getter @Setter private String paymentMethodCode;
    
    @Column(name = "type")
    @Getter @Setter private String type;
    
    @Column(name = "status")
    @Getter @Setter private String status;
    
    @Column(name = "code")
    @Getter @Setter private String code;
    
    @Column(name = "xml")
    @Getter @Setter private String xml;
}