package br.com.chart.enterative.converter;

import br.com.chart.enterative.entity.AccountTransaction;
import br.com.chart.enterative.entity.AccountTransactionDeadFile;
import br.com.chart.enterative.vo.report.ClientCommissionReportVO;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ReportConverterService {

    public ClientCommissionReportVO toClientCommissionReportVO(AccountTransaction transaction) {
        ClientCommissionReportVO line = new ClientCommissionReportVO();
        line.setCommission(transaction.getAmount());
        line.setDate(transaction.getTransactionDate());
        line.setName(transaction.getPurchaseOrderLine().getName());
        line.setOrder(transaction.getPurchaseOrderLine().getPurchaseOrder().getId().toString());
        line.setValue(transaction.getSaleOrderLine().getAmount());
        return line;
    }

    public ClientCommissionReportVO toClientCommissionReportVO(AccountTransactionDeadFile transaction) {
        ClientCommissionReportVO line = new ClientCommissionReportVO();
        line.setCommission(transaction.getAmount());
        line.setDate(transaction.getTransactionDate());
        line.setName(transaction.getPurchaseOrderLine().getName());
        line.setOrder(transaction.getPurchaseOrderLine().getPurchaseOrder().getId().toString());
        line.setValue(transaction.getSaleOrderLine().getAmount());
        return line;
    }
}
