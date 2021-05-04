package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@XStreamAlias("Item")
public class EpayReportItem {

    @XStreamAlias("Data")
    @Getter @Setter private String data;
    
    @XStreamAlias("Operadora")
    @Getter @Setter private String operadora;
    
    @XStreamAlias("Qtde")
    @Getter @Setter private String qtde;
    
    @XStreamAlias("Valor")
    @Getter @Setter private String valor;
    
    @XStreamAlias("PercComiss")
    @Getter @Setter private String percComiss;
    
    @XStreamAlias("ValorComissao")
    @Getter @Setter private String valorComissao;
    
    @XStreamAlias("ValorLiquido")
    @Getter @Setter private String valorLiquido;
    
    @XStreamAlias("DataHoraVenda")
    @Getter @Setter private String dataHoraVenda;
    
    @XStreamAlias("NSU")
    @Getter @Setter private String nsu;
    
    @XStreamAlias("NSU_02")
    @Getter @Setter private String nsu02;
    
    @XStreamAlias("CNPJ")
    @Getter @Setter private String cnpj;
    
    @XStreamAlias("NomeFantasia")
    @Getter @Setter private String nomeFantasia;
    
    @XStreamAlias("Telefone")
    @Getter @Setter private String telefone;
}
