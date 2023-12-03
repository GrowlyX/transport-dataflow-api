package io.liftgate.bus.dataflow.metrics

import com.mongodb.client.model.Field
import io.liftgate.bus.dataflow.models.database.Records
import io.liftgate.bus.dataflow.models.database.TransportationEvent
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.MeterBinder
import org.litote.kmongo.addFields
import org.litote.kmongo.div
import org.litote.kmongo.from
import org.litote.kmongo.projection
import java.time.Duration

/**
 * @author GrowlyX
 * @since 11/30/2023
 */
class TransportationMetrics : MeterBinder
{
    override fun bindTo(registry: MeterRegistry)
    {
        val averagePersonsLast10s = TimeSensitiveResult(
            Duration.ofSeconds(10L),
            addFields(
                Field(
                    "value",
                    "avg".projection from TransportationEvent::passengerData / Records::totalPassengers
                )
            )
        )

        Gauge
            .builder("transportation.persons.count.average", averagePersonsLast10s::compute)
            .description("The average number of persons on all registered vehicles in the last 10s")
            .register(registry)
    }
}
