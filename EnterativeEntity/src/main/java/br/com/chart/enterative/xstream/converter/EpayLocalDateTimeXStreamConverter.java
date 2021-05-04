package br.com.chart.enterative.xstream.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 *
 * @author William Leite
 */
public class EpayLocalDateTimeXStreamConverter implements SingleValueConverter {

    private static final DateTimeFormatter FORMATTER;

    static {
        FORMATTER = new DateTimeFormatterBuilder()
                .appendPattern("uuuu-MM-dd'T'HH:mm:ss")
                .appendFraction(ChronoField.NANO_OF_SECOND, 0, 7, true)
                .appendOffset("+HH:MM", "+00:00")
                .toFormatter();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean canConvert(final Class type) {
        return LocalDateTime.class == type;
    }

    @Override
    public Object fromString(final String str) {
        try {
            return LocalDateTime.parse(str, FORMATTER);
        } catch (final DateTimeParseException e) {
            final ConversionException exception = new ConversionException("ParseException: LocalDateTime", e);
            exception.add("value", str);
            throw exception;
        }
    }

    @Override
    public String toString(final Object obj) {
        if (obj == null) {
            return null;
        }

        final LocalDateTime localDateTime = (LocalDateTime) obj;
        return FORMATTER.format(localDateTime);
    }
}
