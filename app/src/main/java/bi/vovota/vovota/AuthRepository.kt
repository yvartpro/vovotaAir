package bi.vovota.vovota

import bi.vovota.vovota.network.ApiService
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AuthRepository {
    private val client = ApiService.client
    private val baseUrl = "https://air.vovota.bi"

    suspend fun login(name: String, password: String): AuthResponse {
        return client.post("$baseUrl/api/login/") {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(name, password))
        }.body()
    }

    suspend fun register(name: String, password: String): Boolean {
        return try {
            val resp = client.post("$baseUrl/api/user/") {
                contentType(ContentType.Application.Json)
                setBody(AuthRegister(name, password))
            }
            if(resp.status == HttpStatusCode.Created || resp.status == HttpStatusCode.OK){
                true
            }else{
                val err = resp.bodyAsText()
                println("Error register: ${resp.status} -$err ")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}