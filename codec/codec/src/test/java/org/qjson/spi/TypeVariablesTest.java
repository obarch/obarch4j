package org.qjson.spi;

import org.junit.Assert;
import org.junit.Test;
import org.qjson.TypeLiteral;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeVariablesTest {

    private static class BaseClass<T> {
        public T field1;
    }

    private interface BaseInf<T> {

    }

    private static class DerivedClass extends BaseClass<String> implements BaseInf<String> {

    }

    @Test
    public void one_layer() {
        HashMap<TypeVariable, Type> typeArgs = new HashMap<>();
        TypeVariables.collect(DerivedClass.class, typeArgs);
        TypeVariable typeParam = BaseClass.class.getTypeParameters()[0];
        Assert.assertEquals(String.class, typeArgs.get(typeParam));
        typeParam = BaseInf.class.getTypeParameters()[0];
        Assert.assertEquals(String.class, typeArgs.get(typeParam));
    }

    private static class GenericDerivedClass<T> extends BaseClass<T> {
    }

    private static class FinalClass extends GenericDerivedClass<String> {
    }

    @Test
    public void two_layers() {
        HashMap<TypeVariable, Type> typeArgs = new HashMap<>();
        TypeVariables.collect(FinalClass.class, typeArgs);
        TypeVariable typeParam = BaseClass.class.getTypeParameters()[0];
        Assert.assertEquals(String.class, typeArgs.get(typeParam));
    }



    public static class MyClass1<E extends Date> {
    }

    @Test
    public void use_lower_bound_if_not_specified() {
        TypeVariable param = MyClass1.class.getTypeParameters()[0];
        Type sub = TypeVariables.substitute(param, Collections.emptyMap());
        Assert.assertEquals(Date.class, sub);
    }

    public static class MyClass2<E> {
    }

    @Test
    public void no_bound_use_object() {
        TypeVariable param = MyClass2.class.getTypeParameters()[0];
        Type sub = TypeVariables.substitute(param, Collections.emptyMap());
        Assert.assertEquals(Object.class, sub);
    }

    @Test
    public void wildcard_use_object() {
        TypeLiteral typeLiteral = new TypeLiteral<MyClass2<?>>(){};
        Map<TypeVariable, Type> typeArgs = new HashMap<>();
        TypeVariables.collect(typeLiteral.$(), typeArgs);
        TypeVariable param = MyClass2.class.getTypeParameters()[0];
        Type sub = TypeVariables.substitute(param, typeArgs);
        Assert.assertEquals(Object.class, sub);
    }

    @Test
    public void wildcard_extends() {
        TypeLiteral typeLiteral = new TypeLiteral<MyClass2<? extends Date>>(){};
        Map<TypeVariable, Type> typeArgs = new HashMap<>();
        TypeVariables.collect(typeLiteral.$(), typeArgs);
        TypeVariable param = MyClass2.class.getTypeParameters()[0];
        Type sub = TypeVariables.substitute(param, typeArgs);
        Assert.assertEquals(Date.class, sub);
    }

    @Test
    public void wildcard_super() {
        TypeLiteral typeLiteral = new TypeLiteral<MyClass2<? super Date>>(){};
        Map<TypeVariable, Type> typeArgs = new HashMap<>();
        TypeVariables.collect(typeLiteral.$(), typeArgs);
        TypeVariable param = MyClass2.class.getTypeParameters()[0];
        Type sub = TypeVariables.substitute(param, typeArgs);
        Assert.assertEquals(Date.class, sub);
    }
}
