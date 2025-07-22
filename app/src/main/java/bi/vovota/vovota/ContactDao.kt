package bi.vovota.vovota

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM Contact ORDER BY name ASC")
    fun getContacts(): Flow<List<Contact>>

    @Query("SELECT * FROM Contact WHERE id = :id")
    suspend fun getContactById(id: Int): Contact?

    @Query("SELECT * FROM Contact WHERE phoneNumber = :number LIMIT 1")
    suspend fun getContactByNumber(number: String): Contact?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Update
    suspend fun update(contact: Contact)
    @Delete
    suspend fun delete(contact: Contact)
}