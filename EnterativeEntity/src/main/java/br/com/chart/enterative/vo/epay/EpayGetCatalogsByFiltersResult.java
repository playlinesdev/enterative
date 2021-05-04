package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@XStreamAlias("GetCatalogsByFiltersResult")
public class EpayGetCatalogsByFiltersResult implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @XStreamImplicit
    @XStreamAlias("a:Catalog")
    @Getter @Setter private List<EpayCatalog> catalogs;

}
