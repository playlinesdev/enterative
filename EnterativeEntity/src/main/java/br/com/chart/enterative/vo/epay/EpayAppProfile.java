package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@XStreamAlias("AppProfile")
public class EpayAppProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    // @Getter @Setter private EpayClerkIds[] clerkIds;
    // @Getter @Setter private EpayClerks[] clerks;
    // @Getter @Setter private EpayExtendedProperties extendedProperties;
    
    @XStreamAlias("Description")
    @Getter @Setter private String description;
    
    @XStreamAlias("IBOAccountId")
    @Getter @Setter private Long iboAccountId;
    
    @XStreamAlias("IBOSalesNodeId")
    @Getter @Setter private Long iboSalesNodeId;
    
    @XStreamAlias("Name")
    @Getter @Setter private String name;
    
    @XStreamAlias("TID")
    @Getter @Setter private Long tid;
    
    @XStreamAlias("TerminalId")
    @Getter @Setter private Long terminalId;
    
    @XStreamAlias("RetailerAcc")
    @Getter @Setter private Long retailerAcc;
    
    @XStreamAlias("ValidateClerkId")
    @Getter @Setter private boolean validateClerkId;
    
    @XStreamAlias("WebPOSAgentAdminId")
    @Getter @Setter private Long webPOSAgentAdminId;
    
    @XStreamAlias("WebPOSRetailerId")
    @Getter @Setter private Long webPOSRetailerId;
}