package bi.vovota.vovota

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AuthRequest(
    @SerialName("phone_number") val phone: String,
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

@Entity(tableName = "Contact")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phoneNumber: String
)