package br.edu.ifsp.scl.ads.contactspdm.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContactDaoRtDbFb: ContactDao {
    companion object {
        private const val CONTACT_LIST_ROOT_NODE = "contact"
    }

    private val contactRtDbFbReference = Firebase.database.getReference(
        CONTACT_LIST_ROOT_NODE
    )

    // Simula uma consulta no banco de dados
    private val contactList = mutableListOf<Contact>()
    init {
        // Chamado sempre que houver uma modificação no banco de dados de tempo real do Firebase
        contactRtDbFbReference.addChildEventListener(
            object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val contact = snapshot.getValue<Contact>()
                    //val contact = snapshot.getValue(Contact::class.java)
                    if (contact != null) {
                        contactList.add(contact)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val contact = snapshot.getValue<Contact>()

                    if (contact != null) {
                        val index = contactList.indexOfFirst { it.id == contact.id }
                        if (index != -1) {
                            contactList[index] = contact
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val contact = snapshot.getValue<Contact>()

                    if (contact != null) {
                        val index = contactList.indexOfFirst { it.id == contact.id }
                        if (index != -1) {
                            contactList.removeAt(index)
                        }
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // NSA
                }

                override fun onCancelled(error: DatabaseError) {
                    // NSA
                }
            }
        )

        // Chamado uma única vez sempre que o aplicativo for executado
        contactRtDbFbReference.addListenerForSingleValueEvent(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val contactMap = snapshot.getValue<Map<String, Contact>>()

                    if (contactMap != null) {
                        contactList.clear()
                        contactList.addAll(contactMap.values)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // NSA
                }
            }
        )
    }

    override fun createContact(contact: Contact): Int {
        createOrUpdateContact(contact)
        return 1
    }

    override fun retrieveContacts(): MutableList<Contact> {
        return contactList
    }

    override fun updateContact(contact: Contact): Int {
        createOrUpdateContact(contact)
        return 1
    }

    override fun deleteContact(id: Int): Int {
        contactRtDbFbReference.child(id.toString()).removeValue()
        return 1
    }

    private fun createOrUpdateContact(contact: Contact) {
        contactRtDbFbReference.child(contact.id.toString()).setValue(contact)
    }
}