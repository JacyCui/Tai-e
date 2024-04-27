package pascal.taie.analysis.ifds.solver;

import pascal.taie.analysis.dataflow.fact.SetFact;
import pascal.taie.analysis.graph.icfg.ICFG;
import pascal.taie.analysis.ifds.analysis.IFDSProblem;

/**
 * A dummy IFDS solver that just prints zero item's targets of in edge flow functions,
 * which is just used to test the runnability of skeleton.
 * <b>It should be removed after you fully implement the default solver.</b>
 *
 * @param <Node> type of ICFG nodes, typically {@link pascal.taie.ir.stmt.Stmt}
 * @param <Item> type of items in a data fact which is a set in IFDS
 * @param <Method> type of ICFG methods, typically {@link pascal.taie.language.classes.JMethod}
 */
public class DummyIFDSSolver<Node, Item, Method> extends AbstractIFDSSolver<Node, Item, Method> {

    @Override
    public IFDSResult<Node, Item> solve(IFDSProblem<Node, Item, Method> problem) {
        IFDSResult<Node, Item> result = new IFDSResult<>(problem.isForward());
        ICFG<Method, Node> icfg = problem.getICFG();
        icfg.getNodes().forEach(node -> {
            icfg.getInEdgesOf(node).forEach(edge -> {
                SetFact<Item> inFact = new SetFact<>(
                        problem.getFlowFunction(edge).getTargets(problem.getZeroItem()));
                inFact.add(problem.getZeroItem());
                result.setInFact(node, inFact);
            });
            icfg.getOutEdgesOf(node).forEach(edge -> {
                SetFact<Item> outFact = new SetFact<>(
                        problem.getFlowFunction(edge).getTargets(problem.getZeroItem()));
                outFact.add(problem.getZeroItem());
                result.setOutFact(node, outFact);
            });
        });
        return result;
    }

}
