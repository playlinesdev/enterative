package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@XStreamAlias("a:Catalog")
public class EpayCatalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @XStreamAlias("a:Active")
    @Getter @Setter private boolean active;
    
    @XStreamAlias("a:Amount")
    @Getter @Setter private BigDecimal amount;
    
    @XStreamAlias("a:CanLookupAccount")
    @Getter @Setter private boolean canLookupAccount;
    
    @XStreamAlias("a:CatalogType")
    @Getter @Setter private String catalogType;
    
    @XStreamAlias("a:Category")
    @Getter @Setter private String category;
    
    @XStreamAlias("a:CategoryFlags")
    @Getter @Setter private Integer categoryFlags;
    
    @XStreamAlias("a:DisplayGroup")
    @Getter @Setter private String displayGroup;
    
    @XStreamAlias("a:DisplayGroupFlags")
    @Getter @Setter private Integer displayGroupFlags;
    
    @XStreamAlias("a:DisplayGroupOrder")
    @Getter @Setter private Integer displayGroupOrder;
    
    @XStreamAlias("a:DisplayGroupTips")
    @Getter @Setter private String diplayGroupTips;
    
    @XStreamAlias("a:Extensions")
    @Getter @Setter private List<EpayCatalogExtension> extensions;
    
    @XStreamAlias("a:ForeignValue")
    @Getter @Setter private Integer foreignValue;
    
    @XStreamAlias("a:LogoFile")
    @Getter @Setter private String logoFile;
    
    @XStreamAlias("a:MaximumValue")
    @Getter @Setter private Integer maximumValue;
    
    @XStreamAlias("a:MinimumValue")
    @Getter @Setter private Integer minimumValue;
    
    @XStreamAlias("a:PIRRequired")
    @Getter @Setter private boolean pirRequired;
    
    @XStreamAlias("a:PopularProduct")
    @Getter @Setter private boolean popularProduct;
    
    @XStreamAlias("a:ProductCatalogFlags")
    @Getter @Setter private Integer productCatalogFlags;
    
    @XStreamAlias("a:ProductExtensionData")
    @Getter @Setter private List<Object> productExtensionData;
    
    @XStreamAlias("a:ProductId")
    @Getter @Setter private String productId;
    
    @XStreamAlias("a:ProductType")
    @Getter @Setter private Object productType;
    
    @XStreamAlias("a:ScanRequired")
    @Getter @Setter private boolean scanRequired;
    
    @XStreamAlias("a:ServiceFee")
    @Getter @Setter private Integer serviceFee;
    
    @XStreamAlias("a:ShowDisplayGroup")
    @Getter @Setter private boolean showDisplayGroup;
    
    @XStreamAlias("a:SubProductId")
    @Getter @Setter private Object subProductId;
    
    @XStreamAlias("a:SwipeRequired")
    @Getter @Setter private boolean swipeRequired;
    
    @XStreamAlias("a:Title")
    @Getter @Setter private String title;
    
    @XStreamAlias("a:VariableValue")
    @Getter @Setter private boolean variableValue;
    
    @XStreamAlias("a:ValidityPeriod")
    @Getter @Setter private Integer validityPeriod;
}
