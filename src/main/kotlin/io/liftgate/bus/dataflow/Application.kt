package io.liftgate.bus.dataflow

import io.liftgate.bus.dataflow.models.vehicle.MockVehicleMetadataProvider
import io.liftgate.bus.dataflow.models.vehicle.VehicleMetadataProvider
import io.liftgate.bus.dataflow.models.vehicle.transloc.TransLocVehicleMetadataProvider
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.liftgate.bus.dataflow.plugins.configureMonitoring
import io.liftgate.bus.dataflow.plugins.configureRouting
import io.liftgate.bus.dataflow.plugins.configureSerialization
import kotlinx.coroutines.runBlocking
import java.time.Instant
import kotlin.concurrent.thread

lateinit var vehicleMetadataProvider: VehicleMetadataProvider

fun main(args: Array<String>)
{
    io.ktor.server.cio.EngineMain.main(args)
}

fun Application.module()
{
    val transLocConfig = busConfig().config("trans-loc")
    val transLocAPIKey = transLocConfig.tryGetString("api-key")

    vehicleMetadataProvider = if (transLocAPIKey != null && transLocAPIKey != "__NONE__")
    {
        log.info("Using TransLoc vehicle metadata provider with API key \"$transLocAPIKey\".")
        TransLocVehicleMetadataProvider(
            apiKey = transLocAPIKey,
            apiHost = transLocConfig.tryGetString("api-host")
                ?: throw IllegalStateException(
                    "API host was not specified in the TransLoc"
                ),
            agencies = transLocConfig.tryGetString("agency")
                ?: throw IllegalStateException(
                    "Bus agency was not specified in the bus config"
                )
        )
    } else
    {
        log.info("Using mock vehicle metadata provider.")
        MockVehicleMetadataProvider()
    }

    val invalidationTime = busConfig()
        .tryGetString("cache-invalidation")
        ?.toLongOrNull() ?: 16_000L

    thread {
        while (true)
        {
            runBlocking {
                runCatching {
                    vehicleMetadataProvider.repopulateVehicleCache()
                    log.info("Repopulated vehicles at ${Instant.now()}")
                }.onFailure {
                    log.error("Failed to repopulate vehicles, trying again in ${invalidationTime}ms.", it)
                }
            }

            Thread.sleep(invalidationTime)
        }
    }

    configureMonitoring()
    configureSerialization()
    configureRouting()

    Runtime.getRuntime().addShutdownHook(Thread(vehicleMetadataProvider::dispose))
}

fun Application.busConfig() = environment.config.config("bus")
