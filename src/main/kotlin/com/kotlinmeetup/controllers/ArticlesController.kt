package com.kotlinmeetup.controllers

import com.kotlinmeetup.models.Article
import com.kotlinmeetup.repositories.ArticlesRepository
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.articlesController() {

    var articlesRepository = ArticlesRepository()

    route("/api") {
        route("/articles") {

            /*
                if (title == null || content == null) {
                    call.respond(HttpStatusCode(422, "Unprocessable Entity"))
                }
            }*/

            get("/") {
                call.respond(articlesRepository.index())
            }

            post("/") {
                val article = call.receive<Article>()
                //logger.debug { message }
                call.respond(articlesRepository.create(article))
            }

            get("/{id}") {
                val id = call.parameters["id"]!!.toLong()
                call.respond(articlesRepository.show(id))
            }

            put("/{id}") {
                val id = call.parameters["id"]!!.toLong()
                val article = call.receive<Article>()
                call.respond(articlesRepository.update(id, article))
            }

            delete("/{id}") {
                val id = call.parameters["id"]!!.toInt()
                call.respond(articlesRepository.delete(id))
            }
        }
    }
}