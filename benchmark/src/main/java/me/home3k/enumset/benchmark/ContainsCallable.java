package me.home3k.enumset.benchmark;

import java.util.Set;

/**
 * @author home3k
 */
public class ContainsCallable extends AbstractCallable {

    public ContainsCallable(int level, int type) {
        super(level, type);
    }

    @Override
    void _call(Set<Enum<?>> enumSet) {
        enumSet.contains(random());
    }

    @Override
    String feature() {
        return " contains ";
    }
}
