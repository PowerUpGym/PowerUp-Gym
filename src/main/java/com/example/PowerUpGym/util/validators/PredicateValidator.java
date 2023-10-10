package com.example.PowerUpGym.util.validators;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class PredicateValidator<P, R> implements Validator<P, List<R>> {

    private final Predicate<P> inputPredicate;
    private final Function<P, R> function;

    public PredicateValidator(Predicate<P> inputPredicate, R violation) {
        this.function = p -> violation;
        this.inputPredicate = inputPredicate;
    }

    @Override
    public List<R> validate(P input) {
        return !inputPredicate.test(input) ?
                singletonList(function.apply(input)) : emptyList();
    }
}