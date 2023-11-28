package gg.growly.models.vehicle

/**
 * @author GrowlyX
 * @since 11/27/2023
 */
class MockVehicleMetadataProvider : VehicleMetadataProvider
{
    override fun getVehicles() = listOf(
        Vehicle(
            vehicleId = "hors",
            segmentId = "hors",
            routeId = "wonky",
            location = Geolocation(
                latitude = 100.0f,
                longitude = -48.493f
            )
        )
    )

    override suspend fun repopulateVehicleCache()
    {

    }
}
