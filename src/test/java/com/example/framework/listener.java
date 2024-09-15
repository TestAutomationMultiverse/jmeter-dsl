package com.example.framework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static us.abstracta.jmeter.javadsl.prometheus.DslPrometheusListener.prometheusListener;
import static us.abstracta.jmeter.javadsl.prometheus.DslPrometheusListener.PrometheusMetric.responseTime;
import static us.abstracta.jmeter.javadsl.prometheus.DslPrometheusListener.PrometheusMetric.successRatio;

import java.time.Duration;

import us.abstracta.jmeter.javadsl.prometheus.DslPrometheusListener;

public class listener {
    private static final Logger LOG = LogManager.getLogger(listener.class);

    public static DslPrometheusListener setupPrometheus() {
        LOG.info("Setting up Prometheus listener");
        DslPrometheusListener PROMETHEUS_LISTENER = prometheusListener()
                .metrics(
                        responseTime("ResponseTime")
                                .quantile(0.75, 0.5)
                                .quantile(0.95, 0.1)
                                .quantile(0.99, 0.01)
                                .maxAge(Duration.ofMinutes(1)),
                        successRatio("Ratio"))
                .port(9270)
                .host("127.0.0.1")
                .endWait(Duration.ofSeconds(10));

        return PROMETHEUS_LISTENER;
    }
}

