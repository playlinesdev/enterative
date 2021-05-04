package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.chart.enterative.enums.RESOURCE_TYPE;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "resource")
public class Resource extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "server_id")
    @Getter @Setter private Server server;

    @Enumerated
    @Column(name = "type")
    @Getter @Setter private RESOURCE_TYPE type;

    @Column(name = "url", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String url;
}
