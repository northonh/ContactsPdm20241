package br.edu.ifsp.scl.ads.contactspdm.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = -1,
    @NonNull
    var name: String = "",
    @NonNull
    var address: String = "",
    @NonNull
    var phone: String = "",
    @NonNull
    var email: String = ""
): Parcelable
