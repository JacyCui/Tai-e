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
     * Makes a flow function that kills a single item.
     *
     * @param killed the item to be killed
     * @return the flow function that kills {@code killed} and preserves other items
     */
    public static <Item> FlowFunction<Item> kill(Item killed) {
        return item -> item.equals(killed) ?
                Collections.emptySet() : Collections.singleton(item);
    }

    /**
     * @return the flow function that kills all items in a dataflow fact.
     */
    public static <Item> FlowFunction<Item> killAll() {
        return item -> Collections.emptySet();
    }

    /**
     * Makes a flow function by composing several other flow functions.
     *
     * @param funcs the flow functions to be composed
     * @return the composed flow function that applies {@code funcs} in order,
     * identity if {@code funcs} is empty
     */
    @SuppressWarnings("unchecked")
    public static <Item> FlowFunction<Item> compose(FlowFunction<Item>... funcs) {
        List<FlowFunction<Item>> list = Arrays.stream(funcs)
                .filter(f -> f != identity()).toList();
        if (list.isEmpty()) {
            return identity();
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            return item -> {
                Set<Item> result = Sets.newSet();
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

}
