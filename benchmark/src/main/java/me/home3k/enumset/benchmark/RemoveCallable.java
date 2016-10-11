package me.home3k.enumset.benchmark;

import java.util.Set;

/**
 * @author home3k
 */
public class RemoveCallable extends AbstractCallable {

    public RemoveCallable(int level, int type) {
        super(level, type);
    }

    @Override
    void _call(Set<Enum<?>> enumSet) {
        enumSet.remove(random());
    }

    @Override
    String feature() {
        return " remove ";
    }
}
