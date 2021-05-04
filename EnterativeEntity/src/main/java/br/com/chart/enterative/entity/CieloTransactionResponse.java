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
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "cielo_transaction_response")
public class CieloTransactionResponse extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private Long id;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdAt;
    
    @Column(name = "order_number")
    @Getter @Setter private String order_number;
    
    @Column(name = "amount")
    @Getter @Setter private Integer amount;
    
    @Column(name = "discount_amount")
    @Getter @Setter private Integer discount_amount;
    
    @Column(name = "checkout_cielo_order_number")
    @Getter @Setter private String checkout_cielo_order_number;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date created_date;
    
    @Column(name = "customer_name")
    @Getter @Setter private String customer_name;
    
    @Column(name = "customer_phone")
    @Getter @Setter private String customer_phone;
    
    @Column(name = "customer_identity")
    @Getter @Setter private String customer_identity;
    
    @Column(name = "customer_email")
    @Getter @Setter private String customer_email;
    
    @Column(name = "shipping_type")
    @Getter @Setter private Integer shipping_type;
    
    @Column(name = "shipping_name")
    @Getter @Setter private String shipping_name;
    
    @Column(name = "shipping_price")
    @Getter @Setter private Integer shipping_price;
    
    @Column(name = "shipping_address_zipcode")
    @Getter @Setter private String shipping_address_zipcode;
    
    @Column(name = "shipping_address_district")
    @Getter @Setter private String shipping_address_district;
    
    @Column(name = "shipping_address_city")
    @Getter @Setter private String shipping_address_city;
    
    @Column(name = "shipping_address_state")
    @Getter @Setter private String shipping_address_state;
    
    @Column(name = "shipping_address_line1")
    @Getter @Setter private String shipping_address_line1;
    
    @Column(name = "shipping_address_line2")
    @Getter @Setter private String shipping_address_line2;
    
    @Column(name = "shipping_address_number")
    @Getter @Setter private String shipping_address_number;
    
    @Column(name = "payment_method_type")
    @Getter @Setter private Integer payment_method_type;
    
    @Column(name = "payment_method_brand")
    @Getter @Setter private Integer payment_method_brand;
    
    @Column(name = "payment_maskedcreditcard")
    @Getter @Setter private String payment_maskedcreditcard;
    
    @Column(name = "payment_installments")
    @Getter @Setter private Integer payment_installments;
    
    @Column(name = "payment_status")
    @Getter @Setter private Integer payment_status;
    
    @Column(name = "tid")
    @Getter @Setter private String tid;
    
    @Column(name = "test_transaction")
    @Getter @Setter private String test_transaction;
    
    // Workaround
    @Transient
    @Getter @Setter private String $id;
}