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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ponomarenko.myipadrress.R
import com.ponomarenko.myipadrress.ui.activity.ui.components.PrimaryButton
import com.ponomarenko.myipadrress.ui.activity.ui.theme.AppTheme
import com.ponomarenko.myipadrress.ui.activity.utils.DevicePreviews
import com.ponomarenko.myipadrress.ui.activity.utils.ThemePreviews

class MainActivity : ComponentActivity() {

    //    private val viewModel: MainActivityViewModel by inject<MainActivityAndroidViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Scaffold { innerPadding ->
                    MainScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 24.dp)
                            .background(MaterialTheme.colorScheme.onSurface)
                            .padding(innerPadding)
                    )
                }
            }
        }
        //        binding = ActivityMainBinding.inflate(layoutInflater)
        //        binding.lifecycleOwner = this
        //        binding.viewModel = viewModel
        //        setContentView(binding.root)
        //
        //        title = getString(R.string.title_main_screen)
        //        initializeViews()
        //
        //        viewModel.onViewCreated()
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
        val text = view.text
        copyToClipboard(getString(R.string.copy_clipboard_label), text)
        Toast.makeText(this@MainActivity, dataCopied, Toast.LENGTH_SHORT)
            .show()
    }

    private fun onRefreshClicked(view: View) {
        val bundle = Bundle()

        val dataRefreshed = getString(R.string.data_refreshed)
        Toast.makeText(this@MainActivity, dataRefreshed, Toast.LENGTH_SHORT)
            .show()
    }

    companion object {

        private const val MAIL_REQUEST = 1110
    }

    @Composable
    fun MainScreen(modifier: Modifier) {
        Box(modifier = modifier.fillMaxWidth()) {
            PrimaryButton({}, getString(R.string.refresh_data))
        }
    }

    @ThemePreviews
    @DevicePreviews
    @Composable
    fun MainScreenPreview() {
        MainScreen(Modifier)
    }
}