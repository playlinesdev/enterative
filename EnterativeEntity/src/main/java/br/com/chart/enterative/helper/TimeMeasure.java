package br.com.chart.enterative.helper;

import java.io.Serializable;
import java.util.Date;

/**
 * @author William Leite
 */
public class TimeMeasure implements Serializable {

    private static final long serialVersionUID = 1L;

    private long start = 0L;

    public TimeMeasure start() {
        this.start = new Date().getTime();
        return this;
    }

    public String end(String message) {
        long end = new Date().getTime();
        long diff = end - this.start;
        return String.format("[%s] %s msec (%s s)", message, diff, diff / 1000);
    }
}
