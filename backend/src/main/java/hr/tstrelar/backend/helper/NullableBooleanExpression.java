package hr.tstrelar.backend.helper;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.Optional;
import java.util.function.Function;


public class NullableBooleanExpression<T> {
    public BooleanExpression find(Function<T, BooleanExpression> expression, T param) {
        return Optional.ofNullable(param).map(expression).orElse(null);
    }
}
