package org.qjson.encode;

public class QJsonEncodeException extends RuntimeException {

    public QJsonEncodeException() {
    }

    public QJsonEncodeException(String message) {
        super(message);
    }

    public QJsonEncodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public QJsonEncodeException(Throwable cause) {
        super(cause);
    }

    public QJsonEncodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
