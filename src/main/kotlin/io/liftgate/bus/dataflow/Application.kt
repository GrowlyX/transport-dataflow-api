package io.liftgate.bus.dataflow

import io.ktor.server.application.*
import io.liftgate.bus.dataflow.models.vehicle.VehicleMetadataProvider
import io.liftgate.bus.dataflow.modules.configureMonitoring
import io.liftgate.bus.dataflow.modules.configureRouting
import io.liftgate.bus.dataflow.modules.configureSerialization
import io.liftgate.bus.dataflow.modules.configureVehicleMetadataProvider

lateinit var vehicleMetadataProvider: VehicleMetadataProvider

fun main(args: Array<String>)
{
    io.ktor.server.cio.EngineMain.main(args)
}

fun Application.module()
{
    configureVehicleMetadataProvider()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}

fun Application.busConfig() = environment.config.config("bus")
