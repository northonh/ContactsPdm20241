package br.edu.ifsp.scl.ads.contactspdm.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.ads.contactspdm.R
import br.edu.ifsp.scl.ads.contactspdm.databinding.ActivityContactBinding
import br.edu.ifsp.scl.ads.contactspdm.model.Constant.EXTRA_CONTACT
import br.edu.ifsp.scl.ads.contactspdm.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.ifsp.scl.ads.contactspdm.model.Contact

class ContactActivity : AppCompatActivity() {
    private val acb: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)
        acb.toolbarIn.toolbar.apply {
            subtitle = this@ContactActivity.javaClass.simpleName
            setSupportActionBar(this)
        }
        val receivedContact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_CONTACT", Contact::class.java)
        }
        else {
            intent.getParcelableExtra("EXTRA_CONTACT")
        }

        with(acb) {
            receivedContact?.let {
                nameEt.setText(receivedContact.name)
                addressEt.setText(receivedContact.address)
                phoneEt.setText(receivedContact.phone)
                emailEt.setText(receivedContact.email)
                if (intent.getBooleanExtra(EXTRA_VIEW_CONTACT, false)) {
                    nameEt.isEnabled = false
                    addressEt.isEnabled = false
                    phoneEt.isEnabled = false
                    emailEt.isEnabled = false
                    saveBt.visibility = View.GONE
                }
            }
            saveBt.setOnClickListener {
                Contact(
                    id = receivedContact?.id,
                    name = nameEt.text.toString(),
                    address = addressEt.text.toString(),
                    phone = phoneEt.text.toString(),
                    email = emailEt.text.toString()
                ).let { contact ->
                    Intent().apply {
                        putExtra(EXTRA_CONTACT, contact)
                        setResult(RESULT_OK, this)
                        finish()
                    }
                }
            }
        }
    }
}