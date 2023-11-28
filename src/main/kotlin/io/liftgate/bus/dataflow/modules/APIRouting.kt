package io.liftgate.bus.dataflow.modules

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.liftgate.bus.dataflow.models.BusDataPackage
import io.liftgate.bus.dataflow.models.database.TransportationEvent
import io.liftgate.bus.dataflow.vehicleMetadataProvider

fun Application.configureRouting()
{
    routing {
        route("/api/v1/") {
            route("/datastore/") {
                get("/recent-events/") {
                    // TODO
                }
            }

            route("/dataflow/") {
                post("submit-data-package/") {
                    val dataPackage = context.receive<BusDataPackage>()
                    val matchingVehicle = vehicleMetadataProvider.getCachedVehicles()[dataPackage.busId]
                        ?: return@post call.respond(mapOf(
                            "error" to "no vehicle metadata found for bus ${dataPackage.busId}"
                        ))

                    collection.insertOne(TransportationEvent(
                        busId = dataPackage.busId,
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
