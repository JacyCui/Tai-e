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

package pascal.taie.analysis.ifds.analysis;

import pascal.taie.analysis.graph.icfg.ICFG;
import pascal.taie.analysis.graph.icfg.ICFGEdge;
import pascal.taie.analysis.ifds.flowfunc.FlowFunction;

/**
 * Template interface for defining an IFDS problem.
 *
 * @param <Node> type of ICFG nodes, typically {@link pascal.taie.ir.stmt.Stmt}
 * @param <Item> type of items in a data fact, (which must is set in IFDS)
 * @param <Method> type of ICFG methods, typically {@link pascal.taie.language.classes.JMethod}
 */
public interface IFDSProblem<Node, Item, Method> {

    /**
     * @return {@code true} if the problem is a forward analysis, otherwise {@code false}
     */
    boolean isForward();

    /**
     * @return the ICFG associated with this IFDS problem
     */
    ICFG<Method, Node> getICFG();

    /**
     * @return the <b>singleton</b> zero item in all data facts
     */
    Item getZeroItem();

    /**
     * Get the flow function corresponding to each ICFGEdge,
     * where the trivial zero item to zero item mapping is left out.
     *
     * @param edge an icfg edge from a source node to a target node
     * @return the flow function in the form of representative relation
     * describing primarily the semantics of <b>source</b> node
     */
    FlowFunction<Item> getFlowFunction(ICFGEdge<Node> edge);

}
