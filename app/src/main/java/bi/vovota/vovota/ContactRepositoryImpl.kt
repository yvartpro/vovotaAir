package bi.vovota.vovota

import kotlinx.coroutines.flow.Flow

class ContactRepositoryImpl(private val dao: ContactDao) : ContactRepository {
    override fun getContacts(): Flow<List<Contact>> = dao.getContacts()
    override suspend fun insertContact(contact: Contact) {
        dao.insert(contact)
    }
    override suspend fun deleteContact(contact: Contact) {
        dao.delete(contact)
    }
    override suspend fun getContactById(id: Int): Contact? {
        return dao.getContactById(id)
    }

    override suspend fun getContactByNumber(number: String): Contact? {
        return dao.getContactByNumber(number)
    }

    override suspend fun updateContact(contact: Contact) {
        dao.update(contact)
    }
}