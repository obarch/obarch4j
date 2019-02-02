package org.qjson.spi;

import org.junit.Assert;
import org.junit.Test;

import java.beans.Transient;

public class StructDescriptorTest {

    public static class MyClass {

        public transient String field1;

        @QJsonProperty
        public transient String field2;

        @Transient
        public String getField3() {
            return null;
        }

        @Transient
        @QJsonProperty
        public String getField4() {
            return null;
        }
    }

    @Test
    public void ignore_transient() {
        StructDescriptor struct = StructDescriptor.create(MyClass.class);
        StructDescriptor.Prop field1 = struct.fields.get("field1");
        Assert.assertTrue(field1.getAnnotation(QJsonProperty.class).ignore());
        StructDescriptor.Prop field2 = struct.fields.get("field2");
        Assert.assertFalse(field2.getAnnotation(QJsonProperty.class).ignore());
        StructDescriptor.Prop field3 = struct.methods.get("getField3").get(0);
        Assert.assertTrue(field3.getAnnotation(QJsonProperty.class).ignore());
        StructDescriptor.Prop field4 = struct.methods.get("getField4").get(0);
        Assert.assertFalse(field4.getAnnotation(QJsonProperty.class).ignore());
    }

    @Test
    public void copy_qjson_property_into_descriptor() {
        StructDescriptor struct = StructDescriptor.create(MyClass.class);
        StructDescriptor.Prop field1 = struct.fields.get("field1");
        Assert.assertTrue(field1.ignore);
    }
}
