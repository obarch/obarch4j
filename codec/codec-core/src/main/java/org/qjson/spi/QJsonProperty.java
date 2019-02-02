package org.qjson.spi;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.function.Predicate;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface QJsonProperty {

    class Ignore implements QJsonProperty {

        @Override
        public String value() {
            return "";
        }

        @Override
        public boolean ignore() {
            return true;
        }

        @Override
        public Class<? extends Predicate> shouldEncode() {
            return null;
        }

        @Override
        public Class<? extends Encoder> encoder() {
            return null;
        }

        @Override
        public Class<? extends Decoder> decoder() {
            return null;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return QJsonProperty.class;
        }
    }

    String value() default "";

    boolean ignore() default false;

    // shouldEncode can be used to omit empty value when encoding
    Class<? extends Predicate> shouldEncode() default Predicate.class;

    Class<? extends Encoder> encoder() default Encoder.class;

    Class<? extends Decoder> decoder() default Decoder.class;
}
