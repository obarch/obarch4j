package org.qjson.format;

import org.junit.Test;
import org.qjson.TestDecode;
import org.qjson.TestEncode;

public class ReferenceTest {

    @Test
    public void ref_map_value() {
        TestEncode.$();
        TestDecode.$();
    }

    @Test
    public void ref_list_element() {
        TestEncode.$();
        TestDecode.$();
    }

    @Test
    public void ref_array_element() {
        TestEncode.$();
        TestDecode.$();
    }

    @Test
    public void ref_struct_field() {
        TestEncode.$();
        TestDecode.$();
    }

    @Test
    public void ref_map_itself() {
        TestEncode.$();
        TestDecode.$(false);
    }

    @Test
    public void ref_list_itself() {
        TestEncode.$();
        TestDecode.$(false);
    }

    @Test
    public void ref_struct_itself() {
        TestEncode.$();
        TestDecode.$(false);
    }
}
