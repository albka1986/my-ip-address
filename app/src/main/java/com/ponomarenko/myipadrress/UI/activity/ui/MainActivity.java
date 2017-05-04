package com.ponomarenko.myipadrress.UI.activity.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ponomarenko.myipadrress.R;
import com.ponomarenko.myipadrress.UI.activity.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static final int MAIL_REQUEST = 1110;
    private AdView mAdView;
    private TextView ipAddressTextView, networkNameTextView, networkTypeTexView;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.title_main_screen));
        initializeViews();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        loadAdvertisement();
    }

    private void loadAdvertisement() {
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void initializeViews() {
        View.OnClickListener clickListener = new MyOnClickListener();

        ipAddressTextView = (TextView) findViewById(R.id.ip_address_text_view);
        ipAddressTextView.setOnClickListener(clickListener);

        networkNameTextView = (TextView) findViewById(R.id.network_name);
        networkNameTextView.setOnClickListener(clickListener);

        networkTypeTexView = (TextView) findViewById(R.id.network_type);
        networkTypeTexView.setOnClickListener(clickListener);

        Button refresh = (Button) findViewById(R.id.refresh_button);
        refresh.setOnClickListener(clickListener);

    }

    private void loadData() {
        setIpAddress();
        setNetworkName();
        setNetworkType();
    }

    private void setNetworkType() {
        String networkType = Utils.networkType(getApplicationContext());
        networkTypeTexView.setText(networkType);
    }

    private void setNetworkName() {
        String networkName = Utils.networkName(getApplicationContext());
        networkNameTextView.setText(networkName);
    }

    private void setIpAddress() {
        String ipAddress = Utils.getIPAddress(true);
        if (ipAddress != null) {
            ipAddressTextView.setText(ipAddress);
        } else {
            ipAddressTextView.setText("");
        }
    }


    private void copyToClipboard(CharSequence label, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }


    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, v.getId());


            switch (v.getId()) {
                case R.id.refresh_button:

                    loadData();

                    String dataRefreshed = getString(R.string.data_refreshed);
                    Toast.makeText(MainActivity.this, dataRefreshed, Toast.LENGTH_SHORT).show();

                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, dataRefreshed);
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    break;
                default:

                    String dataCopied = getString(R.string.data_copied);
                    Toast.makeText(MainActivity.this, dataCopied, Toast.LENGTH_SHORT).show();

                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, dataCopied);
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                    CharSequence text = ((TextView) v).getText();
                    copyToClipboard(getString(R.string.copy_clipboard_label), text);
                    break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share_app:
                shareApp();
                break;

            case R.id.meu_item_contact_us:
                startEmailClient();
                break;
        }
        return true;
    }

    private void shareApp() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.url_google_play));
        startActivity(Intent.createChooser(intent, "Share"));
    }

    private void startEmailClient() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + TextUtils.join(",", new String[]{getString(R.string.developer_email)})));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        startActivityForResult(Intent.createChooser(intent, "Invite friends"), MAIL_REQUEST);

    }
}
