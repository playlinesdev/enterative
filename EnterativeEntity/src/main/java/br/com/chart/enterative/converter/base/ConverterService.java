package br.com.chart.enterative.converter.base;

import br.com.chart.enterative.entity.base.BaseEntity;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import br.com.chart.enterative.vo.base.BaseVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author William Leite
 * @param <E> Entity
 * @param <V> VO
 */
public abstract class ConverterService<E extends BaseEntity, V extends BaseVO> {

    protected EnterativeReflectionUtils reflectionUtils;

    public ConverterService(EnterativeReflectionUtils reflectionUtils) {
        this.reflectionUtils = reflectionUtils;
    }
    
    public abstract E convert(V vo);

    public List<E> convertListVO(List<V> vo) {
        return vo.stream().map(this::convert).collect(Collectors.toList());
    }

    public abstract V convert(E entity);

    public List<V> convertListEntity(List<E> entity) {
        return entity.stream().map(this::convert).collect(Collectors.toList());
    }

    public String asJSON(E entity) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(entity);
    }

    public Date toDate(String pattern, String date) throws ParseException {
        return new SimpleDateFormat(pattern).parse(date);
    }

    public String fromDate(String pattern, Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public BigDecimal fromDotlessString(String str, int scale) {
        int dec = str.length() - scale;
        String result = str.substring(0, dec).concat(".").concat(str.substring(dec));
        return new BigDecimal(result);
    }

    public String toOnlyNumbers(String str) {
        if (Objects.nonNull(str)) {
            return str.replaceAll("\\D+", "");
        } else {
            return "";
        }
    }

    public String cleanTextEdges(String value){
        return Optional.ofNullable(value).orElse("").trim();
    }

}
