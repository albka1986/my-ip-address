package com.ponomarenko.myipadrress.ui.activity.ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ponomarenko.myipadrress.R
import com.ponomarenko.myipadrress.ui.activity.ui.theme.AppTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val viewModel: MainAndroidViewModel by inject<MainAndroidViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        viewModel.updateData()
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

    companion object {

        private const val MAIL_REQUEST = 1110
    }
}