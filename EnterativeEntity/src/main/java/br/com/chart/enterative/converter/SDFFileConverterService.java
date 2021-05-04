package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.SDFFile;
import br.com.chart.enterative.entity.vo.SDFFileVO;
import br.com.chart.enterative.entity.vo.SDFHeaderVO;
import br.com.chart.enterative.entity.vo.SDFTrailerVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import br.com.chart.enterative.vo.search.SDFValidationSearchVO;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SDFFileConverterService extends ConverterService<SDFFile, SDFFileVO> {

    @Autowired
    private SDFDetailConverterService detailConverterService;

    @Autowired
    private SDFHeaderConverterService headerConverterService;

    @Autowired
    private SDFTrailerConverterService trailerConverterService;

    public SDFFileConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public SDFFile convert(SDFFileVO vo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDFFileVO convert(SDFFile entity) {
        SDFFileVO vo = new SDFFileVO();

        vo.setCreatedAt(entity.getCreatedAt());
        vo.setDetails(detailConverterService.convertListEntity(entity.getDetails()));
        if (Objects.nonNull(entity.getHeader())) {
            vo.setHeader(headerConverterService.convert(entity.getHeader()));
        } else {
            vo.setHeader(new SDFHeaderVO());
        }
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setStatus(entity.getStatus());
        if (Objects.nonNull(entity.getTrailer())) {
            vo.setTrailer(trailerConverterService.convert(entity.getTrailer()));
        } else {
            vo.setTrailer(new SDFTrailerVO());
        }
        return vo;
    }

    public SDFValidationSearchVO toSDFValidationSearchVO(SDFFile file) {
        SDFValidationSearchVO result = new SDFValidationSearchVO();

        result.setCreatedAt(file.getCreatedAt());
        result.setId(file.getId());
        result.setName(file.getName());
        result.setStatus(file.getStatus());

        return result;
    }

}
