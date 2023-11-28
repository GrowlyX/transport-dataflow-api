package io.liftgate.bus.dataflow.models.database

import io.liftgate.bus.dataflow.models.vehicle.Geolocation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author GrowlyX
 * @since 11/27/2023
 */
@Serializable
data class TransportationEvent(
    @SerialName("_id")
    val busId: String,
    val timestamp: Long,
    val geolocation: Geolocation,
    /**
     * Allows us to expand in the future with additional data from detection
     * rather than having to change the schema entirely.
     */
    val passengerData: Map<String, String>
)
