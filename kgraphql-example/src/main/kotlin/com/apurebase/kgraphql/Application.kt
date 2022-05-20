package com.apurebase.kgraphql

import com.apurebase.kgraphql.model.User
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(Authentication) {
        basic {
            realm = "ktor"
            validate {
                User(4, it.name)
            }
        }
    }

    install(GraphQL) {
        useDefaultPrettyPrinter = true
        playground = true
        endpoint = "/"

        wrap {
            authenticate(optional = true, build = it)
        }

        context { call ->
            call.authentication.principal<User>()?.let {
                +it
            }
        }

        schema { ufoSchema() }
    }
}
