package io.liftgate.bus.dataflow.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

val json = Json {
    prettyPrint = false
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    // responses back from client
    allowSpecialFloatingPointValues = true
    allowStructuredMapKeys = true
}

fun Application.configureSerialization()
{
    install(ContentNegotiation) {
        json(json)
    }
}
