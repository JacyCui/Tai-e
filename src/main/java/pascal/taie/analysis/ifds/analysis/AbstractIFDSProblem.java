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

import pascal.taie.World;
import pascal.taie.analysis.ProgramAnalysis;

import pascal.taie.analysis.graph.icfg.CallToReturnEdge;
import pascal.taie.analysis.graph.icfg.CallEdge;
import pascal.taie.analysis.graph.icfg.ICFG;
import pascal.taie.analysis.graph.icfg.ICFGBuilder;
import pascal.taie.analysis.graph.icfg.ICFGEdge;
import pascal.taie.analysis.graph.icfg.NormalEdge;
import pascal.taie.analysis.graph.icfg.ReturnEdge;
import pascal.taie.analysis.ifds.flowfunc.FlowFunction;
import pascal.taie.analysis.ifds.solver.IFDSResult;
import pascal.taie.analysis.ifds.solver.IFDSSolver;
import pascal.taie.config.AnalysisConfig;

/**
 * Provides common functionalities for {@link IFDSProblem} implementation.
 *
 * @param <Node> type of ICFG nodes, typically {@link pascal.taie.ir.stmt.Stmt}
 * @param <Item> type of items in a data fact, (which must is set in IFDS)
 * @param <Method> type of ICFG methods, typically {@link pascal.taie.language.classes.JMethod}
 */
public abstract class AbstractIFDSProblem<Node, Item, Method>
        extends ProgramAnalysis<IFDSResult<Node, Item>>
        implements IFDSProblem<Node, Item, Method> {

    protected final ICFG<Method, Node> icfg;
    protected final Item zeroItem;

    protected AbstractIFDSProblem(AnalysisConfig config) {
        super(config);
        zeroItem = createZeroItem();
        icfg = World.get().getResult(ICFGBuilder.ID);
    }

    @Override
    public ICFG<Method, Node> getICFG() {
        return icfg;
    }

    @Override
    public final Item getZeroItem() {
        return zeroItem;
    }

    /**
     * A factory method to create and return the ZERO item. Meant to be
     * overwritten by subclasses.
     *
     * @return a new ZERO item of this IFDS problem.
     */
    protected abstract Item createZeroItem();

    /**
     * If the concrete analysis needs to perform some initialization before
     * the solver starts, then it can overwrite this method.
     */
    protected void initialize() {

    }

    /**
     * If the concrete analysis needs to perform some finishing work after
     * the solver finishes, then it can overwrite this method.
     */
    protected void finish() {

    }

    /**
     * Dispatches {@link ICFGEdge} to specific edge flow functions
     * according to the concrete type of {@link ICFGEdge}.
     */
    @Override
    public FlowFunction<Item> getFlowFunction(ICFGEdge<Node> edge) {
        if (edge instanceof NormalEdge<?>) {
            return getNormalFlowFunction((NormalEdge<Node>) edge);
        }
        if (edge instanceof CallToReturnEdge<?>) {
            return getCallToReturnFlowFunction((CallToReturnEdge<Node>) edge);
        }
        if (edge instanceof CallEdge<?>) {
            return getCallFlowFunction((CallEdge<Node>) edge);
        }
        return getReturnFlowFunction((ReturnEdge<Node>) edge);
    }

    // ---------- transfer functions for specific ICFG edges ----------
    protected abstract FlowFunction<Item> getNormalFlowFunction(NormalEdge<Node> edge);

    protected abstract FlowFunction<Item> getCallToReturnFlowFunction(CallToReturnEdge<Node> edge);

    protected abstract FlowFunction<Item> getCallFlowFunction(CallEdge<Node> edge);

    protected abstract FlowFunction<Item> getReturnFlowFunction(ReturnEdge<Node> edge);
    // ----------------------------------------------------------------

    @Override
    public IFDSResult<Node, Item> analyze() {
        IFDSSolver<Node, Item, Method> solver = IFDSSolver.getSolver();
        initialize();
        IFDSResult<Node, Item> result = solver.solve(this);
        finish();
        return result;
    }

}
