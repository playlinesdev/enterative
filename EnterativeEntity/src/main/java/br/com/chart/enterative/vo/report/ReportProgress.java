package br.com.chart.enterative.vo.report;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * @author William Leite
 */
@Getter
@Setter
public class ReportProgress implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    protected List<String> messages;
    protected BigDecimal progress;
    protected BigDecimal progressMax;
    protected LocalDateTime lastUpdate;
    protected boolean isDone;

    public ReportProgress() {
        this.messages = new ArrayList<>(0);
        this.isDone = false;
    }

    public void log(String message) {
        String finalMsg = String.format("[%s] %s", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), message);
        this.messages.add(finalMsg);
    }

    public void log(String template, Object... args) {
        this.log(String.format(template, args));
    }

    @Override
    public ReportProgress clone() {
        ReportProgress c = new ReportProgress();
        c.setLastUpdate(this.getLastUpdate());
        c.setMessages(new ArrayList<>(this.messages));
        c.setProgress(this.progress);
        c.setProgressMax(this.progressMax);
        return c;
    }
}
