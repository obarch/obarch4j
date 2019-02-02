package io.obarch.codec.usage.struct.adhoc_rename;

import io.obarch.codec.spi.QJsonProperty;

public class UserPost {
    @QJsonProperty("TITLE")
    public String title;
    public String content;
}
