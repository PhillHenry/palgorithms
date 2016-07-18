package uk.co.odinconsultants.graph.impl;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.runner.RunnerException;
import scala.Tuple2;
import scala.collection.immutable.Seq;

import static uk.co.odinconsultants.graph.impl.GraphGenerator.eachComponentIsARing;
import static uk.co.odinconsultants.graph.impl.GraphGenerator.makeAGraphWith;
import static uk.co.odinconsultants.graph.impl.GraphGenerator.stronglyConnectedComponents;
import static uk.co.odinconsultants.performance.StandardPerfTest.run;

public class AdjacencyListGraphPerformanceMain {

    static private Tuple2 graphTuple  = makeAGraphWith(100,stronglyConnectedComponents(), eachComponentIsARing());

    @Benchmark
    @Fork(1)
    public void bfsTopologicalOrdering() {
        AdjacencyListGraph.apply((Seq<Tuple2<Object, Object>>) graphTuple._2);
    }

    public static void main(String[] args) throws RunnerException {
        run(AdjacencyListGraphPerformanceMain.class);
    }
}
