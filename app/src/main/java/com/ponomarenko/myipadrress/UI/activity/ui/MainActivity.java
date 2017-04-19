package com.ponomarenko.myipadrress.UI.activity.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ponomarenko.myipadrress.R;
import com.ponomarenko.myipadrress.UI.activity.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView ipAddressTextView, networkNameTextView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private View.OnClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.title_main_screen));
        clickListener = new MyOnClickListener();
        ipAddressTextView = (TextView) findViewById(R.id.ip_address_text_view);
        String ipAddress = Utils.getIPAddress(true);
        ipAddressTextView.setText(ipAddress);
        ipAddressTextView.setOnClickListener(clickListener);

        networkNameTextView = (TextView) findViewById(R.id.network_name);
        String networkName = Utils.getWifiName(getApplicationContext());
        networkNameTextView.setText(networkName);
        networkNameTextView.setOnClickListener(clickListener);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "bundle_id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "bundle_name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }


    private void copyToClipboard(CharSequence label, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }


    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            CharSequence text = ((TextView) v).getText();
            copyToClipboard("MyIpAddress", text);
        }
    }
}
