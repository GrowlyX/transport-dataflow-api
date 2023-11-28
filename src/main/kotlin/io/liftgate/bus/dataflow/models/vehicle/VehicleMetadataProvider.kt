package io.liftgate.bus.dataflow.models.vehicle

/**
 * @author GrowlyX
 * @since 11/27/2023
 */
interface VehicleMetadataProvider
{
    suspend fun repopulateVehicleCache()
    fun getCachedVehicles(): Map<String, Vehicle>

    fun dispose()
    {

    }
}
