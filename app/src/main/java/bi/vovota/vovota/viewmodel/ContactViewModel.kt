package bi.vovota.vovota.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bi.vovota.vovota.Contact
import bi.vovota.vovota.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    var contacts = repository.getContacts().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _selectedContact = MutableStateFlow<Contact?>(null)
    val selectedContact: StateFlow<Contact?> = _selectedContact

    //fetch contact from repository
    fun getContactById(id: Int) {
        viewModelScope.launch {
            val contact = repository.getContactById(id)
            _selectedContact.value = contact
        }
    }

    suspend fun getContactByNumber(number: String) : Contact? {
        return repository.getContactByNumber(number)
    }

    //if direct call, share, usage in general directly
    suspend fun fetchContactById(id: Int): Contact? {
        return repository.getContactById(id)
    }
    private val _name = MutableStateFlow("")
    private val _number = MutableStateFlow("")

    val name = _name
    val number = _number

    private val _contactToEdit = MutableStateFlow<Contact?>(null)
    val  contactToEdit = _contactToEdit
    fun setContactToEdit(contact: Contact?) {
        _contactToEdit.value = contact
    }
    fun clearContactToEdit() {
        _contactToEdit.value = null
    }

    private var editingContact: Contact? = null

    fun onNameChange(newName: String) {
        _name.value = newName
    }

    fun onNumberChange(newNumber: String) {
        _number.value = newNumber
    }

    fun saveContact() {
        viewModelScope.launch {
            if (editingContact != null) {
                val updated = editingContact!!.copy(
                    name = _name.value,
                    phoneNumber = _number.value
                )
                repository.updateContact(updated)
            } else {
                val newContact = Contact(
                    name = _name.value,
                    phoneNumber = _number.value
                )
                repository.insertContact(newContact)
            }
            clearForm()
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }

    fun setForm(contact: Contact) {
        _name.value = contact.name
        _number.value = contact.phoneNumber
        editingContact = contact
    }

    fun clearForm() {
        _name.value = ""
        _number.value = ""
        editingContact = null
    }
}