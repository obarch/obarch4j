package org.qjson.usage.struct.adhoc_rename;

import org.qjson.spi.QJsonProperty;

public class UserPost {
    @QJsonProperty("TITLE")
    public String title;
    public String content;
}
