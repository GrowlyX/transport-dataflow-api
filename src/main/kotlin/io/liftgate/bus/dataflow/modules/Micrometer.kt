package io.liftgate.bus.dataflow.modules

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.liftgate.bus.dataflow.metrics.TransportationMetrics
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry

/**
 * @author GrowlyX
 * @since 11/30/2023
 */
val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
fun Application.configureMicrometerMetrics()
{
    install(MicrometerMetrics) {
        registry = appMicrometerRegistry
        meterBinders = listOf(
            TransportationMetrics()
        )
    }
}
