package br.com.chart.enterative.entity.transacao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptsFields {

    private String line;

    public ReceiptsFields() {
        this.line = "";
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line += line;
    }
}
