package com.ponomarenko.myipadrress.ui.activity.ui


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.ponomarenko.myipadrress.R
import com.ponomarenko.myipadrress.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private val viewModel: MainActivityViewModel by inject<MainActivityAndroidViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)

        title = getString(R.string.title_main_screen)
        initializeViews()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        viewModel.onViewCreated()
    }

    private fun initializeViews() {
        //        binding.apply {
        //            ipAddress.apply { setOnClickListener { copyToBuffer(this) } }
        //            networkType.setOnClickListener { copyToBuffer(networkType) }
        //            networkName.setOnClickListener { copyToBuffer(networkName) }
        //            refreshButton.setOnClickListener { viewModel.loadData() }
        //        }
    }

    private fun copyToClipboard(label: CharSequence, text: CharSequence) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_share_app -> shareApp()
            R.id.meu_item_contact_us -> startEmailClient()
        }
        return true
    }

    private fun shareApp() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND

        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.url_google_play))
        startActivity(Intent.createChooser(intent, "Share"))
    }

    private fun startEmailClient() {
        val intent = Intent(
            Intent.ACTION_SENDTO,
            Uri.parse(
                "mailto:" + TextUtils.join(
                    ",",
                    arrayOf(getString(R.string.developer_email))
                )
            )
        )
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
        startActivityForResult(Intent.createChooser(intent, "Invite friends"), MAIL_REQUEST)
    }

    private fun copyToBuffer(view: TextView) {
        val bundle = Bundle()

        val dataCopied = getString(R.string.data_copied)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, dataCopied)
        mFirebaseAnalytics?.apply {
            logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }

        val text = view.text
        copyToClipboard(getString(R.string.copy_clipboard_label), text)
        Toast.makeText(this@MainActivity, dataCopied, Toast.LENGTH_SHORT)
            .show()
    }

    private fun onRefreshClicked(view: View) {
        val bundle = Bundle()
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, view.id)

        val dataRefreshed = getString(R.string.data_refreshed)
        Toast.makeText(this@MainActivity, dataRefreshed, Toast.LENGTH_SHORT)
            .show()

        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, dataRefreshed)
        mFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    companion object {

        private const val MAIL_REQUEST = 1110
    }
}
