package br.com.chart.enterative.vo.epay;

import br.com.chart.enterative.entity.EpayTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import br.com.chart.enterative.interfaces.EpayTransactionThreadListener;

public class EpayTransactionThreadVO {

    @Getter @Setter private Long id;
    @Getter @Setter private int counter;
    @Getter @Setter private EpayTransaction transaction;
    @Getter @Setter private List<EpayTransactionThreadListener> callback;

    public EpayTransactionThreadVO(Long id, EpayTransaction transaction) {
        this.id = id;
        this.transaction = transaction;
        this.callback = new ArrayList<>();
    }

    public boolean addCallback(EpayTransactionThreadListener callback) {
        if (Objects.isNull(this.callback)) {
            this.callback = new ArrayList<>();
        }
        return this.callback.add(callback);
    }
}
