package com.cole

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import io.ktor.server.html.*
import kotlinx.html.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.cole.models.Users
import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserSession(
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val phone: String
)

fun Application.registerRoutes() {
    routing {

        // Registration
        post("/register") {
            val params = call.receiveParameters()
            val firstName = params["firstName"] ?: return@post call.respondText("Missing firstName")
            val lastName = params["lastName"] ?: return@post call.respondText("Missing lastName")
            val phone = params["phone"] ?: return@post call.respondText("Missing phone")
            val password = params["password"] ?: return@post call.respondText("Missing password")

            try {
                val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

                val userId: Int? = transaction {
                    Users.insert {
                        it[Users.firstName] = firstName
                        it[Users.lastName] = lastName
                        it[Users.phone] = phone
                        it[Users.passwordHash] = hashedPassword
                    } get Users.id
                }?.value

                if (userId == null) {
                    call.respondText("Failed to register user")
                } else {
                    call.sessions.set(
                        UserSession(
                            userId = userId,
                            firstName = firstName,
                            lastName = lastName,
                            phone = phone
                        )
                    )
                    call.respondRedirect("/static/home.html")
                }

            } catch (e: Exception) {
                call.respondText("Registration failed: ${e.localizedMessage}")
            }
        }

        // Login
        post("/login") {
            val params = call.receiveParameters()
            val phone = params["phone"] ?: return@post call.respondText("Missing phone")
            val password = params["password"] ?: return@post call.respondText("Missing password")

            val userRow: ResultRow? = transaction {
                Users.selectAll()
                    .where { Users.phone eq phone }
                    .limit(1)
                    .firstOrNull()
            }

            if (userRow == null || !BCrypt.checkpw(password, userRow[Users.passwordHash])) {
                return@post call.respondText("Invalid phone or password")
            }

            call.sessions.set(
                UserSession(
                    userId = userRow[Users.id].value,
                    firstName = userRow[Users.firstName],
                    lastName = userRow[Users.lastName],
                    phone = userRow[Users.phone]
                )
            )
            call.respondRedirect("/static/home.html")
        }

        // Home (debug endpoint)
        get("/home") {
            val session = call.sessions.get<UserSession>()
            if (session == null) {
                call.respondText("Not logged in")
            } else {
                call.respondHtml {
                    body {
                        h1 { +"Welcome, ${session.firstName}!" }
                        p { +"Phone: ${session.phone}" }
                        form("/logout", method = FormMethod.post) {
                            submitInput { value = "Logout" }
                        }
                    }
                }
            }
        }

        // Logout
        post("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/static/index.html")
        }
    }
}