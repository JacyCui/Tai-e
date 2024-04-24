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

import pascal.taie.analysis.graph.icfg.CallEdge;
import pascal.taie.analysis.graph.icfg.CallToReturnEdge;
import pascal.taie.analysis.graph.icfg.NormalEdge;
import pascal.taie.analysis.graph.icfg.ReturnEdge;
import pascal.taie.analysis.ifds.flowfunc.FlowFunction;
import pascal.taie.analysis.ifds.flowfunc.FlowFunctions;
import pascal.taie.config.AnalysisConfig;
import pascal.taie.ir.IR;
import pascal.taie.ir.exp.InvokeExp;
import pascal.taie.ir.exp.NullLiteral;
import pascal.taie.ir.exp.Var;
import pascal.taie.ir.stmt.Invoke;
import pascal.taie.ir.stmt.Stmt;
import pascal.taie.language.classes.JMethod;
import pascal.taie.language.type.NullType;
import pascal.taie.util.collection.Maps;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of possible uninitialized variables analysis as an IFDS problem.
 */
public class IFDSUninitializedVariables extends AbstractIFDSProblem<Stmt, Var, JMethod> {

    public static final String ID = "ifds-uninit-var";

    public IFDSUninitializedVariables(AnalysisConfig config) {
        super(config);
    }

    @Override
    public boolean isForward() {
        return true;
    }

    @Override
    protected Var createZeroItem() {
        return new Var(null, "<<zero>>", NullType.NULL, -1, NullLiteral.get());
    }

    @Override
    protected FlowFunction<Var> getNormalFlowFunction(NormalEdge<Stmt> edge) {
        Stmt source = edge.source();
        JMethod m = icfg.getContainingMethodOf(source);
        if (icfg.entryMethods().toList().contains(m) && icfg.getEntryOf(m).equals(source)) {
            return item -> {
                // e.g. (S_main, 0) => 0 -> {0, d1, d2, ...}
                if (item == zeroItem) {
                    IR ir = m.getIR();
                    return ir.getVars().stream()
                            // entry parameters like `args` are initialized by default
                            .filter(v -> !ir.isParam(v))
                            .collect(Collectors.toUnmodifiableSet());
                }
                // e.g. (S_main, d) => d -> {}
                return Collections.emptySet();
            };
        }
        return source.getDef()
                .filter(lv -> lv instanceof Var)
                .map(v -> (FlowFunction<Var>) (item -> {
                    // e.g. (a = a - g, g) => g -> {a, g}
                    if (source.getUses().contains(item)) {
                        return Set.of(item, (Var) v);
                    }
                    // e.g. (a = b + c, a) => a -> {}
                    if (item.equals(v)) {
                        return Collections.emptySet();
                    }
                    // e.g. (a = b + c, x) => x -> {x}
                    return Collections.singleton(item);
                }))
                .orElse(FlowFunctions.identity());
    }

    @Override
    protected FlowFunction<Var> getCallToReturnFlowFunction(CallToReturnEdge<Stmt> edge) {
        return edge.source().getDef()
                .filter(lv -> lv instanceof Var)
                // e.g. (a = foo(), a) => a -> {}
                .map(v -> FlowFunctions.kill((Var) v))
                .orElse(FlowFunctions.identity());
    }

    @Override
    protected FlowFunction<Var> getCallFlowFunction(CallEdge<Stmt> edge) {
        InvokeExp invokeExp = ((Invoke) edge.source()).getInvokeExp();
        Map<Var, Var> renameMap = Maps.newHybridMap();
        IR ir = edge.getCallee().getIR();
        for (int i = 0; i < invokeExp.getArgCount(); i++) {
            renameMap.put(invokeExp.getArg(i), ir.getParam(i));
        }
        return item -> {
            if (item == zeroItem) {
                return ir.getVars().stream()
                        .filter(v -> !ir.isParam(v))
                        .collect(Collectors.toUnmodifiableSet());
            }
            if (renameMap.containsKey(item)) {
                return Collections.singleton(renameMap.get(item));
            }
            return Collections.emptySet();
        };
    }

    @Override
    protected FlowFunction<Var> getReturnFlowFunction(ReturnEdge<Stmt> edge) {
        if (edge.getReturnVars().isEmpty()) {
            return FlowFunctions.empty();
        }
        return edge.getCallSite().getDef()
                .filter(lv -> lv instanceof Var)
                .map(v -> (FlowFunction<Var>) (item -> {
                    if (edge.getReturnVars().contains(item)) {
                        return Collections.singleton((Var) v);
                    }
                    return Collections.emptySet();
                }))
                .orElse(FlowFunctions.empty());
    }

}
