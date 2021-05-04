package br.com.chart.enterative.event;

import br.com.chart.enterative.enums.ACTIVATION_DECISION;
import br.com.chart.enterative.vo.epay.EpayDoTransactionUPResult;
import br.com.chart.enterative.vo.epay.EpayTransactionThreadVO;
import lombok.Getter;
import lombok.Setter;

public class EpayTransactionThreadEvent {

    @Getter @Setter private Long id;
    @Getter @Setter private EpayDoTransactionUPResult responseContainer;
    @Getter @Setter private ACTIVATION_DECISION decisao;
    @Getter @Setter private EpayTransactionThreadVO vo;

    public EpayTransactionThreadEvent(EpayDoTransactionUPResult responseContainer, ACTIVATION_DECISION decisao) {
        this.responseContainer = responseContainer;
        this.decisao = decisao;
    }

    public EpayTransactionThreadEvent(Long id, EpayDoTransactionUPResult responseContainer, ACTIVATION_DECISION decisao) {
        this.id = id;
        this.responseContainer = responseContainer;
        this.decisao = decisao;
    }

    public EpayTransactionThreadEvent(Long id, EpayDoTransactionUPResult responseContainer, ACTIVATION_DECISION decisao, EpayTransactionThreadVO vo) {
        this.id = id;
        this.responseContainer = responseContainer;
        this.decisao = decisao;
        this.vo = vo;
    }

}
