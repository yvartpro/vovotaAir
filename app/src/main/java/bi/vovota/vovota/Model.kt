package bi.vovota.vovota

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    @SerialName("phone_number") val username: String,
    val password: String
)

@Serializable
data class AuthRegister(
    @SerialName("full_name") val name: String,
    val password: String
)
@Serializable
data class AuthResponse(
    val access: String,
    val refresh: String
)
