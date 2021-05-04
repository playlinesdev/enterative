package br.com.chart.enterative.ws.model;

import br.com.chart.enterative.entity.vo.ProductVO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class FlutterProduct implements Serializable {
    private Long id;
    private String displayName;
    private String imagem;
    private BigDecimal amount;
    private Boolean favorite;
    private Boolean highlight;

    public static FlutterProduct fromVO(final ProductVO vo) {
        FlutterProduct fp = new FlutterProduct();
        fp.setId(vo.getId());
        fp.setAmount(vo.getAmount());
        fp.setDisplayName(vo.getDisplayName());
        fp.setFavorite(vo.getFavorite());
        fp.setImagem(vo.getImagem());
        return fp;
    }
}