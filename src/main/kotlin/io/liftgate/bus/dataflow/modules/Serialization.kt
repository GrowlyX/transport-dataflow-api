package io.liftgate.bus.dataflow.modules

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.util.*

object UUIDSerializer : KSerializer<UUID>
{
    override val descriptor =
        PrimitiveSerialDescriptor("UUIDSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID =
        UUID.fromString(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: UUID)
    {
        encoder.encodeString(value.toString())
    }
}

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
