package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@XStreamAlias("GetDisplayGroupResult")
public class EpayGetDisplayGroupResult {
    
    @XStreamImplicit
    @XStreamAlias("a:string")
    @Getter @Setter private String[] displayGroups;
}
