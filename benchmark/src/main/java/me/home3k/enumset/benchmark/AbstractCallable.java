package me.home3k.enumset.benchmark;

import java.util.Random;
import java.util.Set;

/**
 * @author home3k
 */
public abstract class AbstractCallable implements Callable {

    final char[] items = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    final int level;
    final int type;

    final Random random = new Random();

    public AbstractCallable(int level, int type) {
        this.level = level;
        this.type = type;
    }

    @Override
    public void call(Set<Enum<?>> enumSet, Set<Enum<?>> genericSet) {
        String feature = feature();
        String enumType = (type ==BenchMark.TINY? "26-elements":"130-elements");
        long start = System.currentTimeMillis();
        for (int i = 0; i < level; i++) {
            _call(enumSet);
        }
        System.out.printf("Enum Set \t %s \t loop %d \t [%s]:\t\t %d\n", enumType, level, feature, (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < level; i++) {
            _call(genericSet);
        }
        System.out.printf("Gene Set \t %s \t loop %d \t [%s]:\t\t %d\n", enumType, level, feature, (System.currentTimeMillis() - start));

        System.out.println("");
    }

    abstract void _call(Set<Enum<?>> enumSet);

    abstract String feature();

    Enum<?> random() {
        String prefix = items[random.nextInt(26)] + "";
        if(type == BenchMark.TINY) return TinyEnum.valueOf(prefix);
        else return LargeEnum.valueOf(prefix + random.nextInt(4));
    }

}
