package com.ponomarenko.myipadrress.ui


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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.ponomarenko.myipadrress.R
import com.ponomarenko.myipadrress.data.NetworkManager
import com.ponomarenko.myipadrress.utils.InjectorUtils
import com.ponomarenko.myipadrress.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mAdView: AdView
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeUI()
        title = getString(R.string.title_main_screen)
        setListeners()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        initAds()
    }

    private fun initializeUI() {
        val factory = InjectorUtils.provideMainViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)

        viewModel.getIPAddress().observe(this, Observer {
            ip_address_text_view.text = it.ip
            network_type.text = it.type.toString()
            network_name.text = it.name
        })


    }

    private fun initAds() {
        MobileAds.initialize(applicationContext, getString(R.string.ads_app_id))
    }

    override fun onResume() {
        super.onResume()
//        loadData()
        loadAdvertisement()
    }

    private fun loadAdvertisement() {
        val adRequest = AdRequest.Builder().build()
        mAdView = findViewById(R.id.adView)
        mAdView.loadAd(adRequest)
    }

    private fun setListeners() {
        ip_address_text_view.setOnClickListener(this)
        network_name.setOnClickListener(this)
        network_type.setOnClickListener(this)

        val refresh = findViewById<Button>(R.id.refresh_button)
        refresh.setOnClickListener(this)

    }

    private fun loadData() {
        setIpAddress()
        setNetworkName()
        setNetworkType()
    }

    private fun setNetworkType() {
        val networkType = Utils.networkType(applicationContext)
        network_type.text = networkType
        if (networkType.isNullOrEmpty()) {
            ip_address_text_view.text = null
        }
    }

    private fun setNetworkName() {
        val networkName = Utils.networkName(applicationContext)
        network_name.text = networkName
    }

    private fun setIpAddress() {
        val ipAddress = Utils.getIPAddress(true)
        if (ipAddress != null) {
            ip_address_text_view.text = ipAddress
        } else {
            ip_address_text_view.text = ""
        }
    }


    private fun copyToClipboard(label: CharSequence, text: CharSequence) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.primaryClip = clip
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
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + TextUtils.join(",", arrayOf(getString(R.string.developer_email)))))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
        startActivityForResult(Intent.createChooser(intent, "Invite friends"), MAIL_REQUEST)

    }

    override fun onClick(v: View) {
        val bundle = Bundle()
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, v.id)

        when (v.id) {
            R.id.refresh_button -> {

                NetworkManager.updateIPAddress(this)

                val dataRefreshed = getString(R.string.data_refreshed)
                Toast.makeText(this@MainActivity, dataRefreshed, Toast.LENGTH_SHORT).show()

                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, dataRefreshed)
                mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
            }
            else -> {

                val dataCopied = getString(R.string.data_copied)
                Toast.makeText(this@MainActivity, dataCopied, Toast.LENGTH_SHORT).show()

                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, dataCopied)
                mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

                val text = (v as TextView).text
                copyToClipboard(getString(R.string.copy_clipboard_label), text)
            }
        }
    }

    companion object {
        private const val MAIL_REQUEST = 1110
    }
}
