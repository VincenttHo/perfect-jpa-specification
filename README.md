# perfect-jpa-specification

## Overview

This project provides a fluent API for building dynamic JPA `Specification` queries using lambda expressions. It allows developers to create complex SQL queries in a more readable and maintainable way.

## Getting Started

### Maven Dependency

Add the following Maven dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.vincenttho</groupId>
    <artifactId>perfect-jpa-specification</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Basic Usage

Here's how you can use `LambdaSpecification` and `LambdaPredicateBuilder` to build JPA queries:

```java
LambdaSpecification<DemoEntity> spec = LambdaSpecification.query(DemoEntity.class)
    .eq(DemoEntity::getId, 1)
    .like(DemoEntity::getName, "John%")
    .in(DemoEntity::getStatus, "ACTIVE", "INACTIVE")
    .build();
    
List<DemoEntity> results = demoRepository.findAll(spec);
```

This will generate a query similar to:

```sql
SELECT * FROM demo_entity 
WHERE id = 1 
AND name LIKE 'John%' 
AND status IN ('ACTIVE', 'INACTIVE')
```

## Methods

### `eq` - Equals Condition

- **Description**: Adds an equality condition (`=`) to the query.

- Signature:

  ```java
  public LambdaPredicateBuilder<T> eq(SerializableFunction<T, Object> columnNameGetter, Object value);
  public LambdaPredicateBuilder<T> eq(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, Object value);
  ```

- Example:

  ```java
  .eq(DemoEntity::getId, 1)
  ```

### `notEq` - Not Equals Condition

- **Description**: Adds a not-equal condition (`!=`) to the query.

- Signature:

  ```java
  public LambdaPredicateBuilder<T> notEq(SerializableFunction<T, Object> columnNameGetter, Object value);
  public LambdaPredicateBuilder<T> notEq(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, Object value);
  ```

- Example:

  ```java
  .notEq(DemoEntity::getId, 2)
  ```

### `in` - In Condition

- **Description**: Adds an `IN` condition to the query.

- Signature:

  ```java
  public LambdaPredicateBuilder<T> in(SerializableFunction<T, Object> columnNameGetter, Object... values);
  public LambdaPredicateBuilder<T> in(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, Object... values);
  public LambdaPredicateBuilder<T> in(SerializableFunction<T, Object> columnNameGetter, List<Object> values);
  public LambdaPredicateBuilder<T> in(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, List<Object> values);
  ```

- Example:

  ```java
  .in(DemoEntity::getStatus, "ACTIVE", "INACTIVE")
  ```

### `notIn` - Not In Condition

- **Description**: Adds a `NOT IN` condition to the query.

- Signature:

  ```java
  public LambdaPredicateBuilder<T> notIn(SerializableFunction<T, Object> columnNameGetter, Object... values);
  public LambdaPredicateBuilder<T> notIn(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, Object... values);
  public LambdaPredicateBuilder<T> notIn(SerializableFunction<T, Object> columnNameGetter, List<Object> values);
  public LambdaPredicateBuilder<T> notIn(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, List<Object> values);
  ```

- Example:

  ```java
  .notIn(DemoEntity::getStatus, "DELETED")
  ```

### `isNull` - Is Null Condition

- **Description**: Adds an `IS NULL` condition to the query.

- Signature:

  ```java
  public LambdaPredicateBuilder<T> isNull(SerializableFunction<T, Object> columnNameGetter);
  ```

- Example:

  ```java
  .isNull(DemoEntity::getDeletedAt)
  ```

### `isNotNull` - Is Not Null Condition

- **Description**: Adds an `IS NOT NULL` condition to the query.

- Signature:

  ```java
  public LambdaPredicateBuilder<T> isNotNull(SerializableFunction<T, Object> columnNameGetter);
  ```

- Example:

  ```java
  .isNotNull(DemoEntity::getUpdatedAt)
  ```

### `like` - Like Condition

- **Description**: Adds a `LIKE` condition to the query.

- Signature:

  ```java
  public LambdaPredicateBuilder<T> like(SerializableFunction<T, Object> columnNameGetter, String value);
  public LambdaPredicateBuilder<T> like(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, String value);
  ```

- Example:

  ```java
  .like(DemoEntity::getName, "John%")
  ```

### `notLike` - Not Like Condition

- **Description**: Adds a `NOT LIKE` condition to the query.

- Signature:

  ```java
  public LambdaPredicateBuilder<T> notLike(SerializableFunction<T, Object> columnNameGetter, String value);
  public LambdaPredicateBuilder<T> notLike(Boolean ignoreEmpty, SerializableFunction<T, Object> columnNameGetter, String value);
  ```

- Example:

  ```java
  .notLike(DemoEntity::getName, "Admin%")
  ```

### `andOr` - OR Conditions Group

- **Description**: Creates a group of conditions that are combined using `OR`.

- Signature:

  ```java
  public LambdaPredicateBuilder<T> andOr(LambdaSpecification anotherSpecification);
  ```

- Example

  ```java
  .eq(DemoEntity::getId, 1)
  .andOr(LambdaSpecification.query(DemoEntity.class)
      .eq(DemoEntity::getName, "Vincent")
      .eq(DemoEntity::getAge, 25)
  )
  ```

This will generate a query like:

```sql
SELECT * FROM demo_entity 
WHERE id = 1 
AND (name = 'Vincent' OR age = 25)
```

### `build`

- **Description**: Builds the final `LambdaSpecification`.

- Signature:

  ```java
  public LambdaSpecification<T> build();
  ```

- Example:

  ```java
  LambdaSpecification<DemoEntity> spec = LambdaSpecification.query(DemoEntity.class)
      .eq(DemoEntity::getId, 1)
      .build();
  ```

## Demo

```java
LambdaSpecification<DemoEntity> spec = LambdaSpecification.query(DemoEntity.class)
    .eq(DemoEntity::getId, 1)
    .like(DemoEntity::getName, "John%")
    .in(DemoEntity::getStatus, "ACTIVE", "INACTIVE")
    .andOr(LambdaSpecification.query(DemoEntity.class)
        .eq(DemoEntity::getCountry, "USA")
        .eq(DemoEntity::getAge, 30)
    )
    .build();

List<DemoEntity> results = demoRepository.findAll(spec);
```

## Conclusion

`LambdaSpecification` and `LambdaPredicateBuilder` make it easier to build dynamic queries in a type-safe and readable manner. This approach eliminates the need to write complex query logic manually and reduces the risk of errors.

------

You can include this README.md in your project repository to help other developers understand and utilize the `LambdaSpecification` and `LambdaPredicateBuilder` classes effectively.