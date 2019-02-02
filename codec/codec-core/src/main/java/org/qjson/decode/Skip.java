package org.qjson.decode;

interface Skip {
    static void $(BytesDecoderSource source) {
        switch (source.peek()) {
            case '[':
                skipList(source);
                return;
            case '{':
                skipMap(source);
                return;
            case 't':
                source.expect('t', 'r', 'u', 'e');
                return;
            case 'f':
                source.expect('f', 'a', 'l', 's', 'e');
                return;
            case 'n':
                source.expect('n', 'u', 'l', 'l');
                return;
            case '"':
                source.offset = findDoubleQuote(source, source.offset + 1) + 1;
                return;
            default:
                throw source.reportError("unexpected token");
        }
    }

    static void $(StringDecoderSource source) {
        switch (source.peek()) {
            case '[':
                skipList(source);
                return;
            case '{':
                skipMap(source);
                return;
            case 't':
                source.expect('t', 'r', 'u', 'e');
                return;
            case 'f':
                source.expect('f', 'a', 'l', 's', 'e');
                return;
            case 'n':
                source.expect('n', 'u', 'l', 'l');
                return;
            case '"':
                source.offset = findDoubleQuote(source, source.offset + 1) + 1;
                return;
            default:
                throw source.reportError("unexpected token");
        }
    }

    static void skipList(BytesDecoderSource source) {
        int count = 1;
        for (int i = source.offset + 1; i < source.size; i++) {
            switch (source.buf[i]) {
                case '"':
                    i = findDoubleQuote(source, i + 1);
                    break;
                case '[':
                    count++;
                    break;
                case ']':
                    count--;
                    if (count == 0) {
                        source.offset = i + 1;
                        return;
                    }
                    break;
            }
        }
        throw source.reportError("expect ]");
    }

    static void skipList(StringDecoderSource source) {
        int count = 1;
        for (int i = source.offset + 1; i < source.buf.length(); i++) {
            switch (source.buf.charAt(i)) {
                case '"':
                    i = findDoubleQuote(source, i + 1);
                    break;
                case '[':
                    count++;
                    break;
                case ']':
                    count--;
                    if (count == 0) {
                        source.offset = i + 1;
                        return;
                    }
                    break;
            }
        }
        throw source.reportError("expect ]");
    }

    static void skipMap(BytesDecoderSource source) {
        int count = 1;
        for (int i = source.offset + 1; i < source.size; i++) {
            switch (source.buf[i]) {
                case '"':
                    i = findDoubleQuote(source, i + 1);
                    break;
                case '{':
                    count++;
                    break;
                case '}':
                    count--;
                    if (count == 0) {
                        source.offset = i + 1;
                        return;
                    }
                    break;
            }
        }
        throw source.reportError("expect }");
    }

    static void skipMap(StringDecoderSource source) {
        int count = 1;
        for (int i = source.offset + 1; i < source.buf.length(); i++) {
            switch (source.buf.charAt(i)) {
                case '"':
                    i = findDoubleQuote(source, i + 1);
                    break;
                case '{':
                    count++;
                    break;
                case '}':
                    count--;
                    if (count == 0) {
                        source.offset = i + 1;
                        return;
                    }
                    break;
            }
        }
        throw source.reportError("expect }");
    }

    static int findDoubleQuote(BytesDecoderSource source, int from) {
        for (int i = from; i < source.size; i++) {
            if (source.buf[i] == '"') {
                return i;
            }
        }
        throw source.reportError("expect double quote");
    }

    static int findDoubleQuote(StringDecoderSource source, int from) {
        for (int i = from; i < source.buf.length(); i++) {
            if (source.buf.charAt(i) == '"') {
                return i;
            }
        }
        throw source.reportError("expect double quote");
    }
}
