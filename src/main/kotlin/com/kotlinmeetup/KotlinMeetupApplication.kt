package com.kotlinmeetup

import com.kotlinmeetup.controllers.articlesController
import com.kotlinmeetup.repositories.ArticlesRepository
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Duration


fun Application.main() {

    install(DefaultHeaders)
    install(CallLogging)

    install(CORS) {
        maxAge = Duration.ofDays(1)
    }

    install(ContentNegotiation) {
        register(
            ContentType.Application.Json,
            GsonConverter()
        )
    }

    routing {
        get("/healthcheck") {
            call.respondText("ok")
        }

        get("/dbcreate") {
            transaction { SchemaUtils.create(ArticlesRepository.Articles) }
            call.response.status(HttpStatusCode.Created)
            call.respond("create table: " + ArticlesRepository.Articles.tableName)
        }

        get("/dbdrop") {
            transaction { SchemaUtils.drop(ArticlesRepository.Articles) }
            call.response.status(HttpStatusCode.OK)
            call.respond("drop table:" + ArticlesRepository.Articles.tableName)
        }

        articlesController()
    }
}

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        8080,
        module = Application::main
    ).start()
}