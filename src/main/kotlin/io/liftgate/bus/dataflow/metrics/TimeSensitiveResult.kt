package io.liftgate.bus.dataflow.metrics

import io.liftgate.bus.dataflow.models.database.TransportationEvent
import io.liftgate.bus.dataflow.modules.collection
import org.bson.conversions.Bson
import org.litote.kmongo.*
import java.time.Duration

/**
 * @author GrowlyX
 * @since 11/30/2023
 */
class TimeSensitiveResult(
    private val threshold: Duration,
    private vararg val aggregation: Bson
)
{
    fun compute(): Number
    {
        val result = collection.aggregate<NumericalValue>(
            match(
                TransportationEvent::timestamp gte System.currentTimeMillis() - threshold.toMillis(),
                TransportationEvent::timestamp lte System.currentTimeMillis()
            ),
            *aggregation,
            project(
                NumericalValue::value from NumericalValue::value
            ),
            limit(1)
        )

        return result.first()?.value ?: 0
    }
}
