package br.edu.ifsp.scl.ads.contactspdm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.ifsp.scl.ads.contactspdm.R
import br.edu.ifsp.scl.ads.contactspdm.model.Contact

class ContactAdapter(context: Context, private val contactList: MutableList<Contact>):
    ArrayAdapter<Contact>(context, R.layout.tile_contact, contactList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // pegar o contato no data source
        val contact = contactList[position]

        // inflo uma nova célula se necessário
        var contactTileView = convertView
        if (contactTileView == null) {
            contactTileView = LayoutInflater.from(context).inflate(
                R.layout.tile_contact,
                parent,
                false
            ).apply{
                tag = TileContactHolder(
                findViewById(R.id.nameTv),
                findViewById(R.id.emailTv)
                )
            }
        }

        // colocar os valores de contato na célula
        (contactTileView?.tag as TileContactHolder).apply{
            nameTv.text = contact.name
            emailTv.text = contact.email
        }

        // retorna a célula preenchida
        return contactTileView
    }

    private data class TileContactHolder(val nameTv: TextView, val emailTv: TextView)
}