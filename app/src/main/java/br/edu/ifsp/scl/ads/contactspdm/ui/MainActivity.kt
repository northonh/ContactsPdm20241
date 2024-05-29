package br.edu.ifsp.scl.ads.contactspdm.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.contactspdm.R
import br.edu.ifsp.scl.ads.contactspdm.adapter.ContactAdapter
import br.edu.ifsp.scl.ads.contactspdm.controller.ContactController
import br.edu.ifsp.scl.ads.contactspdm.controller.ContactRoomController
import br.edu.ifsp.scl.ads.contactspdm.databinding.ActivityMainBinding
import br.edu.ifsp.scl.ads.contactspdm.model.Constant.EXTRA_CONTACT
import br.edu.ifsp.scl.ads.contactspdm.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.ifsp.scl.ads.contactspdm.model.Contact
import java.util.Arrays

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val contactList: MutableList<Contact> = mutableListOf()

    // Adapter
    private val contactAdapter: ContactAdapter by lazy {
        ContactAdapter(this, contactList)
    }

    // Controller
    private val contactController: ContactRoomController by lazy {
        ContactRoomController(this)
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        amb.toolbarIn.toolbar.apply {
            subtitle = this@MainActivity.javaClass.simpleName
            setSupportActionBar(this)
        }

        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val contact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(EXTRA_CONTACT, Contact::class.java)
                }
                else {
                    result.data?.getParcelableExtra(EXTRA_CONTACT)
                }
                contact?.let { newOrEditedContact ->
                    if (contactList.any{ it.id == newOrEditedContact.id}){
                        contactController.editContact(newOrEditedContact)
                    }
                    else {
                        contactController.insertContact(newOrEditedContact)
                    }
                }
            }
        }

        registerForContextMenu(amb.contactsLv)
        amb.contactsLv.setOnItemClickListener { _, _, position, _ ->
            val contact = contactList[position]
            val viewContactIntent = Intent(this@MainActivity, ContactActivity::class.java)
            viewContactIntent.putExtra(EXTRA_CONTACT, contact)
            viewContactIntent.putExtra(EXTRA_VIEW_CONTACT, true)
            startActivity(viewContactIntent)
        }
        contactController.getContacts()

        amb.contactsLv.adapter = contactAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == R.id.addContactMi) {
            Intent(this, ContactActivity::class.java).let {
                carl.launch(it)
            }
            true
        } else
            false

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterContextMenuInfo).position
        return when(item.itemId) {
            R.id.removeContactMi -> {
                contactController.removeContact(contactList[position])
                Toast.makeText(this, "Contact removed.", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editContactMi -> {
                carl.launch(
                    Intent(this, ContactActivity::class.java).apply{
                        putExtra(EXTRA_CONTACT, contactList[position])
                    }
                )
                true
            }
            else -> { false }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterForContextMenu(amb.contactsLv)
    }

    private fun fillContacts() {
        for (i in 1..10) {
            contactList.add(
                Contact(
                    i,
                    "Name $i",
                    "Address $i",
                    "Phone $i",
                    "Email $i"
                )
            )
        }
    }

    fun updateContactList(contacts: MutableList<Contact>) {
        contactList.clear()
        contactList.addAll(contacts)
        contactAdapter.notifyDataSetChanged()
    }

    val uiUpdaterHandler: Handler = object: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            msg.data.getParcelableArrayList<Contact>("CONTACT_ARRAY")?.let { _contactArray ->
                updateContactList(_contactArray.toMutableList())
            }
        }
    }
}