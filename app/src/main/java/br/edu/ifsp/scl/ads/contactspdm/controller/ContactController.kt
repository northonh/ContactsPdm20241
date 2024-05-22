package br.edu.ifsp.scl.ads.contactspdm.controller

import br.edu.ifsp.scl.ads.contactspdm.model.Contact
import br.edu.ifsp.scl.ads.contactspdm.model.ContactDao
import br.edu.ifsp.scl.ads.contactspdm.model.ContactDaoSqlite
import br.edu.ifsp.scl.ads.contactspdm.ui.MainActivity

class ContactController(mainActivity: MainActivity) {
    private val contactDaoImpl: ContactDao = ContactDaoSqlite(mainActivity)

    fun insertContact(contact: Contact) = contactDaoImpl.createContact(contact)
    fun getContacts() = contactDaoImpl.retrieveContacts()
    fun editContact(contact: Contact) = contactDaoImpl.updateContact(contact)
    fun removeContact(id: Int) = contactDaoImpl.deleteContact(id)
}