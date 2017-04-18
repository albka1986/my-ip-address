package com.ponomarenko.myipadrress.UI.activity.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ponomarenko.myipadrress.R;
import com.ponomarenko.myipadrress.UI.activity.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    TextView ipAddressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String ipAddress = Utils.getIPAddress(true);
        ipAddressTextView = (TextView) findViewById(R.id.ip_address_text_view);
        ipAddressTextView.setText(ipAddress);
        ipAddressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = ((TextView) v).getText();
                copyToClipboard("Ip Address", text);
                Toast.makeText(MainActivity.this, getString(R.string.copySuccessful), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void copyToClipboard(CharSequence label, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }
}
