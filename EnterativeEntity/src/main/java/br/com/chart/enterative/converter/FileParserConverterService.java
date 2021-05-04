package br.com.chart.enterative.converter;

import br.com.chart.enterative.entity.SDFDetail;
import br.com.chart.enterative.entity.SDFHeader;
import br.com.chart.enterative.entity.SDFTrailer;
import br.com.chart.enterative.enums.SDF_DETAIL_STATUS;
import br.com.chart.enterative.helper.EnterativeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class FileParserConverterService {

    @Autowired
    private EnterativeUtils utils;

    /**
     *
     * @param optional
     * @param isCSV
     * @return
     * @throws IllegalArgumentException
     */
    public SDFHeader toSDFHeader(Optional<String> optional, boolean isCSV) throws IllegalArgumentException {
        SDFHeader header = new SDFHeader();

        if (!optional.isPresent()) {
            return header;
        }

        String str = optional.get();

        if (isCSV) {
            String[] fields = str.split(",");
            int i = 0;

            // RecordId - 0
            String recordId = fields[i++];
            // FileId - 1
            String fileId = fields[i++];
            // FileTransmissionDate - 2
            header.setFileTransmissionDate(this.utils.toDate(fields[i++]));
            // FileReportingDate - 3
            header.setFileReportingDate(this.utils.toDate(fields[i++]));
            // PartnerId - 4
            header.setPartnerId(fields[i++]);
            // PartnerName - 5
            header.setPartnerName(fields[i++]);

            if (fields.length == 7) {
                // Filler - 6
                header.setFiller(fields[i++]);
            }
        } else {
            throw new IllegalArgumentException("Apenas arquivos CSV foram implementados!");
        }

        return header;
    }

    /**
     *
     * @param strList
     * @param isCSV
     * @return
     * @throws IllegalArgumentException
     */
    public List<SDFDetail> toSDFDetails(List<String> strList, boolean isCSV) throws IllegalArgumentException {
        final List<SDFDetail> details = new ArrayList<>();

        if (isCSV) {
            strList.stream().forEach((String s) -> {
                String[] fields = s.split(",");
                int i = 0;

                SDFDetail detail = new SDFDetail();

                detail.setStatus(SDF_DETAIL_STATUS.NOT_FOUND);

                // RecordId - 0
                String recordId = fields[i++];
                // MerchantId - 1
                detail.setMerchantId(fields[i++]);
                // MerchantName - 2
                detail.setMerchantName(fields[i++]);
                // StoreId - 3
                detail.setStoreId(fields[i++]);
                // TerminalId - 4
                detail.setTerminalId(fields[i++]);
                // ClerkId - 5
                detail.setClerkId(fields[i++]);
                // CardIssuerId - 6
                detail.setCardIssuerId(fields[i++]);
                // CardIssuerProcessorId - 7
                detail.setCardIssuerProcessorId(fields[i++]);
                // AcquirerId - 8
                detail.setAcquirerId(fields[i++]);
                // ArquiredTransactionDate - 9
                detail.setAcquiredTransactionDate(this.utils.toDate(fields[i++]));
                // AcquiredTransactionTime - 10
                detail.setAcquiredTransactionTime(this.utils.toTime(fields[i++]));
                // GiftCardNumber - 11
                detail.setGiftCardNumber(fields[i++]);
                // ProductId - 12
                detail.setProductId(fields[i++]);
                // POSTransactionDate - 13
                detail.setPosTransactionDate(this.utils.toDate(fields[i++]));
                // POSTransactionTime - 14
                detail.setPosTransactionTime(this.utils.toTime(fields[i++]));
                // TransactionType - 15
                detail.setTransactionType(fields[i++]);
                // SystemTraceAuditNumber - 16
                detail.setSystemTraceAuditNumber(fields[i++]);
                // ProductItemPrice - 17
                detail.setProductItemPrice(this.utils.toBigDecimal(fields[i++], 17, 4));
                // CurrencyCode - 18
                detail.setCurrencyCode(fields[i++]);
                // MerchantTransactionId / ReferenceNumber - 19
                detail.setMerchantTransactionId(fields[i++]);
                // BHNTransactionId - 20
                detail.setBhnTransactionId(fields[i++]);
                // AuthResponseCode - 21
                detail.setAuthResponseCode(fields[i++]);
                // ApprovalCode - 22
                detail.setApprovalCode(fields[i++]);
                // Reversal / SAFTypeCode - 23
                detail.setReversalTypeCode(fields[i++]);
                // TransactionAmount - 24
                detail.setTransactionAmount(this.utils.toBigDecimal(fields[i++], 17, 4));
                // ConsumerFeeAmount - 25
                detail.setConsumerFeeAmount(this.utils.toBigDecimal(fields[i++], 11, 4));
                // CommissionAmount - 26
                detail.setCommissionAmount(this.utils.toBigDecimal(fields[i++], 11, 4));
                // TotalTaxOnTransactionAmount - 27
                detail.setTotalTaxTransactionAmount(this.utils.toBigDecimal(fields[i++], 11, 4));
                // TotalTaxOnCommissionAmount - 28
                detail.setTotalTaxCommissionAmount(this.utils.toBigDecimal(fields[i++], 11, 4));
                // NetAmount - 29
                detail.setNetAmount(this.utils.toBigDecimal(fields[i++], 11, 4));

                details.add(detail);
            });
        } else {
            throw new IllegalArgumentException("Apenas arquivos CSV foram implementados!");
        }

        return details;
    }

    /**
     *
     * @param optional
     * @param isCSV
     * @return
     * @throws IllegalArgumentException
     */
    public SDFTrailer toSDFTrailer(Optional<String> optional, boolean isCSV) throws IllegalArgumentException {
        SDFTrailer trailer = new SDFTrailer();

        if (!optional.isPresent()) {
            return trailer;
        }

        String str = optional.get();

        if (isCSV) {
            String[] fields = str.split(",");
            int i = 0;

            // RecordId - 0
            String recordId = fields[i++];
            // FileId - 1
            String fileId = fields[i++];
            // FileTransmissionDate - 2
            trailer.setFileTransmissionDate(this.utils.toDate(fields[i++]));
            // TotalActivationCount - 3
            trailer.setTotalActivationCount(this.utils.toLong(fields[i++]));
            // TotalRedemptionCount - 4
            trailer.setTotalRedemptionCount(this.utils.toLong(fields[i++]));
            // TotalReloadCount - 5
            trailer.setTotalReloadCount(this.utils.toLong(fields[i++]));
            // TotalRefundActivationCount - 6
            trailer.setTotalRefundActivationCount(this.utils.toLong(fields[i++]));
            // TotalReturnCount - 7
            trailer.setTotalReturnCount(this.utils.toLong(fields[i++]));
            // TotalReversalCount - 8
            trailer.setTotalReversalCount(this.utils.toLong(fields[i++]));
            // TotalTransactionCount - 9
            trailer.setTotalTransactionCount(this.utils.toLong(fields[i++]));
            // TotalActivationCount - 10
            trailer.setTotalActivationAmount(this.utils.toBigDecimal(fields[i++], 17, 4));
            // TotalRedemptionAmount - 11
            trailer.setTotalRedemptionAmount(this.utils.toBigDecimal(fields[i++], 17, 4));
            // TotalReloadAmount - 12
            trailer.setTotalReloadAmount(this.utils.toBigDecimal(fields[i++], 17, 4));
            // TotalRefundActivationAmount - 13
            trailer.setTotalRefundActivationAmount(this.utils.toBigDecimal(fields[i++], 17, 4));
            // TotalReturnAmount - 14
            trailer.setTotalReturnAmount(this.utils.toBigDecimal(fields[i++], 17, 4));
            // TotalReversalAmount - 15
            trailer.setTotalReversalAmount(this.utils.toBigDecimal(fields[i++], 17, 4));
            // TotalConsumerFeeAmount - 16
            trailer.setTotalConsumerFeeAmount(this.utils.toBigDecimal(fields[i++], 15, 4));
            // TotalCommissionAmount - 17
            trailer.setTotalCommissionAmount(this.utils.toBigDecimal(fields[i++], 15, 4));
            // TotalTaxOnTransactionAmount - 18
            trailer.setTotalTaxTransactionAmount(this.utils.toBigDecimal(fields[i++], 15, 4));
            // TotalTaxOnCommissionAmount - 19
            trailer.setTotalTaxCommissionAmount(this.utils.toBigDecimal(fields[i++], 17, 4));
            // NetTransactionAmount - 20
            trailer.setNetTransactionAmount(this.utils.toBigDecimal(fields[i++], 17, 4));

            if (fields.length == 22) {
                // Filler - 21
                trailer.setFiller(fields[i++]);
            }

        } else {
            throw new IllegalArgumentException("Apenas arquivos CSV foram implementados!");
        }

        return trailer;
    }
}
