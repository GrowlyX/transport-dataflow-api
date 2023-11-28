package io.liftgate.bus.dataflow.models

import kotlinx.serialization.Serializable

/**
 * @author GrowlyX
 * @since 11/27/2023
 */
@Serializable
data class BusDataPackage(
    val busId: String,
    val humansDetected: Int
)
