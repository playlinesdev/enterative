package br.com.chart.enterative.event;

import br.com.chart.enterative.enums.ACTIVATION_DECISION;
import br.com.chart.enterative.vo.bhn.BHNResponseContainer;
import br.com.chart.enterative.vo.bhn.BHNTransactionThreadVO;
import lombok.Getter;
import lombok.Setter;

public class BHNTransactionThreadEvent {

    @Getter @Setter private Long id;
    @Getter @Setter private BHNResponseContainer responseContainer;
    @Getter @Setter private ACTIVATION_DECISION decisao;
    @Getter @Setter private BHNTransactionThreadVO vo;

    public BHNTransactionThreadEvent(BHNResponseContainer responseContainer, ACTIVATION_DECISION decisao) {
        this.responseContainer = responseContainer;
        this.decisao = decisao;
    }

    public BHNTransactionThreadEvent(Long id, BHNResponseContainer responseContainer, ACTIVATION_DECISION decisao) {
        this.id = id;
        this.responseContainer = responseContainer;
        this.decisao = decisao;
    }

    public BHNTransactionThreadEvent(Long id, BHNResponseContainer responseContainer, ACTIVATION_DECISION decisao, BHNTransactionThreadVO vo) {
        this.id = id;
        this.responseContainer = responseContainer;
        this.decisao = decisao;
        this.vo = vo;
    }

}
