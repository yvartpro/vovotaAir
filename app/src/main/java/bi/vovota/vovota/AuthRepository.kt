package bi.vovota.vovota

import bi.vovota.vovota.network.ApiService
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthRepository {
    private val client = ApiService.client
    private val baseUrl = "https://mib.vovota.bi"

    suspend fun login(username: String, password: String): AuthResponse {
        return client.post("$baseUrl/token/") {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(username, password))
        }.body()
    }

    suspend fun register(username: String, password: String): AuthResponse {
        return client.post("$baseUrl/api/register/") {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(username, password))
        }.body()
    }
}