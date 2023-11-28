package gg.growly.models.vehicle

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author GrowlyX
 * @since 11/27/2023
 */
@Serializable
data class Geolocation(
    @SerialName("lat") val latitude: Float,
    @SerialName("lng") val longitude: Float
)
