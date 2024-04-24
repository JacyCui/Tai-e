/*
 * Tai-e: A Static Analysis Framework for Java
 *
 * Copyright (C) 2022 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2022 Yue Li <yueli@nju.edu.cn>
 *
 * This file is part of Tai-e.
 *
 * Tai-e is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Tai-e is distributed in the hope that it will be useful,but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Tai-e. If not, see <https://www.gnu.org/licenses/>.
 */

package pascal.taie.analysis.ifds.flowfunc;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlowFunctionTest {

    Random random = new Random(System.currentTimeMillis());

    @Test
    void testIdentity() {
        random.ints(10)
                .forEach(i -> assertEquals(Set.of(i),
                        FlowFunctions.identity().getTargets(i)));
    }

    @Test
    void testEmpty() {
        random.ints(10)
                .forEach(i -> assertEquals(Set.of(),
                        FlowFunctions.empty().getTargets(i)));
    }

    @Test
    void testKill() {
        random.ints(10, 0, 10).forEach(k -> {
            FlowFunction<Integer> killK = FlowFunctions.kill(k);
            IntStream.range(0, 10).forEach(i -> {
                if (i == k) {
                    assertEquals(Set.of(), killK.getTargets(i));
                } else {
                    assertEquals(Set.of(i), killK.getTargets(i));
                }
            });
        });
    }

    @Test
    void testGen() {
        random.ints(10, 0, 10).forEach(k -> {
            FlowFunction<Integer> genK = FlowFunctions.gen(k, -1);
            assertEquals(FlowFunctions.twoElementSet(-1, k), genK.getTargets(-1));
            random.ints(10, 0, 10).forEach(i -> {
                assertEquals(Set.of(i), genK.getTargets(i));
            });
        });
    }

    @Test
    void testTransfer() {
        random.ints(10, 0, 8).forEach(p ->
            random.ints(10, 2, 10).forEach(q -> {
                FlowFunction<Integer> transferPQ = FlowFunctions.transfer(p, q);
                IntStream.range(0, 10).forEach(i -> {
                    if (i == p) {
                        assertEquals(FlowFunctions.twoElementSet(p, q), transferPQ.getTargets(i));
                    } else if (i == q) {
                        assertEquals(Set.of(), transferPQ.getTargets(i));
                    } else {
                        assertEquals(Set.of(i), transferPQ.getTargets(i));
                    }
                });
            })
        );
    }

    @Test
    void testUnion() {
        FlowFunction<Integer> id = FlowFunctions.identity();
        FlowFunction<Integer> empty = FlowFunctions.empty();
        FlowFunction<Integer> kill2 = FlowFunctions.kill(2);
        FlowFunction<Integer> kill5 = FlowFunctions.kill(5);
        FlowFunction<Integer> gen7 = FlowFunctions.gen(7, -1);
        FlowFunction<Integer> transfer36 = FlowFunctions.transfer(3, 6);

        FlowFunction<Integer> union1 = FlowFunctions
                .union(id, empty, kill2, kill5, gen7, transfer36);
        assertEquals(Set.of(-1, 7), union1.getTargets(-1));
        assertEquals(Set.of(3, 6), union1.getTargets(3));
        assertEquals(Set.of(2), union1.getTargets(2));
        assertEquals(Set.of(4), union1.getTargets(4));
        assertEquals(Set.of(5), union1.getTargets(5));

        FlowFunction<Integer> union2 = FlowFunctions
                .union(empty, kill2);
        assertEquals(Set.of(), union2.getTargets(2));
        assertEquals(Set.of(3), union2.getTargets(3));
        assertEquals(Set.of(4), union2.getTargets(4));

        FlowFunction<Integer> union3 = FlowFunctions.union(kill2, kill5);
        IntStream.range(0, 10).forEach(i -> assertEquals(Set.of(i), union3.getTargets(i)));
    }

    @Test
    void testCompose() {
        FlowFunction<Integer> id = FlowFunctions.identity();
        FlowFunction<Integer> empty = FlowFunctions.empty();
        FlowFunction<Integer> kill3 = FlowFunctions.kill(3);
        FlowFunction<Integer> kill6 = FlowFunctions.kill(6);
        FlowFunction<Integer> gen8 = FlowFunctions.gen(8, -1);
        FlowFunction<Integer> transfer25 = FlowFunctions.transfer(2, 5);

        FlowFunction<Integer> compose1 = FlowFunctions
                .compose(id, empty, kill3, kill6, gen8, transfer25);
        IntStream.range(0, 10).forEach(i -> assertEquals(Set.of(), compose1.getTargets(i)));
        FlowFunction<Integer> compose2 = FlowFunctions
                .compose(id, kill3, kill6, id, gen8, transfer25);
        assertEquals(Set.of(), compose2.getTargets(3));
        assertEquals(Set.of(), compose2.getTargets(6));
        assertEquals(Set.of(-1, 8), compose2.getTargets(-1));
        assertEquals(Set.of(8), compose2.getTargets(8));
        assertEquals(Set.of(2, 5), compose2.getTargets(2));
        assertEquals(Set.of(), compose2.getTargets(5));

        FlowFunction<Integer> transfer53 = FlowFunctions.transfer(5, 3);
        FlowFunction<Integer> compose3 = FlowFunctions
                .compose(transfer25, transfer53);
        assertEquals(Set.of(2, 3, 5), compose3.getTargets(2));
        assertEquals(Set.of(), compose3.getTargets(5));
        assertEquals(Set.of(), compose3.getTargets(3));
        assertEquals(Set.of(1), compose3.getTargets(1));
        assertEquals(Set.of(4), compose3.getTargets(4));
        assertEquals(Set.of(-1), compose3.getTargets(-1));

        FlowFunction<Integer> gen5 = FlowFunctions.gen(5, -1);
        FlowFunction<Integer> gen3 = FlowFunctions.gen(3, -1);
        FlowFunction<Integer> compose4 = FlowFunctions
                .compose(transfer25, transfer53, gen3, gen5);
        assertEquals(Set.of(2, 3, 5), compose4.getTargets(2));
        assertEquals(Set.of(), compose4.getTargets(5));
        assertEquals(Set.of(), compose4.getTargets(3));
        assertEquals(Set.of(1), compose4.getTargets(1));
        assertEquals(Set.of(4), compose4.getTargets(4));
        assertEquals(Set.of(-1, 3, 5), compose4.getTargets(-1));
    }

}
