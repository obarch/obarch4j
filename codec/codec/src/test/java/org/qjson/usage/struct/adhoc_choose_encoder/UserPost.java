package org.qjson.usage.struct.adhoc_choose_encoder;

import org.qjson.spi.QJsonProperty;
import org.qjson.spi.encoder.DemoDateEncoder;

import java.time.OffsetDateTime;

public class UserPost {
    public String title;
    public String content;
    @QJsonProperty(encoder = DemoDateEncoder.class)
    public OffsetDateTime publishedAt;
}
