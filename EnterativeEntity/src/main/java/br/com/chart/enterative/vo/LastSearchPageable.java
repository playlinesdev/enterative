package br.com.chart.enterative.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author William Leite
 */
@Getter
@Setter
public class LastSearchPageable {
    private static final long serialVersionUID = 1L;

    private Integer pageNumber;
    private Integer pageSize;

    public static LastSearchPageable of(Integer pageNumber, Integer pageSize) {
        LastSearchPageable pageable = new LastSearchPageable();
        pageable.setPageNumber(pageNumber);
        pageable.setPageSize(pageSize);
        return pageable;
    }
}