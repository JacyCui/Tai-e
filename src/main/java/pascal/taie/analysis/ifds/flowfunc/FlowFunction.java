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

import java.util.Set;

/**
 * Template interface for defining a flow function in the form of
 * representative relation.
 *
 * @param <Item> type of items in a data fact which is a set in IFDS
 */
@FunctionalInterface
public interface FlowFunction<Item> {

    /**
     * Get the target set of an item in the representative relation of this
     * flow function.
     *
     * @param item the source item
     * @return the set of target items in the representative relation
     */
    Set<Item> getTargets(Item item);

}
