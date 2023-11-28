package gg.growly.models.vehicle

/**
 * @author GrowlyX
 * @since 11/27/2023
 */
interface VehicleMetadataProvider
{
    suspend fun repopulateVehicleCache()
    fun getVehicles(): List<Vehicle>

    fun dispose()
    {

    }
}
