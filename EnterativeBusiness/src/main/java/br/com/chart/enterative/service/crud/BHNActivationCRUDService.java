package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.BHNActivationDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.AccountTransaction;
import br.com.chart.enterative.entity.AccountTransactionDeadFile;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.BHNTransaction;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.vo.BHNActivationVO;
import br.com.chart.enterative.entity.vo.SaleOrderVO;
import br.com.chart.enterative.enums.ACTIVATION_QUEUE_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.BHNActivationSearchVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class BHNActivationCRUDService extends UserAwareCRUDService<BHNActivation, Long, BHNActivationVO, BHNActivationSearchVO> {

    @Autowired
    private AccountTransactionCRUDService accountTransactionService;

    @Autowired
    private AccountTransactionDeadFileCRUDService accountTransactionDeadFileService;

    @Autowired
    private SaleOrderLineCRUDService saleOrderLineService;

    @Autowired
    private BHNTransactionCRUDService bhnTransactionService;

    @Autowired
    private ShopCRUDService shopService;

    public BHNActivationCRUDService(UserAwareDAO<BHNActivation, Long> dao, ConverterService<BHNActivation, BHNActivationVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BHNActivationDAO dao() {
        return (BHNActivationDAO) super.dao();
    }

    @Override
    public PageWrapper<BHNActivationVO> retrieve(BHNActivationSearchVO searchForm, Pageable pageable, String url) {
        Page<BHNActivation> page;

        if (Objects.nonNull(searchForm.getId()) && searchForm.getId() > 0) {
            page = this.dao().findById(searchForm.getId(), pageable);
        } else if (Objects.nonNull(searchForm.getExternalCode()) && !searchForm.getExternalCode().isEmpty()) {
            page = this.dao().findByExternalCode(searchForm.getExternalCode(), pageable);
        } else if (Objects.nonNull(searchForm.getStartDate()) && Objects.isNull(searchForm.getEndDate())) {
            page = this.dao().findByCreatedAtGreaterThan(searchForm.getStartDate(), pageable);
        } else if (Objects.isNull(searchForm.getStartDate()) && Objects.nonNull(searchForm.getEndDate())) {
            page = this.dao().findByCreatedAtLessThan(searchForm.getEndDate(), pageable);
        } else if (Objects.nonNull(searchForm.getStartDate()) && Objects.nonNull(searchForm.getEndDate())) {
            page = this.dao().findByCreatedAtBetween(searchForm.getStartDate(), searchForm.getEndDate(), pageable);
        } else {
            page = this.dao().findAll(pageable);
        }

        return new PageWrapper<>(page.map(this::convertForUI), url);
    }

    public BHNActivationVO convertForUI(BHNActivation activation) {
        if (Objects.nonNull(activation)) {
            BHNActivationVO result = this.converter().convert(activation);

            List<BHNTransaction> transactions = this.bhnTransactionService.findByBhnActivationId(activation.getId());

            if (Objects.nonNull(transactions) && !transactions.isEmpty()) {
                result.setTransactions(this.bhnTransactionService.converter().convertListEntity(transactions));
            } else {
                result.setTransactions(new ArrayList<>());
            }

            SaleOrderLine line = this.saleOrderLineService.findByExternalCode(activation.getExternalCode());
            if (Objects.nonNull(line)) {
                result.setSaleOrder(new SaleOrderVO());
                result.getSaleOrder().setId(line.getSaleOrder().getId());

                List<AccountTransaction> accountTransactions = this.accountTransactionService.findBySaleOrderLineId(line.getId());
                List<AccountTransactionDeadFile> accountTransactionsDeadFile = this.accountTransactionDeadFileService.findBySaleOrderLineId(line.getId());

                if (Objects.nonNull(accountTransactions)) {
                    result.setAccountTransactions(accountTransactions.stream().map(this.accountTransactionService.converter()::convert).collect(Collectors.toList()));
                } else {
                    result.setAccountTransactions(new ArrayList<>(0));
                }

                if (Objects.nonNull(accountTransactionsDeadFile)) {
                    result.getAccountTransactions().addAll(accountTransactionsDeadFile.stream().map(this.accountTransactionDeadFileService.converter()::convert).collect(Collectors.toList()));
                }
            } else {
                result.setAccountTransactions(new ArrayList<>(0));
            }

            Shop shop = this.shopService.findByCode(activation.getShopCode());
            if (Objects.nonNull(shop)) {
                result.setShopCode(String.format("%s - %s", result.getShopCode(), shop.getName()));
            }

            return result;
        } else {
            return new BHNActivationVO();
        }
    }

    public BHNActivation findByExternalCode(String code) {
        return this.dao().findByExternalCode(code);
    }

    public List<BHNActivation> findByShopCode(String code) {
        return this.dao().findByShopCode(code);
    }

    public List<BHNActivation> findByStatusAndQueueStatus(ACTIVATION_STATUS status, ACTIVATION_QUEUE_STATUS queueStatus) {
        return this.dao().findByStatusAndQueueStatus(status, queueStatus);
    }

    public BHNActivation findByMerchantAndShopCodeAndTerminalAndExternalCode(Merchant merchant, String shopCode, String terminal, String externalCode) {
        return this.dao().findByMerchantAndShopCodeAndTerminalAndExternalCode(merchant, shopCode, terminal, externalCode);
    }

    public void setCallbackStatusForID(CALLBACK_STATUS status, Long id) {
        this.dao().setCallbackStatusForID(status, id);
    }

    public void cancel(Long id) {
        BHNActivation activation = this.findOne(id);

        activation.setStatus(ACTIVATION_STATUS.CANCELED);
        activation.setQueueStatus(ACTIVATION_QUEUE_STATUS.PROCESSED);
        activation.setCallbackStatus(CALLBACK_STATUS.DONE);

        this.saveAndFlush(activation, this.loggedUserId());
    }

    @Override
    public ServiceResponse validate(BHNActivationVO vo, CRUD_OPERATION operation) {
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
    protected Supplier<BHNActivation> initEntitySupplier() {
        return BHNActivation::new;
    }

    @Override
    protected Supplier<BHNActivationVO> initVOSupplier() {
        return BHNActivationVO::new;
    }
}
