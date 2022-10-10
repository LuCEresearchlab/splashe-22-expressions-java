package ch.usi.inf.luce.expr.analyzer.stats;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        StatsVisitorTest.class,
        JavaStatsAnalyzerTest.class,
        ParameterizedJavaExpressionsStatsVisitorTest.class,
        StatsTest.class,
        TreePathTest.class,
})

public class AllTests {
}
