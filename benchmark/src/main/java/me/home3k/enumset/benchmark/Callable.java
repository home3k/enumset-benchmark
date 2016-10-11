package me.home3k.enumset.benchmark;

import java.util.Set;

/**
 * @author home3k
 */
public interface Callable {
    void call(Set<Enum<?>> enumSet, Set<Enum<?>> genericSet);
}
