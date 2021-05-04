package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.ProductDAO;
import br.com.chart.enterative.dao.ShopDAO;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.MerchantVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.ShopProductCommissionVO;
import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShopConverterService extends ConverterService<Shop, ShopVO> {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ShopDAO shopDAO;

    @Autowired
    private ShopProductCommissionConverterService shopProductCommissionConverterService;

    @Autowired
    private ShopPhoneConverterService shopPhoneConverterService;

    public ShopConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public Shop convert(ShopVO vo) {
        Shop entity;

        if (Objects.nonNull(vo.getId())) {
            entity = this.shopDAO.findOne(vo.getId());
        } else {
            entity = new Shop();
            entity.setCommissions(new ArrayList<>());
            entity.setPhones(new ArrayList<>());
        }

        entity.setAccount(this.reflectionUtils.asHollowLink(Account::new, vo.getAccount()));
        entity.setDistrict(vo.getDistrict());
        entity.setCep(vo.getCep());
        entity.setCity(vo.getCity());
        entity.setCnpj(this.toOnlyNumbers(vo.getCnpj()));
        
        if (Objects.nonNull(entity.getCnpj()) && entity.getCnpj().isEmpty()) {
            entity.setCnpj(null);
        }
        
        entity.setCpf(this.toOnlyNumbers(vo.getCpf()));
        
        if (Objects.nonNull(entity.getCpf()) && entity.getCpf().isEmpty()) {
            entity.setCpf(null);
        }
        
        entity.setCode(vo.getCode());
        entity.setContact(vo.getContact());
        entity.setFantasia(vo.getFantasia());
        entity.setId(vo.getId());
        entity.setStreet(vo.getStreet());
        entity.setMerchant(this.reflectionUtils.asHollowLink(Merchant::new, vo.getMerchant()));
        entity.setName(vo.getName());
        entity.setNumber(vo.getNumber());
        entity.setRazaoSocial(vo.getRazaoSocial());
        entity.setStatus(vo.getStatus());
        entity.setState(vo.getState());
        entity.setCountry(vo.getCountry());
        entity.setInscEstadual(vo.getInscEstadual());
        entity.setInscMunicipal(vo.getInscMunicipal());

        entity.getCommissions().clear();
        if (Objects.nonNull(vo.getCommissions())) {
            vo.getCommissions().stream()
                    .map(this.shopProductCommissionConverterService::convert)
                    .forEach(c -> {
                        c.setId(null);
                        c.setShop(entity);
                        entity.getCommissions().add(c);
                    });
        }

        entity.getPhones().clear();
        if (Objects.nonNull(vo.getPhones())) {
            vo.getPhones().stream()
                    .map(this.shopPhoneConverterService::convert)
                    .forEach(p -> {
                        p.setShop(entity);
                        entity.getPhones().add(p);
                    });
        }

        return entity;
    }

    @Override
    public ShopVO convert(Shop entity) {
        ShopVO vo = new ShopVO();
        vo.setAccount(this.reflectionUtils.asNamedLink(AccountVO::new, entity.getAccount()));
        vo.setDistrict(entity.getDistrict());
        vo.setCep(entity.getCep());
        vo.setCity(entity.getCity());
        vo.setCnpj(entity.getCnpj());
        vo.setCpf(entity.getCpf());
        vo.setCode(entity.getCode());
        vo.setContact(entity.getContact());
        vo.setFantasia(entity.getFantasia());
        vo.setId(entity.getId());
        vo.setStreet(entity.getStreet());
        vo.setMerchant(this.reflectionUtils.asNamedLink(MerchantVO::new, entity.getMerchant()));
        vo.setName(entity.getName());
        vo.setNumber(entity.getNumber());
        vo.setRazaoSocial(entity.getRazaoSocial());
        vo.setStatus(entity.getStatus());
        vo.setState(entity.getState());
        vo.setCountry(entity.getCountry());
        vo.setInscEstadual(entity.getInscEstadual());
        vo.setInscMunicipal(entity.getInscMunicipal());

        vo.setPhones(entity.getPhones().stream().map(this.shopPhoneConverterService::convert).collect(Collectors.toList()));

        vo.setCommissions(new ArrayList<>());

        Stream<Product> productStream = this.productDAO.findByStatusOrderByName(STATUS.ACTIVE).stream();

        if (Objects.nonNull(entity.getCommissions()) && !entity.getCommissions().isEmpty()) {
            productStream = productStream.filter(p -> entity.getCommissions().stream().noneMatch(c -> Objects.equals(c.getProduct().getId(), p.getId())));
            vo.getCommissions().addAll(entity.getCommissions().stream().map(this.shopProductCommissionConverterService::convert).collect(Collectors.toList()));
        }

        productStream.forEach(p -> {
            vo.getCommissions().add(new ShopProductCommissionVO(new ProductVO(p.getId(), p.getName()), BigDecimal.ZERO, BigDecimal.ZERO));
        });

        vo.getCommissions().sort(Comparator.comparing(c -> c.getProduct().getName()));

        return vo;
    }
}
