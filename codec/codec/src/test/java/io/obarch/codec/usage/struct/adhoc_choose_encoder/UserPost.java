package io.obarch.codec.usage.struct.adhoc_choose_encoder;

import io.obarch.codec.spi.QJsonProperty;
import io.obarch.codec.spi.encoder.DemoDateEncoder;

import java.time.OffsetDateTime;

public class UserPost {
    public String title;
    public String content;
    @QJsonProperty(encoder = DemoDateEncoder.class)
    public OffsetDateTime publishedAt;
}
