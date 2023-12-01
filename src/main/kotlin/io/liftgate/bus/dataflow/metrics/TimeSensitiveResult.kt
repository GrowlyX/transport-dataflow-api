package io.liftgate.bus.dataflow.metrics

import com.mongodb.client.AggregateIterable
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
    private var previousTime = System.currentTimeMillis()
    fun compute(): Number
    {
        if (System.currentTimeMillis() - previousTime > threshold.toMillis())
        {
            previousTime = System.currentTimeMillis() - threshold.toMillis()
        }

        val result = collection.aggregate<NumberResult>(
            match(
                TransportationEvent::timestamp gte previousTime,
                TransportationEvent::timestamp lte System.currentTimeMillis()
            ),
            *aggregation,
            project(
                NumberResult::total from NumberResult::total
            ),
            limit(1)
        )

        previousTime = System.currentTimeMillis()

        return result.first()?.total ?: 0
    }
}
