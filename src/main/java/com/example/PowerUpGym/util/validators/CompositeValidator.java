package com.example.PowerUpGym.util.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CompositeValidator<P, R> implements Validator<P, List<R>> {

    private final List<Validator<P, List<R>>> validators = new ArrayList<>();


    public static boolean hasValue(String str) {
        return str != null && !str.isEmpty();
    }

    public CompositeValidator<P, R> addValidator(Predicate<P> predicate, R violation) {
        validators.add(new PredicateValidator<>(predicate, violation));
        return this;
    }

    @Override
    public List<R> validate(P input) {
        return validators.stream()
                .map(v -> v.validate(input))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}