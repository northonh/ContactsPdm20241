package br.edu.ifsp.scl.ads.contactspdm.controller

import android.os.Message
import br.edu.ifsp.scl.ads.contactspdm.model.Contact
import br.edu.ifsp.scl.ads.contactspdm.model.ContactDao
import br.edu.ifsp.scl.ads.contactspdm.model.ContactDaoRtDbFb
import br.edu.ifsp.scl.ads.contactspdm.ui.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContactRtDbFbController(private val mainActivity: MainActivity) {
    private val contactDaoImpl: ContactDao = ContactDaoRtDbFb()

    fun insertContact(contact: Contact) {
        GlobalScope.launch {
            contactDaoImpl.createContact(contact)
        }
    }

    fun getContacts(){
        val contactList = contactDaoImpl.retrieveContacts()
        if (contactList.isNotEmpty()) {
            mainActivity.uiUpdaterHandler.sendMessage(
                Message.obtain().apply{
                    data.putParcelableArrayList(
                        "CONTACT_ARRAY",
                        ArrayList(contactList)
                    )
                }
            )
        }
    }

    fun editContact(contact: Contact) {
        Thread {
            contactDaoImpl.updateContact(contact)
        }.start()
    }

    fun removeContact(id: Int){
        Thread {
            contactDaoImpl.deleteContact(id)
        }.start()
    }
}

