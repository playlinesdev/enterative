package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.dao.BHNTransactionDAO;
import br.com.chart.enterative.dao.SDFDetailDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.BHNTransaction;
import br.com.chart.enterative.entity.SDFDetail;
import br.com.chart.enterative.entity.vo.BHNTransactionVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.converter.BHNTransactionConverterService;
import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.BHNTransactionSearchVO;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class BHNTransactionCRUDService extends UserAwareCRUDService<BHNTransaction, Long, BHNTransactionVO, BHNTransactionSearchVO> {

    @Autowired
    private SDFDetailDAO detailDAO;

    @Autowired
    private EnterativeUtils utils;

    public BHNTransactionCRUDService(UserAwareDAO<BHNTransaction, Long> dao, ConverterService<BHNTransaction, BHNTransactionVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BHNTransactionDAO dao() {
        return (BHNTransactionDAO) super.dao();
    }

    public List<BHNTransaction> findByBhnActivationId(Long id) {
        return this.dao().findByBhnActivationId(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BHNTransactionConverterService converter() {
        return super.converter();
    }

    public List<BHNTransaction> retrieveMissingByDate(String date) {
        return this.dao().retrieveMissingByDate(date);
    }

    public List<BHNTransactionVO> retrieveMissingByDate(List<String> dates, String responseCode) {
        return this.dao().retrieveMissingByDate(dates, responseCode)
                .stream().map(this.converter()::convert).collect(Collectors.toList());
    }

    public List<BHNTransaction> retrieveByDate(String date) {
        return this.dao().retrieveByDate(date);
    }

    public List<BHNTransaction> retrieveByDate(List<String> date, String responseCode) {
        return this.dao().retrieveByDate(date, responseCode);
    }

    public List<BHNTransactionVO> retrieveByDate(Long detailId) {
        final SDFDetail detail = this.detailDAO.findOne(detailId);

        String localTransactionDate = this.utils.fromDate("yyMMdd", detail.getPosTransactionDate());
        List<BHNTransaction> transacoes = this.dao().retrieveByDate(localTransactionDate, detail.getStoreId() + "%");

        List<BHNTransactionVO> result;
        result = transacoes.stream().map(t -> this.converter().convert(t, detail)).collect(Collectors.toList());
        return result;
    }

    public List<BHNTransactionVO> retrieveByGift(Long detailId) {
        SDFDetail detail = this.detailDAO.findOne(detailId);
        List<BHNTransaction> transacoes = this.dao().retrieveByGift(detail.getGiftCardNumber(), detail.getStoreId() + "%");

        List<BHNTransactionVO> result;
        result = transacoes.stream().map(t -> this.converter().convert(t, detail)).collect(Collectors.toList());
        return result;
    }

    public List<BHNTransactionVO> retrieveByProduct(Long detailId) {
        SDFDetail detail = this.detailDAO.findOne(detailId);
        List<BHNTransaction> transacoes = this.dao().retrieveByProduct(detail.getProductId(), detail.getStoreId() + "%");

        List<BHNTransactionVO> result;
        result = transacoes.stream().map(t -> this.converter().convert(t, detail)).collect(Collectors.toList());
        return result;
    }

    public BHNTransactionVO retrieveDetails(Long detailId, Long transactionId) {
        SDFDetail detail = this.detailDAO.findOne(detailId);
        BHNTransaction transacao = this.dao().findOne(transactionId);

        return this.converter().convert(transacao, detail);
    }

    public BHNTransaction retrieveByMatch(SDFDetail detail) {
        String transactionDate = this.utils.fromDate("yyMMdd", detail.getPosTransactionDate());
        String transactionTime = this.utils.fromDate("HHmmss", detail.getPosTransactionTime());
        String terminalId = this.utils.retrieveMerchantTerminalID(detail.getStoreId(), detail.getTerminalId());
        String responseCode = StringUtils.right(detail.getAuthResponseCode(), 2);

        BHNTransaction result = this.dao().retrieveByMatchPrimaryAccountNumber(transactionDate, transactionTime, terminalId, detail.getGiftCardNumber(),
                detail.getProductId(), responseCode, detail.getSystemTraceAuditNumber(), TRANSACTION_DIRECTION.RETURN);

        if (Objects.isNull(result)) {
            result = this.dao().retrieveByMatchActivationAccountNumber(transactionDate, transactionTime, terminalId, detail.getGiftCardNumber(), detail.getProductId(),
                    responseCode, detail.getSystemTraceAuditNumber(), TRANSACTION_DIRECTION.RETURN);
        }

        if (Objects.nonNull(result)) {
            double resultAmount = this.utils.fromDotlessString(result.getTransactionAmount(), 2).doubleValue();
            double detailAmount = detail.getProductItemPrice().doubleValue();

            if (!Objects.equals(resultAmount, detailAmount)) {
                result = null;
            }
        }

        return result;
    }

    public PageWrapper<BHNTransactionVO> retrieveTransactions(BHNTransactionSearchVO searchForm, Pageable pageable, String url) {

        Long resourceId = null;
        TRANSACTION_DIRECTION direction = searchForm.getDirection();

        if (Objects.nonNull(searchForm.getResource())) {
            resourceId = searchForm.getResource().getId();
        }

        if (Objects.nonNull(searchForm.getDirection())) {
            direction = searchForm.getDirection();
        }
        String redemptionPin = null;
        if (Objects.nonNull(searchForm.getRedemptionPin()) && !searchForm.getRedemptionPin().isEmpty()) {
            redemptionPin = searchForm.getRedemptionPin();
        }
        String primaryAccountNumber = null;
        if (!searchForm.getPrimaryAccountNumber().isEmpty()) {
            primaryAccountNumber = searchForm.getPrimaryAccountNumber();
        }
        String productId = null;
        if (!searchForm.getProductId().isEmpty()) {
            productId = searchForm.getProductId();
        }
        String systemTraceAuditNumber = null;
        if (StringUtils.isNotBlank(searchForm.getSystemTraceAuditNumber())) {
            systemTraceAuditNumber = searchForm.getSystemTraceAuditNumber();
        }

        Page<BHNTransaction> page = null;

        if (Objects.nonNull(systemTraceAuditNumber)) {
            page = this.dao().findBySystemTraceAuditNumberOrderByCreatedAtDesc(systemTraceAuditNumber, pageable);
        } else {
            if (Objects.nonNull(productId)) {
                if (Objects.nonNull(direction)) {
                    if (Objects.nonNull(resourceId)) {
                        if (Objects.nonNull(primaryAccountNumber)) {
                            if (Objects.nonNull(redemptionPin)) {
                                page = this.dao().findByProductIdAndDirectionAndResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(productId, direction, resourceId, primaryAccountNumber, redemptionPin, pageable);
                            } else {
                                page = this.dao().findByProductIdAndDirectionAndResourceIdAndPrimaryAccountNumberOrderByCreatedAtDesc(productId, direction, resourceId, primaryAccountNumber, pageable);
                            }
                        } else if (Objects.nonNull(redemptionPin)) {
                            page = this.dao().findByProductIdAndDirectionAndResourceIdAndRedemptionPinOrderByCreatedAtDesc(productId, direction, resourceId, redemptionPin, pageable);
                        } else {
                            page = this.dao().findByProductIdAndDirectionAndResourceIdAndOrderByCreatedAtOrderByCreatedAtDesc(productId, direction, resourceId, pageable);
                        }
                    } else if (Objects.nonNull(primaryAccountNumber)) {
                        if (Objects.nonNull(redemptionPin)) {
                            page = this.dao().findByProductIdAndDirectionAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(productId, direction, primaryAccountNumber, redemptionPin, pageable);
                        } else {
                            page = this.dao().findByProductIdAndDirectionAndPrimaryAccountNumberOrderByCreatedAtDesc(productId, direction, primaryAccountNumber, pageable);
                        }
                    } else if (Objects.nonNull(redemptionPin)) {
                        page = this.dao().findByProductIdAndDirectionAndRedemptionPinOrderByCreatedAtDesc(productId, direction, redemptionPin, pageable);
                    } else {
                        page = this.dao().findByProductIdAndDirectionOrderByCreatedAtDesc(productId, direction, pageable);
                    }
                } else if (Objects.nonNull(resourceId)) {
                    if (Objects.nonNull(primaryAccountNumber)) {
                        if (Objects.nonNull(redemptionPin)) {
                            page = this.dao().findByProductIdAndResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderbyCreatedAtDesc(productId, resourceId, primaryAccountNumber, redemptionPin, pageable);
                        } else {
                            page = this.dao().findByProductIdAndResourceIdAndPrimaryAccountNumberOrderbyCreatedAtDesc(productId, resourceId, primaryAccountNumber, pageable);
                        }
                    } else if (Objects.nonNull(redemptionPin)) {
                        page = this.dao().findByProductIdAndResourceIdAndRedemptionPinOrderbyCreatedAtDesc(productId, resourceId, redemptionPin, pageable);
                    } else {
                        page = this.dao().findByProductIdAndResourceIdOrderbyCreatedAtDesc(productId, resourceId, redemptionPin, pageable);
                    }
                } else if (Objects.nonNull(primaryAccountNumber)) {
                    if (Objects.nonNull(redemptionPin)) {
                        page = this.dao().findByProductIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(productId, primaryAccountNumber, redemptionPin, pageable);
                    } else {
                        page = this.dao().findByProductIdAndPrimaryAccountNumberOrderByCreatedAtDesc(productId, primaryAccountNumber, pageable);
                    }
                } else if (Objects.nonNull(redemptionPin)) {
                    page = this.dao().findByProductIdAndRedemptionPinOrderByCreatedAtDesc(productId, redemptionPin, pageable);
                } else {
                    page = this.dao().findByProductIdOrderByCreatedAtDesc(productId, pageable);
                }
            } else if (Objects.nonNull(direction)) {
                if (Objects.nonNull(resourceId)) {
                    if (Objects.nonNull(primaryAccountNumber)) {
                        if (Objects.nonNull(redemptionPin)) {
                            page = this.dao().findByDirectionAndResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(direction, resourceId, primaryAccountNumber, redemptionPin, pageable);
                        } else {
                            page = this.dao().findByDirectionAndResourceIdAndPrimaryAccountNumberOrderByCreatedAtDesc(direction, resourceId, primaryAccountNumber, pageable);
                        }
                    } else if (Objects.nonNull(redemptionPin)) {
                        page = this.dao().findByDirectionAndResourceIdAndRedemptionPinOrderByCreatedAtDesc(direction, resourceId, redemptionPin, pageable);
                    } else {
                        page = this.dao().findByDirectionAndResourceIdOrderByCreatedAtDesc(direction, resourceId, pageable);
                    }
                } else if (Objects.nonNull(primaryAccountNumber)) {
                    if (Objects.nonNull(redemptionPin)) {
                        page = this.dao().findByDirectionAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(direction, primaryAccountNumber, redemptionPin, pageable);
                    } else {
                        page = this.dao().findByDirectionAndPrimaryAccountNumberOrderByCreatedAtDesc(direction, primaryAccountNumber, pageable);
                    }
                } else if (Objects.nonNull(redemptionPin)) {
                    page = this.dao().findByDirectionAndRedemptionPinOrderByCreatedAtDesc(direction, redemptionPin, pageable);
                } else {
                    page = this.dao().findByDirectionOrderByCreatedAtDesc(direction, pageable);
                }
            } else if (Objects.nonNull(resourceId)) {
                if (Objects.nonNull(primaryAccountNumber)) {
                    if (Objects.nonNull(redemptionPin)) {
                        page = this.dao().findByResourceIdAndprimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(resourceId, primaryAccountNumber, redemptionPin, pageable);
                    } else {
                        page = this.dao().findByResourceIdAndprimaryAccountNumberOrderByCreatedAtDesc(resourceId, primaryAccountNumber, pageable);
                    }
                } else if (Objects.nonNull(redemptionPin)) {
                    page = this.dao().findByResourceIdAndRedemptionPinOrderByCreatedAtDesc(resourceId, redemptionPin, pageable);
                } else {
                    page = this.dao().findByResourceIdOrderByCreatedAtDesc(resourceId, pageable);
                }
            } else if (Objects.nonNull(primaryAccountNumber)) {
                if (Objects.nonNull(redemptionPin)) {
                    page = this.dao().findByPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(primaryAccountNumber, redemptionPin, pageable);
                } else {
                    page = this.dao().findByPrimaryAccountNumberOrderByCreatedAtDesc(primaryAccountNumber, pageable);
                }
            } else if (Objects.nonNull(redemptionPin)) {
                page = this.dao().findByRedemptionPinOrderByCreatedAtDesc(redemptionPin, pageable);
            } else {
                page = this.dao().findAll(pageable);
            }
        }

        return new PageWrapper<>(page.map(this.converter()::convert), url);
    }

    @Override
    protected Supplier<BHNTransaction> initEntitySupplier() {
        return BHNTransaction::new;
    }

    @Override
    protected Supplier<BHNTransactionVO> initVOSupplier() {
        return BHNTransactionVO::new;
    }

    @Override
    public ServiceResponse validate(BHNTransactionVO vo, CRUD_OPERATION operation
    ) {
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

}
