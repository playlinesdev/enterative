package br.com.chart.enterative.entity.ws.bhn;

import br.com.chart.enterative.entity.transacao.Transaction;
import br.com.chart.enterative.entity.transacao.Header;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private Header header;
    private Transaction transaction;

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
