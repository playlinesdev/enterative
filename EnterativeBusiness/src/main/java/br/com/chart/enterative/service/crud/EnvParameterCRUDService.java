package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.EnvParameter;
import br.com.chart.enterative.entity.vo.EnvParameterVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import static br.com.chart.enterative.enums.CRUD_OPERATION.CREATE;
import static br.com.chart.enterative.enums.CRUD_OPERATION.DELETE;
import static br.com.chart.enterative.enums.CRUD_OPERATION.READ;
import static br.com.chart.enterative.enums.CRUD_OPERATION.UPDATE;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.SequencialVO;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.EnvParameterSearchVO;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class EnvParameterCRUDService extends UserAwareCRUDService<EnvParameter, Long, EnvParameterVO, EnvParameterSearchVO> {

    public EnvParameterCRUDService(UserAwareDAO<EnvParameter, Long> dao, ConverterService<EnvParameter, EnvParameterVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EnvParameterDAO dao() {
        return (EnvParameterDAO) super.dao();
    }

    public List<EnvParameterVO> retrieveUIList() {
        List<EnvParameterVO> parameters = this.findAllVO().sorted(Comparator.comparing(e -> Objects.nonNull(e.getName()) && !e.getName().isEmpty() ? e.getName() : e.getParam().getName())).collect(Collectors.toList());
        List<ENVIRONMENT_PARAMETER> parametersEnum = parameters.stream().map(EnvParameterVO::getParam).collect(Collectors.toList());

        Stream<ENVIRONMENT_PARAMETER> allEnums = Arrays.stream(ENVIRONMENT_PARAMETER.values());
        parameters.addAll(allEnums.filter(p -> !parametersEnum.contains(p))
                .map(p -> {
                    EnvParameterVO env = this.initVO();
                    env.setParam(p);
                    env.setName(p.getName());
                    env.setId(0L);
                    env.setValue(p.getDefaultValue().toString());
                    return env;
                }).sorted(Comparator.comparing(EnvParameterVO::getName)).collect(Collectors.toList()));
        return parameters;
    }

    public SequencialVO getSequenciais(Long user) {
        Integer numero = this.dao().get(ENVIRONMENT_PARAMETER.SEQUENCIALTRANSACAO);
        this.dao().set(ENVIRONMENT_PARAMETER.SEQUENCIALTRANSACAO, ++numero, user);

        return new SequencialVO(String.format("%012d", numero), String.format("%06d", numero));
    }

    @Override
    public ServiceResponse validate(EnvParameterVO vo, CRUD_OPERATION operation) {
        ServiceResponse response = new ServiceResponse();
        switch (operation) {
            case CREATE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case DELETE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case READ:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case UPDATE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
        }
        return response;
    }

    @Override
    protected Supplier<EnvParameter> initEntitySupplier() {
        return EnvParameter::new;
    }

    @Override
    protected Supplier<EnvParameterVO> initVOSupplier() {
        return EnvParameterVO::new;
    }
}
