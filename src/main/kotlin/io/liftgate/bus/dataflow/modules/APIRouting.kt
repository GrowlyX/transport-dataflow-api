package io.liftgate.bus.dataflow.modules

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.liftgate.bus.dataflow.models.BusDataPackage
import io.liftgate.bus.dataflow.models.database.TransportationEvent
import io.liftgate.bus.dataflow.vehicleMetadataProvider

fun Application.configureRouting()
{
    routing {
        authenticate("vehicle-id-apikey") {
            route("/api/v1/") {
                post("submit-data-package") {
                    val dataPackage = context.receive<BusDataPackage>()
                    val busId = call.principal<UserIdPrincipal>()
                        ?: throw IllegalStateException(
                            "No vehicle ID passed in to call"
                        )
                    val matchingVehicle = vehicleMetadataProvider.getCachedVehicles()[busId.name]
                        ?: return@post call.respond(mapOf(
                            "error" to "no vehicle metadata found for bus ${busId.name}"
                        ))

                    collection.insertOne(TransportationEvent(
                        busId = busId.name,
                        timestamp = System.currentTimeMillis(),
                        geolocation = matchingVehicle.location,
                        passengerData = mapOf(
                            "passengers" to "${dataPackage.humansDetected}"
                        )
                    ))

                    call.respond(mapOf("success" to "true"))
                }
            }
        }
    }
}
