package br.edu.ifsp.scl.ads.contactspdm.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactRoomDao {
    companion object {
        const val CONTACT_DATABASE_FILE = "contacts_room"
        private const val CONTACT_TABLE = "contact"
        private const val NAME_COLUMN = "name"
    }

    @Insert
    fun createContact(contact: Contact)
    @Query("SELECT * FROM $CONTACT_TABLE ORDER BY $NAME_COLUMN")
    fun retrieveContacts(): MutableList<Contact>
    @Update
    fun updateContact(contact: Contact): Int
    @Delete
    fun deleteContact(contact: Contact): Int
}