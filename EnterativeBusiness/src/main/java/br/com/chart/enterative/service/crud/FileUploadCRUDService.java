package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.AccountDAO;
import br.com.chart.enterative.dao.FileUploadDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.AccountType;
import br.com.chart.enterative.entity.FileUpload;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.FileUploadVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.enums.*;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountSearchVO;
import br.com.chart.enterative.vo.search.FileUploadSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 *
 * @author William Leite
 */
@Service
public class FileUploadCRUDService extends UserAwareCRUDService<FileUpload, Long, FileUploadVO, FileUploadSearchVO> {

    @Autowired
    private ProductCRUDService productService;

    public FileUploadCRUDService(UserAwareDAO<FileUpload, Long> dao, ConverterService<FileUpload, FileUploadVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public FileUploadDAO dao() {
        return (FileUploadDAO) super.dao();
    }

    public PageWrapper<FileUploadVO> retrieveFiles(FileUploadSearchVO searchForm, Pageable pageable, String url) {
        Page<FileUploadVO> page;

        FileUpload probe = new FileUpload();
        if (Objects.nonNull(searchForm.getType())) {
            probe.setType(searchForm.getType());
        }
        if (Objects.nonNull(searchForm.getObjectID())) {
            probe.setObjectID(searchForm.getObjectID());
        }

        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        return new PageWrapper<>(this.dao().findAll(Example.of(probe, matcher), pageable).map(this.converter()::convert), url);
    }

    @Override
    protected Supplier<FileUpload> initEntitySupplier() {
        return FileUpload::new;
    }

    @Override
    protected Supplier<FileUploadVO> initVOSupplier() {
        return FileUploadVO::new;
    }

    @Override
    public ServiceResponse validate(FileUploadVO vo, CRUD_OPERATION operation) {
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

    public void delete(Long id) {
        this.dao().deleteById(id);
    }

    public List<?> retrieveObjects(String type) {
        List<?> objects;
        FILE_TYPE fileType = FILE_TYPE.valueOf(type);

        switch (fileType) {
            case PRODUCT_IMAGE:
                objects = this.productService.findAllVO().sorted(Comparator.comparing(ProductVO::getDisplayName)).collect(Collectors.toList());
                break;
            default:
                objects = Collections.EMPTY_LIST;
                break;
        }

        return objects;
    }
}