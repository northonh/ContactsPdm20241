package br.edu.ifsp.scl.ads.contactspdm.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Contact::class], version = 1)
abstract class ContactRoomDaoImpl: RoomDatabase() {
    abstract fun getContactRoomDao(): ContactRoomDao
}