package br.com.chart.enterative.service.report;

import br.com.chart.enterative.service.activationprocess.EpayActivationProcess;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.vo.epay.EpayReport;
import br.com.chart.enterative.vo.search.EpaySaleReportSearchVO;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class EpayReportService extends UserAwareComponent {
    
    @Autowired
    private EpayActivationProcess activationProcess;
    
    public Map<String, Object> assembleReportVariables(EpaySaleReportSearchVO vo) throws ParseException {
        Map<String, Object> result = new HashMap<>(0);
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(vo.getStartDate());
        cal.add(Calendar.DAY_OF_MONTH, 20);
        
        EpayReport report = this.activationProcess.retrieveSalesReport(vo.getStartDate(), cal.getTime());
        result.put("report", report);
        
        return result;
    }
}
