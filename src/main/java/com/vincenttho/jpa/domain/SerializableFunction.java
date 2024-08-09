package com.vincenttho.jpa.domain;

import java.io.Serializable;
import java.util.function.Function;

/**
 * <p>能序列化的Function</p>
 *
 * @author VincentHo
 * @date 2024-08-01
 */
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {
}
