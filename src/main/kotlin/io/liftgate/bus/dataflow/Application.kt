package io.liftgate.bus.dataflow

import io.ktor.server.application.*
import io.liftgate.bus.dataflow.models.vehicle.VehicleMetadataProvider
import io.liftgate.bus.dataflow.modules.*

lateinit var vehicleMetadataProvider: VehicleMetadataProvider

fun main(args: Array<String>)
{
    io.ktor.server.cio.EngineMain.main(args)
}

fun Application.module()
{
    configureVehicleMetadataProvider()
    configureMongoDatabase()
    configureAuthentication()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}

fun Application.busConfig() = environment.config.config("bus")
