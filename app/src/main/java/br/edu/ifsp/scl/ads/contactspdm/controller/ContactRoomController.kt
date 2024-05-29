package br.edu.ifsp.scl.ads.contactspdm.controller

import android.os.AsyncTask
import android.os.Message
import androidx.room.Room
import br.edu.ifsp.scl.ads.contactspdm.model.Contact
import br.edu.ifsp.scl.ads.contactspdm.model.ContactDao
import br.edu.ifsp.scl.ads.contactspdm.model.ContactDaoSqlite
import br.edu.ifsp.scl.ads.contactspdm.model.ContactRoomDao
import br.edu.ifsp.scl.ads.contactspdm.model.ContactRoomDao.Companion.CONTACT_DATABASE_FILE
import br.edu.ifsp.scl.ads.contactspdm.model.ContactRoomDaoImpl
import br.edu.ifsp.scl.ads.contactspdm.ui.MainActivity

class ContactRoomController(private val mainActivity: MainActivity) {
    private val contactDaoImpl: ContactRoomDao by lazy {
        Room.databaseBuilder(
            mainActivity,
            ContactRoomDaoImpl::class.java,
            CONTACT_DATABASE_FILE
        ).build().getContactRoomDao()
    }

    fun insertContact(contact: Contact) {
        Thread {
            contactDaoImpl.createContact(contact)
            getContacts()
        }.start()
    }

    fun getContacts() {
        Thread {
            val contactList = contactDaoImpl.retrieveContacts()
            mainActivity.uiUpdaterHandler.sendMessage(
                Message.obtain().apply{
                    data.putParcelableArrayList(
                        "CONTACT_ARRAY",
                        ArrayList(contactList)
                    )
                }
            )
        }.start()
    }

    fun editContact(contact: Contact) {
        Thread{
            contactDaoImpl.updateContact(contact)
            getContacts()
        }.start()
    }

    fun removeContact(contact: Contact) {
        Thread{
            contactDaoImpl.deleteContact(contact)
            getContacts()
        }.start()
    }
}
