package uk.co.odinconsultants.graph.search;


import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.runner.RunnerException;
import scala.Tuple2;
import scala.collection.immutable.Seq;
import scala.concurrent.ExecutionContext;
import scala.concurrent.ExecutionContext$;
import uk.co.odinconsultants.graph.impl.AdjacencyListGraph;

import static uk.co.odinconsultants.graph.impl.GraphGenerator.*;
import static uk.co.odinconsultants.performance.StandardPerfTest.run;

public class BFSPerformanceMain {

    static private Tuple2               graphTuple  = makeAGraphWith(100,stronglyConnectedComponents(), eachComponentIsARing());
    static private ExecutionContext     xc          = ExecutionContext$.MODULE$.global();
    static private AdjacencyListGraph   graph       = AdjacencyListGraph.apply((Seq<Tuple2<Object, Object>>) graphTuple._2);

    @Benchmark
    @Fork(1)
    public void bfsTopologicalOrderingMultiThreaded() {
        BFS.parallelPath(graph, 1L, xc);
    }

    @Benchmark
    @Fork(1)
    public void bfsTopologicalOrdering() {
        BFS.path(graph, 1L);
    }

    public static void main(String[] args) throws RunnerException {
        run(BFSPerformanceMain.class);
    }

}
