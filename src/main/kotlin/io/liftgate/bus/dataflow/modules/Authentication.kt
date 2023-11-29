package io.liftgate.bus.dataflow.modules

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*

/**
 * @author GrowlyX
 * @since 11/29/2023
 */
fun Application.configureAuthentication()
{
    val requestAPIKey = environment.config.config("api")
        .tryGetString("auth-token")
        ?: throw IllegalStateException(
            "No auth token specified in config file"
        )

    authentication {
        basic(name = "vehicle-id-apikey") {
            realm = "Basic Auth with Vehicle ID and API Key"
            validate { credentials ->
                if (credentials.password == requestAPIKey)
                {
                    UserIdPrincipal(credentials.name)
                }

                null
            }
        }
    }
}
