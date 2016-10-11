package me.home3k.enumset.benchmark;

import java.util.Set;

/**
 * @author home3k
 */
public class AddCallable extends AbstractCallable {

    public AddCallable(int level, int type) {
        super(level, type);
    }

    @Override
    void _call(Set<Enum<?>> enumSet) {
        enumSet.add(random());
    }

    @Override
    String feature() {
        return " add ";
    }
}
