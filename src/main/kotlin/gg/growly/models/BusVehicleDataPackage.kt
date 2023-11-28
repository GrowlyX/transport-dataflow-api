package gg.growly.models

import kotlinx.serialization.Serializable

/**
 * @author GrowlyX
 * @since 11/27/2023
 */
@Serializable
data class BusVehicleDataPackage(
    val busId: String,
    val humansDetected: Int
)
