package com.cole

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.response.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.cole.models.Users


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/ktorapp",
        driver = "org.postgresql.Driver",
        user = "cole",
        password = "femi"
    )

    transaction {
        SchemaUtils.create(Users)
    }

    install(Sessions) {
        cookie<UserSession>("USER_SESSION")
    }


    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    // Routing
    routing {
        // Serve static frontend files
        staticResources("/static", "static")

        get("/home-data") {
            val session = call.sessions.get<UserSession>()
            if (session == null) call.respondText("Not logged in", status = io.ktor.http.HttpStatusCode.Unauthorized)
            else call.respond(mapOf("firstName" to session.firstName))
        }

        //  backend routes (registration/login/home/logout)
        registerRoutes()
    }
}