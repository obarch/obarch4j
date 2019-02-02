package org.qjson.decode;

public class QJsonDecodeException extends RuntimeException {

    public QJsonDecodeException() {
    }

    public QJsonDecodeException(String message) {
        super(message);
    }

    public QJsonDecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public QJsonDecodeException(Throwable cause) {
        super(cause);
    }

    public QJsonDecodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
