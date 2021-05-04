package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.entity.MerchantCategory;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.MerchantCategoryVO;
import br.com.chart.enterative.entity.vo.MerchantVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class MerchantConverterService extends ConverterService<Merchant, MerchantVO> {

    public MerchantConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public Merchant convert(MerchantVO vo) {
        Merchant entity = new Merchant();
        entity.setAcquiringInstitutionIdentifier(vo.getAcquiringInstitutionIdentifier());
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCep(vo.getCep());
        entity.setCity(vo.getCity());
        entity.setCnpj(vo.getCnpj());
        entity.setContact(vo.getContact());
        entity.setCpf(vo.getCpf());
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setDistrict(vo.getDistrict());
        entity.setEmail(vo.getEmail());
        entity.setId(vo.getId());
        entity.setCategory(this.reflectionUtils.asHollowLink(MerchantCategory::new, vo.getCategory()));
        entity.setMerchantIdentifier(vo.getMerchantIdentifier());
        entity.setMerchantLocation(vo.getMerchantLocation());
        entity.setName(vo.getName());
        entity.setNumber(vo.getNumber());
        entity.setObservation(vo.getObservation());
        entity.setPhone(vo.getPhone());
        entity.setState(vo.getState());
        entity.setStatus(vo.getStatus());
        entity.setStreet(vo.getStreet());
        return entity;
    }

    @Override
    public MerchantVO convert(Merchant entity) {
        MerchantVO vo = new MerchantVO();
        vo.setAcquiringInstitutionIdentifier(entity.getAcquiringInstitutionIdentifier());
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCep(entity.getCep());
        vo.setCity(entity.getCity());
        vo.setCnpj(entity.getCnpj());
        vo.setContact(entity.getContact());
        vo.setCpf(entity.getCpf());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setDistrict(entity.getDistrict());
        vo.setEmail(entity.getEmail());
        vo.setId(entity.getId());
        vo.setCategory(this.reflectionUtils.asNamedLink(MerchantCategoryVO::new, entity.getCategory()));
        vo.setMerchantIdentifier(entity.getMerchantIdentifier());
        vo.setMerchantLocation(entity.getMerchantLocation());
        vo.setName(entity.getName());
        vo.setNumber(entity.getNumber());
        vo.setObservation(entity.getObservation());
        vo.setPhone(entity.getPhone());
        vo.setState(entity.getState());
        vo.setStatus(entity.getStatus());
        vo.setStreet(entity.getStreet());
        return vo;
    }

}
