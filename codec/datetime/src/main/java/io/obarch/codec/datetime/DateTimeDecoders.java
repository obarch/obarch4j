package io.obarch.codec.datetime;

import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.QJsonSpi;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface DateTimeDecoders {

    Map<Class, Function<QJsonSpi, Decoder>> decoderProviders = new HashMap<Class, Function<QJsonSpi, Decoder>>() {{
        put(LocalDate.class, LocalDateDecoder::new);
        put(LocalDateTime.class, LocalDateTimeDecoder::new);
        put(LocalTime.class, LocalTimeDecoder::new);
        put(MonthDay.class, MonthDayDecoder::new);
        put(OffsetDateTime.class, OffsetDateTimeDecoder::new);
        put(OffsetTime.class, OffsetTimeDecoder::new);
        put(YearMonth.class, YearMonthDecoder::new);
        put(ZonedDateTime.class, ZonedDateTimeDecoder::new);
    }};

    static Decoder create(QJsonSpi spi, Class clazz, Map<TypeVariable, Type> typeArgs) {
        Function<QJsonSpi, Decoder> provider = decoderProviders.get(clazz);
        if (provider != null) {
            return provider.apply(spi);
        }
        return null;
    }

    static Map<Type, Decoder> $() {
        return new HashMap<Type, Decoder>() {{
            put(Instant.class, new InstantDecoder());
            put(Duration.class, new DurationDecoder());
        }};
    }
}
