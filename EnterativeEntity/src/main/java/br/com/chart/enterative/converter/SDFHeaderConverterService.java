package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.SDFHeader;
import br.com.chart.enterative.entity.vo.SDFHeaderVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SDFHeaderConverterService extends ConverterService<SDFHeader, SDFHeaderVO> {

    public SDFHeaderConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public SDFHeader convert(SDFHeaderVO vo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDFHeaderVO convert(SDFHeader entity) {
        SDFHeaderVO vo = new SDFHeaderVO();
        vo.setFileReportingDate(entity.getFileReportingDate());
        vo.setFileTransmissionDate(entity.getFileTransmissionDate());
        vo.setFiller(entity.getFiller());
        vo.setId(entity.getId());
        vo.setPartnerId(entity.getPartnerId());
        vo.setPartnerName(entity.getPartnerName());
        return vo;
    }

}
