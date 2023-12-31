package io.liftgate.bus.dataflow.modules

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.IndexModel
import com.mongodb.client.model.Sorts
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.liftgate.bus.dataflow.models.database.TransportationEvent
import org.litote.kmongo.getCollection
import org.litote.kmongo.serialization.SerializationClassMappingTypeService
import org.litote.kmongo.withKMongo

/**
 * @author GrowlyX
 * @since 11/27/2023
 */
lateinit var collection: MongoCollection<TransportationEvent>

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

    collection = database.getCollection<TransportationEvent>().withKMongo()
    collection.createIndexes(listOf(
        IndexModel(Sorts.ascending("busId")),
        IndexModel(Sorts.descending("timestamp")),
    ))
}
