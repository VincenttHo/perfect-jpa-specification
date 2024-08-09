package com.vincenttho.jpa.utils;

import com.vincenttho.jpa.domain.SerializableFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * <p>字段名工具</p>
 *
 * @author VincentHo
 * @date 2024-08-09
 */
public class ColumnUtils {

    /**
     * <p>获取字段名</p>
     * @author VincentHo
     * @date 2024/8/1
     * @param columnNameGetter
     * @return java.lang.String
     */
    public static <T> String getColumnName(SerializableFunction<T, Object> columnNameGetter) {
        String methodName = getMethodName(columnNameGetter);
        if (methodName.startsWith("get")) {
            String filedName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
            return filedName;
        } else {
            throw new RuntimeException(String.format("动态查询生成失败，方法名必须为get方法，当前方法名为：%s", methodName));
        }
    }

    /**
     * <p>获取方法名</p>
     * 通过writeReplace()方法获取到SerializedLambda对象，从而获取方法名
     * @author VincentHo
     * @date 2024/8/1
     * @param columnNameGetter
     * @return java.lang.String
     */
    public static <T> String getMethodName(SerializableFunction<T, Object> columnNameGetter) {
        try {
            Method writeReplace = columnNameGetter.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            Object sl = writeReplace.invoke(columnNameGetter);
            SerializedLambda serializedLambda = (SerializedLambda)sl;
            return serializedLambda.getImplMethodName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
