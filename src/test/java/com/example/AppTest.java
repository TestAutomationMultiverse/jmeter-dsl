package com.example;

import static org.assertj.core.api.Assertions.assertThat;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;

public class AppTest {

    @Test
    public void testPerformance() throws IOException {
        TestPlanStats stats = testPlan(
                threadGroup(2, 10,
                        httpSampler("http://127.0.0.1:8000/api/products")),
                htmlReporter("target/jmeter-html-report"))
            .run();
        assertThat(stats.overall().sampleTimePercentile99()).isLessThan(Duration.ofSeconds(5));
    }

}