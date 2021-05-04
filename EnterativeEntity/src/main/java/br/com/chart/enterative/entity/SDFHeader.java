package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "sdf_header")
public class SDFHeader extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "sdf_file_id")
    @Getter @Setter private SDFFile file;

    @Temporal(TemporalType.DATE)
    @Column(name = "file_transmission_date")
    @Getter @Setter private Date fileTransmissionDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "file_reporting_date")
    @Getter @Setter private Date fileReportingDate;

    @Column(name = "partner_id", length = 10)
    @Size(max = 10, message = ENTITY_MESSAGE.SIZE_10)
    @Getter @Setter private String partnerId;

    @Column(name = "partner_name", length = 25)
    @Size(max = 25, message = ENTITY_MESSAGE.SIZE_25)
    @Getter @Setter private String partnerName;

    @Column(name = "filler", length = 372)
    @Size(max = 372, message = ENTITY_MESSAGE.SIZE_372)
    @Getter @Setter private String filler;
}
