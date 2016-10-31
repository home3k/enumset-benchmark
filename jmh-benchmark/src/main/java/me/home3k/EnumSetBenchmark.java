/*
 * Copyright (c) 2005, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package me.home3k;

import me.home3k.enumset.benchmark.BenchMark;
import me.home3k.enumset.benchmark.LargeEnum;
import me.home3k.enumset.benchmark.TinyEnum;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;

@BenchmarkMode(Mode.All)
public class EnumSetBenchmark {

    final char[] items = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    final Random random = new Random();

    EnumSet enumSet = EnumSet.allOf(TinyEnum.class);
    EnumSet enumSet1 = EnumSet.allOf(LargeEnum.class);
    Set<Enum<?>> genericSet = new HashSet<Enum<?>>(Arrays.asList(TinyEnum.values()));
    Set<Enum<?>> genericSet1 = new HashSet<Enum<?>>(Arrays.asList(LargeEnum.values()));

    Enum<?> random(int type) {
        String prefix = items[random.nextInt(26)] + "";
        if (type == BenchMark.TINY) return TinyEnum.valueOf(prefix);
        else return LargeEnum.valueOf(prefix + random.nextInt(4));
    }


    @Benchmark
    public void testTinyEnumSetAdd() {
        enumSet.add(random(BenchMark.TINY));
    }
    @Benchmark
    public void testTinyGenericSetAdd() {
        genericSet.add(random(BenchMark.TINY));
    }

    @Benchmark
    public void testLargeEnumSetAdd() {
        enumSet.add(random(BenchMark.LARGE));
    }
    @Benchmark
    public void testLargeGenericSetAdd() {
        genericSet.add(random(BenchMark.LARGE));
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(EnumSetBenchmark.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(10)
                .threads(10)
                .forks(1)
                .build();
        new Runner(opt).run();
    }

}
