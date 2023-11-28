package gg.growly.models.vehicle

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author GrowlyX
 * @since 11/27/2023
 */
@Serializable
data class Vehicle(
    @SerialName("vehicle_id")
    val vehicleId: String,
    @SerialName("segment_id")
    val segmentId: String,
    @SerialName("route_id")
    val routeId: String,
    val location: Geolocation
)
