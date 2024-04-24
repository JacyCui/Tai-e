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

import pascal.taie.util.collection.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Factory class for some typical flow functions.
 */
public final class FlowFunctions {

    /**
     * @return the identity flow function that maps each item to itself
     */
    public static <Item> FlowFunction<Item> identity() {
        return Collections::singleton;
    }

    /**
     * @return the empty flow function that kills all items in a dataflow fact.
     */
    public static <Item> FlowFunction<Item> empty() {
        return item -> Collections.emptySet();
    }

    /**
     * Makes a flow function that kills a single item.
     *
     * @param killItem the item to be killed
     * @return the flow function that kills {@code killItem} and preserves other items
     */
    public static <Item> FlowFunction<Item> kill(Item killItem) {
        return item -> item.equals(killItem) ?
                Collections.emptySet() : Collections.singleton(item);
    }

    /**
     * Makes a flow function that generates a single item.
     *
     * @param genItem the item to be generated
     * @param zeroItem the special zero item in the representative relation
     * @return the flow function that generates {@code genItem} and preserves other items
     */
    public static <Item> FlowFunction<Item> gen(Item genItem, Item zeroItem) {
        return item -> item.equals(zeroItem) ?
                twoElementSet(item, genItem) : Collections.singleton(item);
    }

    /**
     * Makes a flow function that transfers dataflow.
     *
     * @param fromItem the dataflow source item
     * @param toItem the item whose driver will be changed to {@code fromItem}
     * @return the flow function that drives {@code toItem} using {@code fromItem}
     * instead of itself
     */
    public static <Item> FlowFunction<Item> transfer(Item fromItem, Item toItem) {
        return item -> {
          if (item.equals(fromItem)) {
              return twoElementSet(item, toItem);
          }
          if (item.equals(toItem)) {
              return Collections.emptySet();
          }
          return Collections.singleton(item);
        };
    }

    /**
     * Makes a new flow function representing the union of several other flow functions.
     *
     * @param funcs the flow functions to union
     * @return the union flow function of {@code funcs}
     */
    @SafeVarargs
    public static <Item> FlowFunction<Item> union(FlowFunction<Item>... funcs) {
        if (funcs.length == 0) {
            return empty();
        }
        if (funcs.length == 1) {
            return funcs[0];
        }
        return item -> Arrays.stream(funcs)
                .flatMap(f -> f.getTargets(item).stream())
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Makes a new flow function by composing several other flow functions.
     *
     * @param funcs the flow functions to be composed
     * @return the composed flow function that applies {@code funcs} in order,
     * identity if {@code funcs} is empty
     */
    @SafeVarargs
    public static <Item> FlowFunction<Item> compose(FlowFunction<Item>... funcs) {
        List<FlowFunction<Item>> list = Arrays.stream(funcs)
                .filter(f -> f != identity()).toList();
        if (list.isEmpty()) {
            return identity();
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            return item -> {
                Set<Item> result = Sets.newHybridSet();
                result.add(item);
                for (FlowFunction<Item> f : list) {
                    result = result.stream()
                            .flatMap(i -> f.getTargets(i).stream())
                            .collect(Collectors.toSet());
                }
                return Collections.unmodifiableSet(result);
            };
        }
    }

    /**
     * @return an unmodifiable set containing {@code e1} and {@code e2}
     * where {@code e1 == e2} is allowed
     */
    static <Item> Set<Item> twoElementSet(Item e1, Item e2) {
        Set<Item> res = Sets.newSmallSet();
        res.add(e1);
        res.add(e2);
        return Collections.unmodifiableSet(res);
    }

}
