package io.liftgate.bus.dataflow.modules

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.liftgate.bus.dataflow.models.BusDataPackage
import io.liftgate.bus.dataflow.module
import kotlin.test.Test

class APIRoutingKtTest
{
    @Test
    fun testPostApiV1Submitdatapackage() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json(json)
            }
        }

        client.post("/api/v1/submit-data-package/") {
            setBody(BusDataPackage(
                busId = "hors",
                humansDetected = 32
            ))
        }
    }
}
