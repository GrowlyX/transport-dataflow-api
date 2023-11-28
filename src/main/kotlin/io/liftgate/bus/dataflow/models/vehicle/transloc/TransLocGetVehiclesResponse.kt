package io.liftgate.bus.dataflow.models.vehicle.transloc

import io.liftgate.bus.dataflow.models.vehicle.Vehicle
import kotlinx.serialization.Serializable

/**
 * @author GrowlyX
 * @since 11/27/2023
 */
@Serializable
data class TransLocGetVehiclesResponse(
    val data: Map<String, List<Vehicle>>
)
