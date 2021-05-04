package br.com.chart.enterative.vo.bhn;

import br.com.chart.enterative.entity.BHNTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import br.com.chart.enterative.interfaces.BHNTransactionThreadListener;

public class BHNTransactionThreadVO {

    @Getter @Setter private Long id;
    @Getter @Setter private int counter;
    @Getter @Setter private boolean reversal;
    @Getter @Setter private BHNTransaction bhnTransaction;
    @Getter @Setter private List<BHNTransactionThreadListener> callback;

    public BHNTransactionThreadVO(Long id, BHNTransaction bhnTransaction, boolean reversal) {
        this.id = id;
        this.bhnTransaction = bhnTransaction;
        this.callback = new ArrayList<>();
        this.reversal = reversal;
    }

    public boolean addCallback(BHNTransactionThreadListener callback) {
        if (Objects.isNull(this.callback)) {
            this.callback = new ArrayList<>();
        }
        return this.callback.add(callback);
    }
}
