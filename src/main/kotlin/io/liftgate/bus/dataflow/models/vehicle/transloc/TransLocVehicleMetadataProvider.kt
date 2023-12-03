package io.liftgate.bus.dataflow.models.vehicle.transloc

import io.liftgate.bus.dataflow.models.vehicle.Vehicle
import io.liftgate.bus.dataflow.models.vehicle.VehicleMetadataProvider
import io.liftgate.bus.dataflow.modules.json
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

/**
 * @author GrowlyX
 * @since 11/27/2023
 */
class TransLocVehicleMetadataProvider(
    private val apiKey: String,
    private val apiHost: String,
    private val agencies: String
) : VehicleMetadataProvider
{
    private val httpClient by lazy {
        HttpClient(CIO) {
            engine {
                pipelining = true
                requestTimeout = 5_000
            }

            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    private var cacheMutBlock = Any()
    private val localVehicleCache = mutableMapOf<String, Vehicle>()

    override fun getCachedVehicles() = synchronized(cacheMutBlock) { localVehicleCache }

    override suspend fun repopulateVehicleCache()
    {
        val getVehicles = httpClient.get("https://$apiHost/vehicles.json?agencies=$agencies&callback=call") {
            header("X-RapidAPI-Key", apiKey)
            header("X-RapidAPI-Host", apiHost)
        }
        val response = getVehicles.body<TransLocGetVehiclesResponse>()

        synchronized(cacheMutBlock) {
            localVehicleCache.clear()
            localVehicleCache += response.data.values
                .flatten()
                .associateBy { it.vehicleId }
        }
    }

    override fun dispose()
    {
        httpClient.close()
    }

}
