/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.excelmapper.util;

import com.mycompany.smartadvertizer.excelmapper.convert.Aliased;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
public class Util {

    public static <E> Set<E> asSet(E... e) {
        Set<E> set = new HashSet<>(e.length);
        set.addAll(Arrays.asList(e));
        return set;
    }

    public static <E> LinkedHashSet<E> asOrderedSet(E... e) {
        LinkedHashSet<E> set = new LinkedHashSet<>(e.length);
        set.addAll(Arrays.asList(e));
        return set;
    }

    public static <K, V> List<V> getValues(Map<K, V> map, K... keys) {
        return Arrays.asList(keys).stream().map(key
                -> Objects.requireNonNull(map.get(key),
                        "map has no value for key: " + key))
                .collect(Collectors.toList());
    }

    public static <K, E> Map<K, E> toMap(Collection<E> entities,
            Function<? super E, ? extends K> keyMapper) {
        return entities.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    public static <T extends Enum<T> & Aliased> T getEnum(Class<T> enumType,
            String alias) {
        EnumSet<T> enumSet = EnumSet.allOf(enumType);
        return enumSet.stream().filter((Aliased e) -> alias.equals(e.getAlias()))
                .findAny().orElseThrow(() -> new IllegalArgumentException(
                                "no operators with sign: " + alias));
    }
}
