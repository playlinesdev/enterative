package br.com.chart.enterative.service;

import br.com.chart.enterative.converter.base.HelperConverterService;
import br.com.chart.enterative.dao.ProductDAO;
import br.com.chart.enterative.dao.SDFDetailDAO;
import br.com.chart.enterative.dao.SDFFileDAO;
import br.com.chart.enterative.entity.*;
import br.com.chart.enterative.entity.vo.BHNTransactionVO;
import br.com.chart.enterative.entity.vo.SDFDetailVO;
import br.com.chart.enterative.entity.vo.SDFFileVO;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_TYPE;
import br.com.chart.enterative.enums.SDF_DETAIL_STATUS;
import br.com.chart.enterative.enums.SDF_FILE_STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.converter.SDFDetailConverterService;
import br.com.chart.enterative.converter.SDFFileConverterService;
import br.com.chart.enterative.helper.LogService;
import br.com.chart.enterative.service.crud.BHNTransactionCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.SDFFileSummaryDetailVO;
import br.com.chart.enterative.vo.SDFFileSummaryDiffVO;
import br.com.chart.enterative.vo.SDFFileSummaryTransactionVO;
import br.com.chart.enterative.vo.SDFFileSummaryVO;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.SDFReportSearchVO;
import br.com.chart.enterative.vo.search.SDFValidationSearchVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
@Service
public class SDFValidationService extends LogService {

    @Autowired
    private SDFFileDAO fileDAO;

    @Autowired
    private SDFDetailDAO detailDAO;

    @Autowired
    private BHNTransactionCRUDService bhnTransactionCRUDService;

    @Autowired
    private SDFFileConverterService fileConverterService;

    @Autowired
    private SDFDetailConverterService detailConverterService;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private EnterativeUtils utils;

    @Autowired
    private HelperConverterService helperConverterService;

    public Map<String, Object> assembleReportVariables(SDFReportSearchVO vo) {
        Map<String, Object> result = new HashMap<>();

        SDFFile file = new SDFFile();
        file.setName("Relatório de Periodo");
        file.setHeader(new SDFHeader());
        file.getHeader().setFileReportingDate(vo.getStart());
        file.getHeader().setFileTransmissionDate(vo.getEnd());
        result.put("sdfFile", file);

        List<SDFDetail> details = this.detailDAO.findByPosTransactionDateBetweenAndStatus(vo.getStart(), vo.getEnd(), SDF_DETAIL_STATUS.NOT_FOUND);
        result.put("details", details.stream().map(detailConverterService::convert).collect(Collectors.toList()));

        final List<BHNTransactionVO> transactions = new ArrayList<>();
        List<String> dates = this.retrieveTransactionsDates(details);
        if (!details.isEmpty()) {
            // Return only approved transactions
            transactions.addAll(this.bhnTransactionCRUDService.retrieveMissingByDate(dates, "00"));
        }
        result.put("transactions", transactions);

        return result;
    }

    public Map<String, Object> assembleReportVariables(Long id) {
        Map<String, Object> result = new HashMap<>();

        SDFFile file = this.fileDAO.findOne(id);
        result.put("sdfFile", fileConverterService.convert(file));

        final List<SDFDetailVO> details = new ArrayList<>();
        details.addAll(file.getDetails().stream().filter(d -> d.getStatus() == SDF_DETAIL_STATUS.NOT_FOUND).map(detailConverterService::convert).collect(Collectors.toList()));
        result.put("details", details);

        final List<BHNTransactionVO> transactions = new ArrayList<>();
        List<String> dates = this.retrieveTransactionsDates(file.getDetails());
        // Return only approved transactions
        transactions.addAll(this.bhnTransactionCRUDService.retrieveMissingByDate(dates, "00"));
        result.put("transactions", transactions);

        return result;
    }

    public void save(Long fileId) {
        this.fileDAO.setStatusById(SDF_FILE_STATUS.DONE, fileId);
    }

    public String clearTransaction(Long detailId) {
        String result = "OK";

        SDFDetail detail = this.detailDAO.findOne(detailId);
        this.fileDAO.setStatusById(SDF_FILE_STATUS.IN_PROGRESS, detail.getFile().getId());
        this.detailDAO.setBhnTransactionAndStatusById(null, SDF_DETAIL_STATUS.NOT_FOUND, detailId);

        return result;
    }

    public String saveTransaction(Long detailId, BHNTransaction transaction) {
        String result = "OK";

        if (Objects.isNull(detailId) || detailId <= 0 || Objects.isNull(transaction)) {
            result = "IllegalArgumentException: Um ou mais campos não foram preenchidos!";
        } else {
            SDFDetail detail = this.detailDAO.findOne(detailId);
            this.fileDAO.setStatusById(SDF_FILE_STATUS.IN_PROGRESS, detail.getFile().getId());

            this.detailDAO.setBhnTransactionAndStatusById(transaction.getId(), SDF_DETAIL_STATUS.FOUND_WRONG, detailId);
        }

        return result;
    }

    public List<SDFValidationSearchVO> searchSDF(SDFValidationSearchVO param) {
        Date begin = this.utils.firstMoment(param.getCreatedAt());
        Date end = this.utils.lastMoment(param.getCreatedAt());
        List<SDFFile> list;

        if (Objects.nonNull(param.getName()) && param.getName().isEmpty()) {
            param.setName(null);
        }

        if (Objects.nonNull(param.getStatus())) {
            if (Objects.nonNull(begin) && Objects.nonNull(end)) {
                if (Objects.nonNull(param.getName())) {
                    list = this.fileDAO.findByNameContainingIgnoreCaseAndCreatedAtBetweenAndStatusOrderByName(param.getName(), begin, end, param.getStatus());
                } else {
                    list = this.fileDAO.findByCreatedAtBetweenAndStatusOrderByName(begin, end, param.getStatus());
                }
            } else {
                if (Objects.nonNull(param.getName())) {
                    list = this.fileDAO.findByNameContainingIgnoreCaseAndStatusOrderByName(param.getName(), param.getStatus());
                } else {
                    list = this.fileDAO.findByStatusOrderByName(param.getStatus());
                }
            }
        } else {
            if (Objects.nonNull(begin) && Objects.nonNull(end)) {
                if (Objects.nonNull(param.getName())) {
                    list = this.fileDAO.findByNameContainingIgnoreCaseAndCreatedAtBetweenOrderByName(param.getName(), begin, end);
                } else {
                    list = this.fileDAO.findByCreatedAtBetweenOrderByName(begin, end);
                }
            } else {
                if (Objects.nonNull(param.getName())) {
                    list = this.fileDAO.findByNameContainingIgnoreCaseOrderByName(param.getName());
                } else {
                    list = this.fileDAO.findAll().collect(Collectors.toList());
                }
            }
        }

        return list.stream().map(this.fileConverterService::toSDFValidationSearchVO).collect(Collectors.toList());
    }

    public SDFFileSummaryVO summarizeFile(Long fileId) {
        SDFFileSummaryVO summary = new SDFFileSummaryVO();

        summary.setDetails(this.summarizeDetails(fileId));
        summary.setTransactions(this.summarizeTransactions(fileId));
        summary.setDifference(this.summarizeDifference(summary));
        summary.setDifferent(this.isDifferent(summary.getDifference()));

        return summary;
    }

    private boolean isDifferent(SDFFileSummaryDiffVO diff) {
        boolean result = true;

        result &= Objects.equals(diff.getCommissionAmount().doubleValue(), 0d);
        result &= Objects.equals(diff.getConsumerFeeAmount().doubleValue(), 0d);
        result &= Objects.equals(diff.getCount(), 0L);
        result &= Objects.equals(diff.getNetAmount().doubleValue(), 0d);
        result &= Objects.equals(diff.getTaxCommissionAmount().doubleValue(), 0d);
        result &= Objects.equals(diff.getTaxTransactionAmount().doubleValue(), 0d);
        result &= Objects.equals(diff.getTransactionAmount().doubleValue(), 0d);

        return !result;
    }

    private SDFFileSummaryDiffVO summarizeDifference(SDFFileSummaryVO summary) {
        SDFFileSummaryDiffVO result = new SDFFileSummaryDiffVO();

        SDFFileSummaryDetailVO detailTotal = summary.getDetails().stream()
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.reducing(
                                        (SDFFileSummaryDetailVO d1, SDFFileSummaryDetailVO d2)
                                        -> new SDFFileSummaryDetailVO(d1.getCount() + d2.getCount(),
                                                d1.getTransactionAmount().add(d2.getTransactionAmount()),
                                                d1.getConsumerFeeAmount().add(d2.getConsumerFeeAmount()),
                                                d1.getCommissionAmount().add(d2.getCommissionAmount()),
                                                d1.getTaxTransactionAmount().add(d2.getTaxTransactionAmount()),
                                                d1.getTaxCommissionAmount().add(d2.getTaxCommissionAmount()),
                                                d1.getNetAmount().add(d2.getNetAmount()))
                                ),
                                o -> o.isPresent() ? o.get() : new SDFFileSummaryDetailVO()
                        )
                );

        SDFFileSummaryTransactionVO transactionTotal;
        if (Objects.isNull(summary.getTransactions()) || summary.getTransactions().isEmpty()) {
            transactionTotal = new SDFFileSummaryTransactionVO();
        } else {
            transactionTotal = summary.getTransactions().stream()
                    .collect(
                            Collectors.collectingAndThen(
                                    Collectors.reducing(
                                            (SDFFileSummaryTransactionVO t1, SDFFileSummaryTransactionVO t2)
                                            -> new SDFFileSummaryTransactionVO(t1.getCount() + t2.getCount(),
                                                    t1.getTransactionAmount().add(t2.getTransactionAmount()),
                                                    t1.getConsumerFeeAmount().add(t2.getConsumerFeeAmount()),
                                                    t1.getCommissionAmount().add(t2.getCommissionAmount()),
                                                    t1.getTaxTransactionAmount().add(t2.getTaxTransactionAmount()),
                                                    t1.getTaxCommissionAmount().add(t2.getTaxCommissionAmount()),
                                                    t1.getNetAmount().add(t2.getNetAmount()))
                                    ),
                                    Optional::get
                            )
                    );
        }

        result.setCommissionAmount(detailTotal.getCommissionAmount().abs().subtract(transactionTotal.getCommissionAmount().abs()).abs());
        result.setConsumerFeeAmount(detailTotal.getConsumerFeeAmount().subtract(transactionTotal.getConsumerFeeAmount()).abs());
        result.setCount(Math.abs(detailTotal.getCount() - transactionTotal.getCount()));
        result.setNetAmount(detailTotal.getNetAmount().subtract(transactionTotal.getNetAmount()).abs());
        result.setTaxCommissionAmount(detailTotal.getTaxCommissionAmount().subtract(transactionTotal.getTaxCommissionAmount()).abs());
        result.setTaxTransactionAmount(detailTotal.getTaxTransactionAmount().subtract(transactionTotal.getTaxTransactionAmount()).abs());
        result.setTransactionAmount(detailTotal.getTransactionAmount().subtract(transactionTotal.getTransactionAmount()).abs());

        return result;
    }

    private List<String> retrieveTransactionsDates(List<SDFDetail> details) {
        Comparator<SDFDetail> comparator = Comparator.comparing(SDFDetail::getPosTransactionDate);
        Optional<SDFDetail> dateMax = details.stream().max(comparator);
        Optional<SDFDetail> dateMin = details.stream().min(comparator);

        if (dateMax.isPresent() && dateMin.isPresent()) {
            List<Date> dates = this.utils.getDatesBetween(dateMin.get().getPosTransactionDate(), dateMax.get().getPosTransactionDate());
            return dates.stream().map(d -> this.utils.fromDate("yyMMdd", d)).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private List<SDFFileSummaryTransactionVO> summarizeTransactions(Long fileId) {
        SDFFile file = this.fileDAO.findOne(fileId);
        List<String> dates = this.retrieveTransactionsDates(file.getDetails());
        // Retrieve only approved transactions
        List<BHNTransaction> transactionList;
        if (dates.isEmpty()) {
            transactionList = new ArrayList<>();
        } else {
            transactionList = this.bhnTransactionCRUDService.retrieveByDate(dates, "00");
        }

        final Map<SDF_DETAIL_STATUS, Map<ACTIVATION_STATUS, Map<ACTIVATION_TYPE, SDFFileSummaryTransactionVO>>> temp = new HashMap<>();
        final Random rnd = new Random();

        transactionList.stream().forEach(t -> {
            SDFDetail sdfDetail = this.detailDAO.findByBhnTransaction(t.getId());
            SDF_DETAIL_STATUS status = SDF_DETAIL_STATUS.NOT_FOUND;
            if (Objects.nonNull(sdfDetail)) {
                status = sdfDetail.getStatus();
            }

            SDFFileSummaryTransactionVO summary;
            if (!temp.containsKey(status)) {
                temp.put(status, new HashMap<>());
            }
            if (!temp.get(status).containsKey(t.getBhnActivation().getStatus())) {
                temp.get(status).put(t.getBhnActivation().getStatus(), new HashMap<>());
            }
            if (temp.get(status).get(t.getBhnActivation().getStatus()).containsKey(t.getBhnActivation().getType())) {
                summary = temp.get(status).get(t.getBhnActivation().getStatus()).get(t.getBhnActivation().getType());
            } else {
                summary = new SDFFileSummaryTransactionVO();
                summary.setId(rnd.nextLong());
            }

            summary.setDetailStatus(status);
            summary.setStatus(t.getBhnActivation().getStatus());
            summary.setType(t.getBhnActivation().getType());

            BigDecimal transactionAmount = this.utils.fromDotlessString(t.getTransactionAmount(), 2);
            summary.setTransactionAmount(summary.getTransactionAmount().add(transactionAmount));
            summary.setCount(summary.getCount() + 1);

            Product product = this.productDAO.findByEanStartingWith(t.getProductId());
            summary.setCommissionAmount(summary.getCommissionAmount().add(this.calculateCommissionValue(product)));
            summary.setConsumerFeeAmount(summary.getConsumerFeeAmount().add(product.getActivationFee()));

            BigDecimal netAmount = summary.getTransactionAmount();
            netAmount = netAmount.add(summary.getConsumerFeeAmount());
            netAmount = netAmount.subtract(summary.getCommissionAmount());

            summary.setNetAmount(netAmount);

            temp.get(status).get(t.getBhnActivation().getStatus()).put(t.getBhnActivation().getType(), summary);
        });

        return temp.entrySet().stream()
                .flatMap(oe -> oe.getValue().entrySet().stream().flatMap(me -> me.getValue().entrySet().stream().map(s -> s.getValue())))
                .collect(Collectors.toList());
    }

    private BigDecimal calculateCommissionValue(Product product) {
        BigDecimal result = BigDecimal.ZERO.setScale(2);
        BigDecimal amount = product.getAmount();

        if (Objects.nonNull(product.getCommissionPercentage())) {
            result = amount.multiply(product.getCommissionPercentage());
        } else if (Objects.nonNull(product.getCommissionAmount())) {
            result = product.getCommissionAmount();
        }

        return result;
    }

    private List<SDFFileSummaryDetailVO> summarizeDetails(Long fileId) {
        final Random rnd = new Random();
        SDFFile file = this.fileDAO.findOne(fileId);
        final Map<SDF_DETAIL_STATUS, Map<String, Map<String, SDFFileSummaryDetailVO>>> temp = new HashMap<>();
        file.getDetails().stream().forEach(d -> {
            SDFFileSummaryDetailVO summary;
            if (!temp.containsKey(d.getStatus())) {
                temp.put(d.getStatus(), new HashMap<>());
            }
            if (!temp.get(d.getStatus()).containsKey(d.getTransactionType())) {
                temp.get(d.getStatus()).put(d.getTransactionType(), new HashMap<>());
            }
            if (temp.get(d.getStatus()).get(d.getTransactionType()).containsKey(d.getReversalTypeCode())) {
                summary = temp.get(d.getStatus()).get(d.getTransactionType()).get(d.getReversalTypeCode());
            } else {
                summary = new SDFFileSummaryDetailVO();
                summary.setId(rnd.nextLong());
            }

            summary.setTransactionType(d.getTransactionType());
            summary.setStatus(d.getStatus());
            summary.setReversalTypeCode(d.getReversalTypeCode());

            summary.setCommissionAmount(summary.getCommissionAmount().add(d.getCommissionAmount()));
            summary.setConsumerFeeAmount(summary.getConsumerFeeAmount().add(d.getConsumerFeeAmount()));
            summary.setCount(summary.getCount() + 1);
            summary.setTaxCommissionAmount(summary.getTaxCommissionAmount().add(d.getTotalTaxCommissionAmount()));
            summary.setTaxTransactionAmount(summary.getTaxTransactionAmount().add(d.getTotalTaxTransactionAmount()));
            summary.setTransactionAmount(summary.getTransactionAmount().add(d.getTransactionAmount()));

            BigDecimal netAmount = summary.getTransactionAmount();
            netAmount = netAmount.add(summary.getConsumerFeeAmount());
            netAmount = netAmount.add(summary.getCommissionAmount());

            summary.setNetAmount(netAmount);

            temp.get(d.getStatus()).get(d.getTransactionType()).put(d.getReversalTypeCode(), summary);
        });

        return temp.entrySet().stream()
                .flatMap(oe -> oe.getValue().entrySet().stream().flatMap(me -> me.getValue().entrySet().stream().map(s -> s.getValue())))
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = CRUDServiceException.class)
    public void assembleFile(Long fileId) throws CRUDServiceException {
        try {
            SDFFile file = this.fileDAO.findOne(fileId);

            file.getDetails().stream()
                    .forEach((SDFDetail detail) -> {
                        BHNTransaction transaction = this.bhnTransactionCRUDService.retrieveByMatch(detail);

                        if (Objects.nonNull(transaction)) {
                            detail.setStatus(SDF_DETAIL_STATUS.FOUND);
                            this.detailDAO.setBhnTransactionAndStatusById(transaction.getId(), SDF_DETAIL_STATUS.FOUND, detail.getId());
                        } else {
                            detail.setStatus(SDF_DETAIL_STATUS.NOT_FOUND);
                            this.detailDAO.setBhnTransactionAndStatusById(null, SDF_DETAIL_STATUS.NOT_FOUND, detail.getId());
                        }
                    });
            
            if (file.getDetails().stream().allMatch(d -> Objects.equals(d.getStatus(), SDF_DETAIL_STATUS.FOUND))) {
                this.fileDAO.setStatusById(SDF_FILE_STATUS.DONE, fileId);
            } else {
                this.fileDAO.setStatusById(SDF_FILE_STATUS.ERROR, fileId);
            }
        } catch (Exception e) {
            String msg = this.helperConverterService.getMessage("activation.anerroroccurred_");
            ServiceResponse result = new ServiceResponse().setMessage("%s [%s]", msg, Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
            e.printStackTrace();
            throw new CRUDServiceException(result);
        }
    }

    @Transactional(rollbackFor = CRUDServiceException.class)
    public Map<String, Object> assembleFile(Long fileId, Pageable pageable) {
        try {
            SDFFile file = this.fileDAO.findOne(fileId);

            file.getDetails().stream()
                    .filter(d -> Objects.isNull(d.getBhnTransaction()))
                    .forEach((SDFDetail detail) -> {
                        BHNTransaction transaction = this.bhnTransactionCRUDService.retrieveByMatch(detail);

                        if (Objects.nonNull(transaction)) {
                            this.detailDAO.setBhnTransactionAndStatusById(transaction.getId(), SDF_DETAIL_STATUS.FOUND, detail.getId());
                        }
                    });
        } catch (Exception e) {
            String msg = this.helperConverterService.getMessage("activation.anerroroccurred_");
            ServiceResponse result = new ServiceResponse().setMessage("%s [%s]", msg, Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
            e.printStackTrace();
            throw new CRUDServiceException(result);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("page", this.retrieveDetails(fileId, pageable, "/admin/sdfvalidation/assemble/" + fileId));
        result.put("sdfFile", this.retrieveFile(fileId));
        return result;
    }

    public PageWrapper<SDFDetailVO> retrieveDetails(Long fileId, Pageable pageable, String url) {
        Page<SDFDetail> page = this.detailDAO.findByFileIdOrderByPosTransactionDateAscPosTransactionTimeAsc(fileId, pageable);
        return new PageWrapper<>(page.map(this.detailConverterService::convert), url);
    }

    public SDFFileVO retrieveFile(Long id) {
        SDFFile file = fileDAO.findOne(id);
        return this.fileConverterService.convert(file);
    }
}
