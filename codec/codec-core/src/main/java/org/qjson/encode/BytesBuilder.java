package org.qjson.encode;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BytesBuilder {

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    private byte[] buf;
    private int len;

    public BytesBuilder(int capacity) {
        buf = new byte[capacity];
    }

    public BytesBuilder(byte[] buf, int len) {
        this.buf = buf;
        this.len = len;
    }

    public BytesBuilder() {
        this(16);
    }

    public void append(byte b) {
        ensureCapacity(length() + 1);
        buf[len++] = b;
    }

    void appendEscape(byte b) {
        append((byte) '\\', (byte) '\\', (byte) ('A' + (b >>> 4)), (byte) ('A' + (b & 0xF)));
    }

    public void append(char b) {
        append((byte) b);
    }

    public void append(char b1, char b2) {
        append((byte) b1, (byte) b2);
    }

    public void append(byte b1, byte b2) {
        ensureCapacity(length() + 2);
        buf[len++] = b1;
        buf[len++] = b2;
    }

    public void append(char b1, char b2, char b3) {
        append((byte) b1, (byte) b2, (byte) b3);
    }

    public void append(byte b1, byte b2, byte b3) {
        ensureCapacity(length() + 3);
        buf[len++] = b1;
        buf[len++] = b2;
        buf[len++] = b3;
    }

    public void append(char b1, char b2, char b3, char b4) {
        append((byte) b1, (byte) b2, (byte) b3, (byte) b4);
    }

    public void append(byte b1, byte b2, byte b3, byte b4) {
        ensureCapacity(length() + 4);
        buf[len++] = b1;
        buf[len++] = b2;
        buf[len++] = b3;
        buf[len++] = b4;
    }

    public void append(char b1, char b2, char b3, char b4, char b5) {
        append((byte) b1, (byte) b2, (byte) b3, (byte) b4, (byte) b5);
    }

    public void append(byte b1, byte b2, byte b3, byte b4, byte b5) {
        ensureCapacity(length() + 5);
        buf[len++] = b1;
        buf[len++] = b2;
        buf[len++] = b3;
        buf[len++] = b4;
        buf[len++] = b5;
    }

    public void append(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
        ensureCapacity(length() + 7);
        buf[len++] = b1;
        buf[len++] = b2;
        buf[len++] = b3;
        buf[len++] = b4;
        buf[len++] = b5;
        buf[len++] = b6;
        buf[len++] = b7;
    }

    public void append(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        ensureCapacity(length() + 8);
        buf[len++] = b1;
        buf[len++] = b2;
        buf[len++] = b3;
        buf[len++] = b4;
        buf[len++] = b5;
        buf[len++] = b6;
        buf[len++] = b7;
        buf[len++] = b8;
    }

    public void append(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8, byte b9) {
        ensureCapacity(length() + 9);
        buf[len++] = b1;
        buf[len++] = b2;
        buf[len++] = b3;
        buf[len++] = b4;
        buf[len++] = b5;
        buf[len++] = b6;
        buf[len++] = b7;
        buf[len++] = b8;
        buf[len++] = b9;
    }

    public void append(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8, byte b9, byte b10) {
        ensureCapacity(length() + 10);
        buf[len++] = b1;
        buf[len++] = b2;
        buf[len++] = b3;
        buf[len++] = b4;
        buf[len++] = b5;
        buf[len++] = b6;
        buf[len++] = b7;
        buf[len++] = b8;
        buf[len++] = b9;
        buf[len++] = b10;
    }

    public void append(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7,
                       byte b8, byte b9, byte b10, byte b11) {
        ensureCapacity(length() + 11);
        buf[len++] = b1;
        buf[len++] = b2;
        buf[len++] = b3;
        buf[len++] = b4;
        buf[len++] = b5;
        buf[len++] = b6;
        buf[len++] = b7;
        buf[len++] = b8;
        buf[len++] = b9;
        buf[len++] = b10;
        buf[len++] = b11;
    }

    public void append(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7,
                       byte b8, byte b9, byte b10, byte b11, byte b12) {
        ensureCapacity(length() + 12);
        buf[len++] = b1;
        buf[len++] = b2;
        buf[len++] = b3;
        buf[len++] = b4;
        buf[len++] = b5;
        buf[len++] = b6;
        buf[len++] = b7;
        buf[len++] = b8;
        buf[len++] = b9;
        buf[len++] = b10;
        buf[len++] = b11;
        buf[len++] = b12;
    }

    public void append(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7,
                       byte b8, byte b9, byte b10, byte b11, byte b12, byte b13) {
        ensureCapacity(length() + 13);
        buf[len++] = b1;
        buf[len++] = b2;
        buf[len++] = b3;
        buf[len++] = b4;
        buf[len++] = b5;
        buf[len++] = b6;
        buf[len++] = b7;
        buf[len++] = b8;
        buf[len++] = b9;
        buf[len++] = b10;
        buf[len++] = b11;
        buf[len++] = b12;
        buf[len++] = b13;
    }

    public void append(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7,
                       byte b8, byte b9, byte b10, byte b11, byte b12, byte b13, byte b14) {
        ensureCapacity(length() + 14);
        buf[len++] = b1;
        buf[len++] = b2;
        buf[len++] = b3;
        buf[len++] = b4;
        buf[len++] = b5;
        buf[len++] = b6;
        buf[len++] = b7;
        buf[len++] = b8;
        buf[len++] = b9;
        buf[len++] = b10;
        buf[len++] = b11;
        buf[len++] = b12;
        buf[len++] = b13;
        buf[len++] = b14;
    }

    public byte[] buf() {
        return buf;
    }

    public void setLength(int newLen) {
        len = newLen;
    }

    public int length() {
        return len;
    }

    public void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity - buf.length > 0) {
            buf = Arrays.copyOf(buf,
                    newCapacity(minimumCapacity));
        }
    }

    private int newCapacity(int minCapacity) {
        // overflow-conscious code
        int newCapacity = (buf.length << 1) + 2;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        return (newCapacity <= 0 || MAX_ARRAY_SIZE - newCapacity < 0)
                ? hugeCapacity(minCapacity)
                : newCapacity;
    }

    private int hugeCapacity(int minCapacity) {
        if (Integer.MAX_VALUE - minCapacity < 0) { // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE)
                ? minCapacity : MAX_ARRAY_SIZE;
    }

    @Override
    public String toString() {
        return new String(buf, 0, len, StandardCharsets.UTF_8);
    }

    public byte[] copyOfBytes() {
        return Arrays.copyOf(buf, len);
    }
}
