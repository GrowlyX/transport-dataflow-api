package io.liftgate.bus.dataflow.modules

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.liftgate.bus.dataflow.models.database.TransportationData
import org.litote.kmongo.getCollection
import org.litote.kmongo.serialization.SerializationClassMappingTypeService

/**
 * @author GrowlyX
 * @since 11/27/2023
 */
lateinit var collection: MongoCollection<TransportationData>

fun Application.configureMongoDatabase()
{
    val config = environment.config.config("mongo")
    System.setProperty(
        "org.litote.mongo.mapping.service",
        SerializationClassMappingTypeService::class.qualifiedName!!
    )

    val client = MongoClients.create(
        config.tryGetString("connection-uri")
            ?: throw IllegalStateException("No connection URI defined")
    )
    val database = client.getDatabase(
        config.tryGetString("database")
            ?: throw IllegalStateException("No database name defined")
    )

    collection = database.getCollection<TransportationData>(
        config.tryGetString("collection")
            ?: throw IllegalStateException("No collection name defined")
    )
}
