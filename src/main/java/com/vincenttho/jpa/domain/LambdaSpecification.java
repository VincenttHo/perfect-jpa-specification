package com.vincenttho.jpa.domain;

import com.vincenttho.jpa.enums.ConnectionType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * <p>LambdaSpecification</p>
 *
 * @author VincentHo
 * @date 2024-08-01
 */
public class LambdaSpecification<T> implements Specification<T> {

    private final List<BiFunction<Root, CriteriaBuilder, Predicate>> predicateFunctions;
    private final Class<T> poClass;
    private final List<LambdaSpecification> orSpecifications;
    private ConnectionType connectionType;

    private LambdaSpecification(Class<T> poClass) {
        this.predicateFunctions = new ArrayList<>();
        this.poClass = poClass;
        this.connectionType = ConnectionType.AND;
        this.orSpecifications = new ArrayList<>();
    }

    public static <T> LambdaPredicateBuilder<T> query(Class<T> poClazz) {
        return new LambdaPredicateBuilder(poClazz, new LambdaSpecification(poClazz));
    }

    /**
     * <p>增加条件构造Function</p>
     * 用于保存每个链式构造方法的条件构造Function
     * @author VincentHo
     * @date 2024/8/9
     * @param predicateFunction
     */
    public void addPredicateFunction(BiFunction<Root, CriteriaBuilder, Predicate> predicateFunction) {
        this.predicateFunctions.add(predicateFunction);
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
     * @param specification
     * @return com.vincenttho.jpa.domain.LambdaSpecification<T>
     */
    public LambdaSpecification<T> andOr(LambdaSpecification specification) {
        specification.connectionType = ConnectionType.OR;
        this.orSpecifications.add(specification);
        return this;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        // 前面构造好的predicateFunctions执行并获得Predicate对象
        List<Predicate> predicates = predicateFunctions.stream()
                .map(function -> function.apply(root, criteriaBuilder))
                .collect(Collectors.toList());

        List<Predicate> orPredicates = this.orSpecifications.stream().map(orSpecification -> orSpecification.toPredicate(root, query, criteriaBuilder)).collect(Collectors.toList());

        Predicate[] predicateArr = predicates.toArray(new Predicate[predicates.size()]);
        Predicate predicate = ConnectionType.AND.equals(connectionType) ? criteriaBuilder.and(predicateArr) : criteriaBuilder.or(predicateArr);
        predicate.getExpressions().addAll(orPredicates);
        return predicate;
    }

}
