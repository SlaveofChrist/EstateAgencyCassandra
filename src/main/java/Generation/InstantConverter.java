package Generation;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class InstantConverter extends AbstractBeanField{

    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        TemporalAccessor date = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss'Z'").parse(s + "T00:00:00Z");
        System.out.println(Instant.from(date));
        return Instant.from(date);
    }
}
