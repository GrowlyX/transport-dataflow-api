package io.liftgate.bus.dataflow.modules

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.liftgate.bus.dataflow.models.BusDataPackage
import io.liftgate.bus.dataflow.models.database.Records
import io.liftgate.bus.dataflow.models.database.TransportationEvent
import io.liftgate.bus.dataflow.vehicleMetadataProvider
import kotlinx.serialization.Serializable

fun Application.configureRouting()
{
    routing {
        get("metrics") {
            if (call.request.local.remoteHost != "localhost")
            {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respond(appMicrometerRegistry.scrape())
        }

        authenticate("vehicle-id-apikey") {
            route("/api/v1/") {
                route("datastore") {
                    @Serializable
                    data class EventResponse(val data: List<TransportationEvent>)

                    get("get-all") {
                        call.respond(EventResponse(
                            data = collection.find().toList()
                        ))
                    }
                }

                route("dataflow") {
                    post("submit") {
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
                            passengerData = Records(
                                totalPassengers = dataPackage.humansDetected
                            )
                        ))

                        call.respond(mapOf("success" to "true"))
                    }
                }
            }
        }
    }
}
