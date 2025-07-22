package bi.vovota.vovota

import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getContacts(): Flow<List<Contact>>
    suspend fun insertContact(contact: Contact)
    suspend fun deleteContact(contact: Contact)
    suspend fun getContactById(id: Int): Contact?
    suspend fun getContactByNumber(number: String): Contact?
    suspend fun updateContact(contact: Contact)
}