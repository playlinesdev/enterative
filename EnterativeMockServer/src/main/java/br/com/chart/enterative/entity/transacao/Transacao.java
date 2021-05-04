package br.com.chart.enterative.entity.transacao;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transacao implements Serializable {

    private Header header;
    private Transaction transaction;

    public Transacao() {
        this.header = new Header();
        this.transaction = new Transaction();
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
