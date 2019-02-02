package io.obarch.codec.datetime;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.ValueObjectEncoder;

import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface DateTimeEncoders {

    static Map<Class, Encoder> $() {
        return new HashMap<Class, Encoder>() {{
            put(Date.class, new ValueObjectEncoder(
                    (sink, val) -> sink.encodeLong(((Date) val).getTime())));
            put(LocalDate.class, new ValueObjectEncoder(new LocalDateEncoder()));
            put(LocalDateTime.class, new ValueObjectEncoder(new LocalDateTimeEncoder()));
            put(LocalTime.class, new ValueObjectEncoder(new LocalTimeEncoder()));
            put(MonthDay.class, new ValueObjectEncoder(new MonthDayEncoder()));
            put(OffsetDateTime.class, new ValueObjectEncoder(new OffsetDateTimeEncoder()));
            put(OffsetTime.class, new ValueObjectEncoder(new OffsetTimeEncoder()));
            put(YearMonth.class, new ValueObjectEncoder(new YearMonthEncoder()));
            put(Year.class, new ValueObjectEncoder((sink, val) -> sink.encodeInt(((Year) val).getValue())));
            put(ZonedDateTime.class, new ValueObjectEncoder(new ZonedDateTimeEncoder()));
            put(Instant.class, new ValueObjectEncoder(new InstantEncoder()));
            put(Duration.class, new ValueObjectEncoder(new DurationEncoder()));
        }};
    }
}
