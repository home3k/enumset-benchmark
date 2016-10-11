package me.home3k.enumset.benchmark;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * @author home3k
 */
public class BenchMark {

    static final int TINY = 1;

    static final int LARGE = 2;

    static final int LOOP_TINY = 1000000;

    static final int LOOP_NORMAL = 10000000;

    static final int LOOP_LARGE = 80000000;

    private BenchMark() {
    }

    public static BenchMark newInstance() {
        return new BenchMark();
    }

    public static void main(String[] args) {
        BenchMark.newInstance().benchmark();
    }

    void benchmark() {
        EnumSet enumSet = EnumSet.allOf(TinyEnum.class);
        EnumSet enumSet1 = EnumSet.allOf(LargeEnum.class);
        Set<Enum<?>> genericSet = new HashSet<Enum<?>>(Arrays.asList(TinyEnum.values()));
        Set<Enum<?>> genericSet1 = new HashSet<Enum<?>>(Arrays.asList(LargeEnum.values()));
        contains(enumSet, genericSet, enumSet1, genericSet1);
        add(enumSet, genericSet, enumSet1, genericSet1);
        remove(enumSet, genericSet, enumSet1, genericSet1);
    }

    void contains(Set<Enum<?>> enumSet, Set<Enum<?>> genericSet, Set<Enum<?>> enumSet1, Set<Enum<?>> genericSet1) {
        Callable contains1 = new ContainsCallable(LOOP_TINY, TINY);
        Callable contains2 = new ContainsCallable(LOOP_NORMAL, TINY);
        Callable contains3 = new ContainsCallable(LOOP_LARGE, TINY);

        contains1.call(enumSet, genericSet);
        contains2.call(enumSet, genericSet);
        contains3.call(enumSet, genericSet);

        contains1 = new ContainsCallable(LOOP_TINY, LARGE);
        contains2 = new ContainsCallable(LOOP_NORMAL, LARGE);
        contains3 = new ContainsCallable(LOOP_LARGE, LARGE);

        contains1.call(enumSet1, genericSet1);
        contains2.call(enumSet1, genericSet1);
        contains3.call(enumSet1, genericSet1);

    }

    void add(Set<Enum<?>> enumSet, Set<Enum<?>> genericSet, Set<Enum<?>> enumSet1, Set<Enum<?>> genericSet1) {
        Callable add1 = new AddCallable(LOOP_TINY, TINY);
        Callable add2 = new AddCallable(LOOP_NORMAL, TINY);
        Callable add3 = new AddCallable(LOOP_LARGE, TINY);

        add1.call(enumSet, genericSet);
        add2.call(enumSet, genericSet);
        add3.call(enumSet, genericSet);

        add1 = new AddCallable(LOOP_TINY, LARGE);
        add2 = new AddCallable(LOOP_NORMAL, LARGE);
        add3 = new AddCallable(LOOP_LARGE, LARGE);

        add1.call(enumSet1, genericSet1);
        add2.call(enumSet1, genericSet1);
        add3.call(enumSet1, genericSet1);
    }


    void remove(Set<Enum<?>> enumSet, Set<Enum<?>> genericSet, Set<Enum<?>> enumSet1, Set<Enum<?>> genericSet1) {
        Callable remove1 = new RemoveCallable(LOOP_TINY, TINY);
        Callable remove2 = new RemoveCallable(LOOP_NORMAL, TINY);
        Callable remove3 = new RemoveCallable(LOOP_LARGE, TINY);

        remove1.call(enumSet, genericSet);
        remove2.call(enumSet, genericSet);
        remove3.call(enumSet, genericSet);

        remove1 = new RemoveCallable(LOOP_TINY, LARGE);
        remove2 = new RemoveCallable(LOOP_NORMAL, LARGE);
        remove3 = new RemoveCallable(LOOP_LARGE, LARGE);

        remove1.call(enumSet1, genericSet1);
        remove2.call(enumSet1, genericSet1);
        remove3.call(enumSet1, genericSet1);
    }


}
