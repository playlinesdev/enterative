package br.com.chart.enterative.helper;

import br.com.chart.enterative.dao.FileUploadDAO;
import br.com.chart.enterative.dao.SaleOrderLineDAO;
import br.com.chart.enterative.entity.FileUpload;
import br.com.chart.enterative.enums.FILE_TYPE;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.dao.UserDAO;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import br.com.chart.enterative.enums.REPORT_DATE_RANGE;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class EnterativeUtils {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SaleOrderLineDAO saleOrderLineDAO;

    @Autowired
    private FileUploadDAO fileUploadDAO;

    public final static String HTML_LINESEPARATOR = "<br />";
    public final static String VERSION = "v 1.6.0";
    public static String MAINTENANCE;

    private final Set<String> EXTERNALCODE_CACHE;
    private final Set<String> CONFIRMATIONTOKEN_CACHE;

    public EnterativeUtils() {
        this.EXTERNALCODE_CACHE = new HashSet<>();
        this.CONFIRMATIONTOKEN_CACHE = new HashSet<>();
    }

    public String retrieveImagem(Long id) {
        String result = null;
        List<FileUpload> files = this.fileUploadDAO.findByObjectIDAndType(id, FILE_TYPE.PRODUCT_IMAGE);
        if (!files.isEmpty()) {
            FileUpload image = files.get(0);
            result = "data:image/png;base64, " + image.getFileData();
        }
        return result;
    }

    public String formatResponse(RESPONSE_CODE code) {
        if (Objects.nonNull(code)) {
            return String.format("[%s] %s", code.getCode(), code.getDescription());
        } else {
            return "";
        }
    }

    public String generateConfirmationToken() {
        String result = null;
        boolean done = false;

        while (!done) {
            UUID uuid = UUID.randomUUID();
            result = uuid.toString().replace("-", "");
            if (CONFIRMATIONTOKEN_CACHE.contains(result)) {
                continue;
            }

            User user = new User();
            user.setToken(result);
            Example<User> example = Example.of(user);
            long count = this.userDAO.count(example);

            if (count == 0L) {
                done = true;
            }

            CONFIRMATIONTOKEN_CACHE.add(result);
        }

        return result;
    }

    public String generateExternalCode() {
        String result = null;
        boolean done = false;

        while (!done) {
            UUID uuid = UUID.randomUUID();
            // Código externo com 20 caracteres
            result = uuid.toString().replace("-", "").substring(0, 20);

            if (EXTERNALCODE_CACHE.contains(result)) {
                continue;
            }

            SaleOrderLine item = new SaleOrderLine();
            item.setExternalCode(result);
            Example<SaleOrderLine> example = Example.of(item);
            long count = this.saleOrderLineDAO.count(example);

            if (count == 0L) {
                done = true;
            }

            EXTERNALCODE_CACHE.add(result);
        }

        return result;
    }

    public Date lastMoment(Date d) {
        if (Objects.isNull(d)) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    public Date firstMoment(Date d) {
        if (Objects.isNull(d)) {
            return null;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public String retrieveMerchantTerminalID(String shopCode, String terminalCode) {
        String s = String.format("%05d", Integer.parseInt(shopCode));
        String t = String.format("%03d", Integer.parseInt(terminalCode));
        return String.format("%s%s%s%s", s, "     ", t, "   ");
    }

    public Long retrieveRandomNegative() {
        Long r = new Random().nextLong();
        return r > 0 ? r * -1 : r;
    }

    public String removeLeadingZeroes(String str) {
        return str.replaceFirst("^0+(?!$)", "");
    }

    public List<Date> getDatesBetween(Date start, Date end) {
        List<Date> result = new ArrayList<>();
        LocalDate startDate = this.toLocalDate(start);
        LocalDate endDate = this.toLocalDate(end);

        while (!startDate.isAfter(endDate)) {
            result.add(this.fromLocalDate(startDate));
            startDate = startDate.plusDays(1);
        }
        return result;
    }

    public List<LocalDate> getWeeksBetween(Date start, Date end) {
        List<LocalDate> result = new ArrayList<>();
        LocalDate startDate = this.toLocalDate(start);
        LocalDate endDate = this.toLocalDate(end);
        LocalDate lastAdded = this.toLocalDate(start);

        while (!startDate.isAfter(endDate) || lastAdded.isBefore(endDate)) {
            result.add(startDate);
            lastAdded = startDate;
            startDate = startDate.plusWeeks(1);
        }
        return result;
    }

    public List<Date> getMonthsBetween(Date start, Date end) {
        List<Date> result = new ArrayList<>();
        LocalDate startDate = this.toLocalDate(start);
        LocalDate endDate = this.toLocalDate(end);

        while (!startDate.isAfter(endDate)) {
            result.add(this.fromLocalDate(startDate));
            startDate = startDate.plusMonths(1);
        }
        return result;
    }

    public List<String> dateKeys(Date start, Date end, REPORT_DATE_RANGE range, Locale locale) {
        List<String> keys = new ArrayList<>();
        switch (range) {
            case DAILY:
                final SimpleDateFormat dailySDF = new SimpleDateFormat("dd/MM");
                keys.addAll(this.getDatesBetween(start, end).stream().map(dailySDF::format).collect(Collectors.toList()));
                break;
            case WEEKLY:
                WeekFields weekFields = WeekFields.of(locale);
                final SimpleDateFormat weeklySDF = new SimpleDateFormat("dd/MM");
                keys.addAll(this.getWeeksBetween(start, end).stream()
                        .map(d -> {
                            String week = String.valueOf(d.get(weekFields.weekOfWeekBasedYear()));
                            Date sunday = this.fromLocalDate(d.with(weekFields.dayOfWeek(), 1));
                            Date saturday = this.fromLocalDate(d.with(weekFields.dayOfWeek(), 7));
                            return String.format("%s - %s (%s)", weeklySDF.format(sunday), weeklySDF.format(saturday), week);
                        })
                        .collect(Collectors.toList()));
                break;
            case MONTHLY:
                final SimpleDateFormat monthlySDF = new SimpleDateFormat("MMM/yy");
                keys.addAll(this.getMonthsBetween(start, end).stream().map(monthlySDF::format).collect(Collectors.toList()));
                break;
            default:
                break;
        }
        return keys;
    }

    public REPORT_DATE_RANGE dateRange(Date start, Date end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return REPORT_DATE_RANGE.MONTHLY;
        }

        LocalDate startDate = this.toLocalDate(start);
        LocalDate endDate = this.toLocalDate(end);

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 12) {
            return REPORT_DATE_RANGE.DAILY;
        } else if (days <= 84) {
            return REPORT_DATE_RANGE.WEEKLY;
        } else {
            return REPORT_DATE_RANGE.MONTHLY;
        }
    }

    public String toOnlyNumbers(String str) {
        if (Objects.nonNull(str)) {
            return str.replaceAll("\\D+", "");
        } else {
            return "";
        }
    }

    public LocalDateTime toLocalDateTime(Date date) {
        if (date.getClass().equals(java.sql.Date.class)) {
            date = new Date(date.getTime());
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public LocalDate toLocalDate(Date date) {
        if (date.getClass().equals(java.sql.Date.class)) {
            date = new Date(date.getTime());
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Date fromLocalDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Date fromLocalDateTime(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date toDate(String pattern, String date) throws ParseException {
        return new SimpleDateFormat(pattern).parse(date);
    }

    public String fromDate(String pattern, Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }
    
    public String toDotlessString(BigDecimal number) {
        return number.multiply(new BigDecimal(100)).setScale(0).toPlainString();
    }

    public BigDecimal fromDotlessString(String str, int scale) {
        int dec = str.length() - scale;
        String result = str.substring(0, dec).concat(".").concat(str.substring(dec));
        return new BigDecimal(result);
    }

    public String toJSON(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        
        // TODO: Remove this configuration
        // Avoid exceptions thrown by the unknown properties in EpayCatalog
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        return mapper.writeValueAsString(obj);
    }
    
    public <T, S> T fromJSON(String json, Class<T> master, Class<S> child) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructParametricType(master, child);
        return mapper.readValue(json, type);
    }
    
    public <T> T fromJSON(String json, TypeReference<T> type) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, type);
    }

    public <T> T fromJSON(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }

    public <T> List<T> fromJSONList(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }

    /**
     *
     * @param str
     * @return
     */
    public Date toDate(String str) {
        Date result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            result = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param str
     * @return
     */
    public Date toTime(String str) {
        Date result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            result = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param str
     * @param precision
     * @param scale
     * @return
     */
    public BigDecimal toBigDecimal(String str, int precision, int scale) {
        BigDecimal result = null;
        try {
            result = new BigDecimal(str, new MathContext(precision, RoundingMode.HALF_EVEN)).setScale(scale);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param str
     * @return
     */
    public Long toLong(String str) {
        Long result = null;

        try {
            result = Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String convert(DataIntegrityViolationException ex) {
        org.hibernate.exception.ConstraintViolationException inner = (org.hibernate.exception.ConstraintViolationException) ex.getCause();
        SQLException innerInner = inner.getSQLException();
        String message;
        if (innerInner.getErrorCode() == 1062) {
            message = ENTITY_MESSAGE.UNIQUE;
        } else {
            message = inner.getLocalizedMessage();
        }
        return String.format("%s - %s", inner.getConstraintName(), message);
    }
    
    public String formatEpayDate(Date localDateTime) {
        return this.fromDate("yyyy-MM-dd HH:mm:ss", localDateTime);
    }

    public String formatEpayDate(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
