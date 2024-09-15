package com.example;

import java.io.IOException;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.apache.http.entity.ContentType;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import com.example.framework.utilities;
import com.example.framework.listener;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;
import us.abstracta.jmeter.javadsl.prometheus.DslPrometheusListener;
import static org.assertj.core.api.Assertions.assertThat;
import static us.abstracta.jmeter.javadsl.JmeterDsl.htmlReporter;
import static us.abstracta.jmeter.javadsl.JmeterDsl.httpSampler;
import static us.abstracta.jmeter.javadsl.JmeterDsl.testPlan;
import static us.abstracta.jmeter.javadsl.JmeterDsl.threadGroup;
import static us.abstracta.jmeter.javadsl.prometheus.DslPrometheusListener.prometheusListener;


class PerformanceTest {

        public static DslPrometheusListener PROMETHEUS_LISTENER = listener.setupPrometheus();
        public static String TARGET_API = utilities.loadProperty("TARGET_API") + "/api/products";
        public static String REPORT_PATH = utilities.loadProperty("REPORT_PATH");
        public static int THREADS = 4;
        public static int ITERATIONS = 100;
        public final String BODY = "{\"name\": \"banana\", \"description\": \"minion's favorite fruit\", \"price\": 99.99}";

        @Test
        void testCreateProductPerformance() throws IOException {
                TestPlanStats stats = testPlan(
                                threadGroup(THREADS, ITERATIONS,
                                                httpSampler(TARGET_API)
                                                                .method(HTTPConstants.POST)
                                                                .contentType(ContentType.APPLICATION_JSON)
                                                                .body(BODY)),
                                htmlReporter(REPORT_PATH),
                                PROMETHEUS_LISTENER).run();
                assertThat(stats.overall().sampleTimePercentile99()).isLessThan(Duration.ofSeconds(5));
        }


        @Test
        void testGetProductPerformance() throws IOException {
                TestPlanStats stats = testPlan(
                                threadGroup(THREADS, ITERATIONS,
                                                httpSampler(TARGET_API)
                                                                .method(HTTPConstants.GET)
                                                                .contentType(ContentType.APPLICATION_JSON)
                                                                .body(BODY)),
                                htmlReporter(REPORT_PATH),
                                prometheusListener()).run();
                assertThat(stats.overall().sampleTimePercentile99()).isLessThan(Duration.ofSeconds(5));
        }

}
