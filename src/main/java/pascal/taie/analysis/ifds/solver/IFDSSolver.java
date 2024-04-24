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

package pascal.taie.analysis.ifds.solver;

import pascal.taie.analysis.ifds.analysis.IFDSProblem;

/**
 * Interface of IFDS problem ({@link IFDSProblem}) solver.
 *
 * @param <Node> type of ICFG nodes, typically {@link pascal.taie.ir.stmt.Stmt}
 * @param <Item> type of items in a data fact which is a set in IFDS
 * @param <Method> type of ICFG methods, typically {@link pascal.taie.language.classes.JMethod}
 */
public interface IFDSSolver<Node, Item, Method> {

    /**
     * The default solver.
     */
    @SuppressWarnings("rawtypes")
    IFDSSolver SOLVER = new DefaultIFDSSolver<>();

    /**
     * Static factory method for obtaining a solver.
     */
    @SuppressWarnings("unchecked")
    static <Node, Item, Method> IFDSSolver<Node, Item, Method> getSolver() {
        return (IFDSSolver<Node, Item, Method>) SOLVER;
    }

    /**
     * Solves the given IFDS problem.
     *
     * @param problem an IFDS problem
     * @return the IFDS solving result
     */
    IFDSResult<Node, Item> solve(IFDSProblem<Node, Item, Method> problem);

}
