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

import pascal.taie.analysis.dataflow.fact.DataflowResult;
import pascal.taie.analysis.dataflow.fact.SetFact;
import pascal.taie.ir.stmt.Stmt;
import pascal.taie.util.collection.Maps;

import java.util.Map;

/**
 * An IFDS result is a dataflow result with set facts.
 *
 * @param <Node> type of ICFG nodes, typically {@link pascal.taie.ir.stmt.Stmt}
 * @param <Item> type of items in a data fact which is a set in IFDS
 */
public class IFDSResult<Node, Item> extends DataflowResult<Node, SetFact<Item>> {

    private final boolean isForward;

    public IFDSResult(boolean isForward,
                      Map<Node, SetFact<Item>> inFacts,
                      Map<Node, SetFact<Item>> outFacts) {
        super(inFacts, outFacts);
        this.isForward = isForward;
    }

    public IFDSResult(boolean isForward) {
        this(isForward, Maps.newLinkedHashMap(), Maps.newLinkedHashMap());
    }

    public IFDSResult() {
        this(true, Maps.newLinkedHashMap(), Maps.newLinkedHashMap());
    }

    /**
     * @return whether this is the result of a forward ifds analysis
     */
    public boolean isForward() {
        return isForward;
    }

    @Override
    public SetFact<Item> getResult(Stmt stmt) {
        return isForward ? getInFact((Node) stmt) : getOutFact((Node) stmt);
    }

}
