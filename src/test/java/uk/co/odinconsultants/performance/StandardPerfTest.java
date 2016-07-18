package uk.co.odinconsultants.performance;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class StandardPerfTest {
    public static void run(Class clazz) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(clazz.getSimpleName())
                .forks(1)
                .measurementBatchSize(3)
                .measurementIterations(3)
                .warmupIterations(3)
                .build();

        new Runner(opt).run();
    }
}
