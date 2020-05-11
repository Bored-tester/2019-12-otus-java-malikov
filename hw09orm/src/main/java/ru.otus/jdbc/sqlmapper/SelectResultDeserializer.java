package ru.otus.jdbc.sqlmapper;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class SelectResultDeserializer {

    public static Object deserializeObjectFromSelectResult(Class classToDeserializeTo, ResultSet selectResult)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object classInstance = generateNewEmptyClassInstance(classToDeserializeTo);
        System.out.println("Empty class instance created: " + classInstance);
        Arrays.stream(classToDeserializeTo.getDeclaredFields())
                .forEach(field -> {
                    try {
                        String value = selectResult.getString(field.getName());
                        setFieldValue(field, value, classInstance);
                    } catch (SQLException e) {
                        System.out.println(String.format("Failed to get %s value from select result %s. Will leave as null.", field.getName(), selectResult));
                    }
                });
        return classInstance;
    }

    private static Object generateNewEmptyClassInstance(Class clazz)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        try {
            ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
            Constructor objDef = clazz.getSuperclass().getDeclaredConstructor();
            objDef.setAccessible(true);
            Constructor intConstr = rf.newConstructorForSerialization(clazz, objDef);
            return clazz.cast(intConstr.newInstance());
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodException(String.format("Super class of class %s must have default constructor", clazz.getName()));
        } catch (Exception e) {
            System.out.println(String.format("Failed to create new instance of class %s for deserialization from select", clazz.getName()));
            throw e;
        }
    }

    private static void setFieldValue(Field field, String valueInString, Object objectToUpdate) {
        try {
            field.setAccessible(true);
            Object castValue = castFieldValueFromString(field.getType(), valueInString);
            field.set(objectToUpdate, castValue);
        } catch (IllegalAccessException e) {
            System.out.println(String.format("Error! Trying to get field %s from object of class %s", field.getName(), objectToUpdate.getClass().getName()));
        }

    }

    private static Object castFieldValueFromString(Class fieldClass, String valueInString) {
        if (valueInString.equals("UNKNOWN") || valueInString.equals("NULL"))
            return null;

        if (fieldClass.equals(String.class))
            return valueInString;
        if (fieldClass.equals(char.class) || fieldClass.equals(Character.class))
            return valueInString.charAt(0);

        if (fieldClass.equals(byte.class) || fieldClass.equals(Byte.class))
            return Byte.parseByte(valueInString);
        if (fieldClass.equals(short.class) || fieldClass.equals(Short.class))
            return Short.parseShort(valueInString);
        if (fieldClass.equals(int.class) || fieldClass.equals(Integer.class))
            return Integer.parseInt(valueInString);
        if (fieldClass.equals(long.class) || fieldClass.equals(Long.class))
            return Long.parseLong(valueInString);
        if (fieldClass.equals(BigInteger.class))
            return new BigInteger(valueInString);
        if (fieldClass.equals(float.class) || fieldClass.equals(Float.class))
            return Float.parseFloat(valueInString);
        if (fieldClass.equals(double.class) || fieldClass.equals(Double.class))
            return Double.parseDouble(valueInString);
        if (fieldClass.equals(BigDecimal.class))
            return new BigDecimal(valueInString);

        if (fieldClass.equals(boolean.class) || fieldClass.equals(Boolean.class))
            return Boolean.parseBoolean(valueInString);

        System.out.println(String.format("Error! For now only standard field classes are supported. %s is not one of them. Will set to null.", fieldClass.getName()));
        return null;
    }
}
