package com.vincenttho.jpa.domain;

import com.vincenttho.jpa.utils.ColumnUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

/**
 * <p>lambda条件构造器</p>
 *
 * @author VincentHo
 * @date 2024-08-09
 */
public class LambdaPredicateBuilder<T> {

    private final LambdaSpecification<T> lambdaSpecification;

    private final Class<T> poClass;

    public LambdaPredicateBuilder(Class<T> poClass, LambdaSpecification<T> lambdaSpecification) {
        this.poClass = poClass;
        this.lambdaSpecification = lambdaSpecification;
    }

    /**
     * <p>增加条件构造Function</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 空值忽略标志
     * @param value 值
     * @param predicateFunction 条件构造Function
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder
     */
    private LambdaPredicateBuilder<T> addPredicate(Boolean ignoreEmpty, Object value, BiFunction<Root, CriteriaBuilder, Predicate> predicateFunction) {
        // 自主选择是否忽略空值，也就是null时不加入该条件
        if(ignoreEmpty) {
            if(value == null) {
                return this;
            } else if(value instanceof Collection && ((Collection)value).size() == 0) {
                return this;
            } else if(value instanceof CharSequence && ((CharSequence)value).length() == 0) {
                return this;
            }
        }

        lambdaSpecification.addPredicateFunction(predicateFunction);

        return this;

    }

    /**
     * <p>增加条件构造Function</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param predicateFunction
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder
     */
    private LambdaPredicateBuilder<T> addPredicate(BiFunction<Root, CriteriaBuilder, Predicate> predicateFunction) {
        lambdaSpecification.addPredicateFunction(predicateFunction);
        return this;
    }

    /**
     * <p>创建一个用or连接的条件集</p>
     * 入参里面的条件都会变成用or进行连接
     * 如：LambdaSpecification.query(Po.class)
     *           .eq(Po::getId, "1")
     *           .or(LambdaSpecification.query(Po.class)
     *                 .eq(Po::getName, "Vincent")
     *                 .eq(Po::getAge, 2)
     *           )
     *  这么写的话，条件会变成 select * from 表名 where id = '1' and (name = 'Vincent' or age = 2)
     * @author VincentHo
     * @date 2024/8/9
     * @param anotherSpecification
     * @return com.vincenttho.jpa.domain.LambdaSpecification<T>
     */
    public LambdaPredicateBuilder<T> andOr(LambdaSpecification anotherSpecification) {
        this.lambdaSpecification.andOr(anotherSpecification);
        return this;
    }

    public LambdaSpecification<T> build() {
        return lambdaSpecification;
    }

    /**
     * <p>=条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> eq(SerializableFunction<T, Object> columnNameGetter, Object value) {
        return eq(false, columnNameGetter, value);
    }

    /**
     * <p>=条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 是否忽略null值
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> eq(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, Object value) {
        return addPredicate(ignoreEmpty, value,
                (root, criteriaBuilder) -> criteriaBuilder.equal(root.get(ColumnUtils.getColumnName(columnNameGetter)), value)
        );
    }

    /**
     * <p>=条件（字段之间比较）</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter1 字段名getter function
     * @param columnNameGetter2 字段名getter function
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> eq(SerializableFunction<T, Object> columnNameGetter1, SerializableFunction<T, Object> columnNameGetter2) {
        return addPredicate(
                (root, criteriaBuilder) -> criteriaBuilder.equal(root.get(ColumnUtils.getColumnName(columnNameGetter1)), root.get(ColumnUtils.getColumnName(columnNameGetter2)))
        );
    }

    /**
     * <p>!=条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> notEq(SerializableFunction<T, Object> columnNameGetter, Object value) {
        return notEq(false, columnNameGetter, value);
    }

    /**
     * <p>!=条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 是否忽略null值
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> notEq(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, Object value) {
        return addPredicate(ignoreEmpty, value,
                (root, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(ColumnUtils.getColumnName(columnNameGetter)), value)
        );
    }

    /**
     * <p>!=条件（字段之间比较）</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter1 字段名getter function
     * @param columnNameGetter2 字段名getter function
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> notEq(SerializableFunction<T, Object> columnNameGetter1, SerializableFunction<T, Object> columnNameGetter2) {
        return addPredicate(
                (root, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(ColumnUtils.getColumnName(columnNameGetter1)), root.get(ColumnUtils.getColumnName(columnNameGetter2)))
        );
    }

    /**
     * <p>in条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @param values 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> in(SerializableFunction<T, Object> columnNameGetter, Object ... values) {
        return in(false, columnNameGetter, values);
    }

    /**
     * <p>in条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 是否忽略null值
     * @param columnNameGetter 字段名getter function
     * @param values 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> in(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, Object ... values) {
        return addPredicate(ignoreEmpty, values,
                (root, criteriaBuilder) -> {
                    CriteriaBuilder.In in = criteriaBuilder.in(root.get(ColumnUtils.getColumnName(columnNameGetter)));
                    for(Object value : values) {
                        in.value(value);
                    }
                    return in;
                }
        );
    }

    /**
     * <p>in条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @param values 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> in(SerializableFunction<T, Object> columnNameGetter, List<Object> values) {
        return in(false, columnNameGetter, values);
    }

    /**
     * <p>in条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 是否忽略null值
     * @param columnNameGetter 字段名getter function
     * @param values 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> in(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, List<Object> values) {
        return addPredicate(ignoreEmpty, values,
                (root, criteriaBuilder) -> {
                    CriteriaBuilder.In in = criteriaBuilder.in(root.get(ColumnUtils.getColumnName(columnNameGetter)));
                    for(Object value : values) {
                        in.value(value);
                    }
                    return in;
                }
        );
    }

    /**
     * <p>not in条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @param values 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> notIn(SerializableFunction<T, Object> columnNameGetter, Object ... values) {
        return notIn(false, columnNameGetter, values);
    }

    /**
     * <p>not in条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 是否忽略null值
     * @param columnNameGetter 字段名getter function
     * @param values 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> notIn(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, Object ... values) {
        return addPredicate(ignoreEmpty, values,
                (root, criteriaBuilder) -> {
                    CriteriaBuilder.In in = criteriaBuilder.in(root.get(ColumnUtils.getColumnName(columnNameGetter)));
                    for(Object value : values) {
                        in.value(value);
                    }
                    return in.not();
                }
        );
    }

    /**
     * <p>not in条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @param values 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> notIn(SerializableFunction<T, Object> columnNameGetter, List<Object> values) {
        return notIn(false, columnNameGetter, values);
    }

    /**
     * <p>not in条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 是否忽略null值
     * @param columnNameGetter 字段名getter function
     * @param values 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> notIn(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, List<Object> values) {
        return addPredicate(ignoreEmpty, values,
                (root, criteriaBuilder) -> {
                    CriteriaBuilder.In in = criteriaBuilder.in(root.get(ColumnUtils.getColumnName(columnNameGetter)));
                    for(Object value : values) {
                        in.value(value);
                    }
                    return in.not();
                }
        );
    }

    /**
     * <p>is null条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> isNull(SerializableFunction<T, Object> columnNameGetter) {
        return addPredicate(
                (root, criteriaBuilder) -> criteriaBuilder.isNull(root.get(ColumnUtils.getColumnName(columnNameGetter)))
        );
    }

    /**
     * <p>is not null条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> isNotNull(SerializableFunction<T, Object> columnNameGetter) {
        return addPredicate(
                (root, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get(ColumnUtils.getColumnName(columnNameGetter)))
        );
    }

    /**
     * <p>like条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> like(SerializableFunction<T, Object> columnNameGetter, String value) {
        return like(false, columnNameGetter, value);
    }

    /**
     * <p>like条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 是否忽略null值
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> like(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, String value) {
        return addPredicate(ignoreEmpty, value,
                (root, criteriaBuilder) -> criteriaBuilder.like(root.get(ColumnUtils.getColumnName(columnNameGetter)), value)
        );
    }

    /**
     * <p>not like条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> notLike(SerializableFunction<T, Object> columnNameGetter, String value) {
        return notLike(false, columnNameGetter, value);
    }

    /**
     * <p>not like条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 是否忽略null值
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> notLike(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, String value) {
        return addPredicate(ignoreEmpty, value,
                (root, criteriaBuilder) -> criteriaBuilder.notLike(root.get(ColumnUtils.getColumnName(columnNameGetter)), value)
        );
    }

    /**
     * <p> < 条件 </p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> lt(SerializableFunction<T, Object> columnNameGetter, Comparable value) {
        return lt(false, columnNameGetter, value);
    }

    /**
     * <p> < 条件 </p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 是否忽略null值
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> lt(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, Comparable value) {
        return addPredicate(ignoreEmpty, value,
                (root, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(ColumnUtils.getColumnName(columnNameGetter)), value)
        );
    }

    /**
     * <p><=条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> le(SerializableFunction<T, Object> columnNameGetter, Comparable value) {
        return le(false, columnNameGetter, value);
    }

    /**
     * <p><=条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 是否忽略null值
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> le(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, Comparable value) {
        return addPredicate(ignoreEmpty, value,
                (root, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(ColumnUtils.getColumnName(columnNameGetter)), value)
        );
    }

    /**
     * <p> > 条件 </p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> gt(SerializableFunction<T, Object> columnNameGetter, Comparable value) {
        return gt(false, columnNameGetter, value);
    }

    /**
     * <p> > 条件 </p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 是否忽略null值
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> gt(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, Comparable value) {
        return addPredicate(ignoreEmpty, value,
                (root, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(ColumnUtils.getColumnName(columnNameGetter)), value)
        );
    }

    /**
     * <p> >=条件 </p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> ge(SerializableFunction<T, Object> columnNameGetter, Comparable value) {
        return ge(false, columnNameGetter, value);
    }

    /**
     * <p> >=条件 </p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 是否忽略null值
     * @param columnNameGetter 字段名getter function
     * @param value 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> ge(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, Comparable value) {
        return addPredicate(ignoreEmpty, value,
                (root, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(ColumnUtils.getColumnName(columnNameGetter)), value)
        );
    }

    /**
     * <p>between条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param columnNameGetter 字段名getter function
     * @param value1 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> between(SerializableFunction<T, Object> columnNameGetter, Comparable value1, Comparable value2) {
        return between(false, columnNameGetter, value1, value2);
    }

    /**
     * <p>between条件</p>
     * @author VincentHo
     * @date 2024/8/9
     * @param ignoreEmpty 是否忽略null值
     * @param columnNameGetter 字段名getter function
     * @param value1 比较值
     * @return com.vincenttho.jpa.domain.LambdaPredicateBuilder<T>
     */
    public LambdaPredicateBuilder<T> between(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, Comparable value1, Comparable value2) {
        if(ignoreEmpty && (value1 == null || value2 == null)) {
            return this;
        }
        return addPredicate(
                (root, criteriaBuilder) -> criteriaBuilder.between(root.get(ColumnUtils.getColumnName(columnNameGetter)), value1, value2)
        );
    }

}
