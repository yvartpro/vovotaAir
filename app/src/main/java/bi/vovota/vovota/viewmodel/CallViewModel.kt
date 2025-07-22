package bi.vovota.vovota.viewmodel

import androidx.lifecycle.ViewModel
import bi.vovota.vovota.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class CallViewModel : ViewModel() {

    private val _enteredNumber = MutableStateFlow("")
    var enteredNumber = _enteredNumber.asStateFlow()

    private val _name = MutableStateFlow("")
    var name = _name.asStateFlow()


    private val _callOngoing = MutableStateFlow(false)
    val callOngoing = _callOngoing.asStateFlow()

    fun appendDigit(digit: String) {
        _enteredNumber.value += digit
    }

    fun backspace() {
        _enteredNumber.value = _enteredNumber.value.dropLast(1)
    }

    fun clearNumber() {
        _enteredNumber.value = ""
    }

    fun startCall(num: String, name: String?) {
        _enteredNumber.value = num
        _name.value = name.toString()
        if (_enteredNumber.value.isNotBlank()) {
            _callOngoing.value = true
        }
    }

    fun endCall() {
        _callOngoing.value = false
    }

    //add and edit contact
    private val _contactName = MutableStateFlow("")
    val contactName: StateFlow<String> = _contactName

    private val _contactNumber = MutableStateFlow("")
    val contactNumber: StateFlow<String> = _contactNumber

    fun setContactName(name: String) {
        _contactName.value = name
    }

    fun setContactNumber(number: String) {
        _contactNumber.value = number
    }

    fun loadContactForEdit(contact: Contact) {
        _contactName.value = contact.name
        _contactNumber.value = contact.phoneNumber
    }

    fun clearContactFields() {
        _contactName.value = ""
        _contactNumber.value = ""
    }

    fun saveOrUpdateContact(onDone: (Contact) -> Unit) {
        val newContact = Contact(
            name = _contactName.value,
            phoneNumber = _contactNumber.value
        )
        onDone(newContact)
        clearContactFields()
    }

    //contact list
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    fun addContact(contact: Contact) {
        _contacts.value = _contacts.value + contact
    }

    fun updateContact(updated: Contact) {
        _contacts.value = _contacts.value.map {
            if (it.id == updated.id) updated else it
        }
    }

    private val _editingContact = MutableStateFlow<Contact?>(null)
    val editingContact: StateFlow<Contact?> = _editingContact

    fun setContactToEdit(contact: Contact) {
        _editingContact.value = contact
    }

    fun setReadContactNumber(number: String) {
        _editingContact.value = Contact(id = 0, name = "", phoneNumber = number)
    }
}
