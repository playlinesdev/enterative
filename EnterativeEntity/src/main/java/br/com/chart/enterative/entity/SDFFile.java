package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.SDF_FILE_STATUS;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "sdf_file")
public class SDFFile extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Enumerated
    @Column(name = "status")
    @Getter @Setter private SDF_FILE_STATUS status;

    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL)
    @Getter @Setter private SDFHeader header;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    @Getter @Setter private List<SDFDetail> details;

    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL)
    @Getter @Setter private SDFTrailer trailer;
}
